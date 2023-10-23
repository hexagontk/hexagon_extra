package com.hexagonkt.processor

import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.KVisibility.PUBLIC
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor

// TODO Generate codec tests
data class Type<T : Any>(
    val dataClass: KClass<T>,
) {
    val name: String = dataClass.simpleName ?: error("Can't get type name")
    val qualifiedName: String = dataClass.qualifiedName ?: error("Can't get qualified name")
    val declaredProperties: Map<String, KProperty1<T, *>> =
        dataClass.declaredMemberProperties.associateBy { it.name }
    val deprecated: Boolean =
        dataClass.annotations.firstOrNull { it is Deprecated } as? Deprecated != null

    private val constructor = dataClass.primaryConstructor ?: error("Type must have a constructor")
    val fields: List<Field> = constructor.parameters
        .filter { declaredProperties[it.name]?.visibility == PUBLIC }
        .map { Field(this, it) }
    val firstField: Field = fields.firstOrNull() ?: error("Type must have at least a field")
    val isSingleBasicField: Boolean = fields.size == 1 && firstField.basicType

    private val groupedFields: Map<Boolean, List<Field>> = fields.groupBy { it.optional }
    val optionalFields: List<Field> = groupedFields[true] ?: emptyList()
    val requiredFields: List<Field> = groupedFields[false] ?: emptyList()

    val nestedDataClasses: Set<Type<*>> by lazy {
        fields
            .filter { it.data }
            .map { Type(it.typeJvm) }
            .flatMap { it.nestedDataClasses }
            .toSet() + this
    }

    init {
        require(dataClass.isData)
    }

//    fun instanceToMap(instance: T): Map<*, *> =
//        dataClass.declaredMemberProperties
//            .filter { it.name in parameters.map { p -> p.name } }
//            .associate { it.name to it.get(instance) }
}
