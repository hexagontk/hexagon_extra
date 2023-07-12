package com.hexagonkt.processor

import kotlin.reflect.*
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.starProjectedType
import kotlin.reflect.full.withNullability
import kotlin.reflect.jvm.jvmErasure

data class Field(
    val fieldType: Type<*>,
    val parameter: KParameter,
) {
    private companion object {
        val collectionType: KType = Collection::class.starProjectedType
        val setType: KType = Set::class.starProjectedType
        val mapType: KType = Map::class.starProjectedType
        val enumType: KType = Enum::class.starProjectedType
        val basicTypes: Set<KClass<*>> = setOf(
            Boolean::class, Int::class, Long::class, Float::class, Double::class, String::class
        )
    }

    val optional: Boolean = parameter.isOptional
    val name: String = parameter.name ?: error("Can't get parameter name")
    val qualifiedName: String = fieldType.qualifiedName + "." + name

    private val dataType: KType = parameter.type
    val nullable: Boolean = dataType.isMarkedNullable
    val collection: Boolean = dataType.isSubtypeOf(collectionType)
    val map: Boolean = dataType.isSubtypeOf(mapType)
    val set: Boolean = collection && dataType.isSubtypeOf(setType)

    private val typeArgument: KType by lazy { dataType.arguments.first().type ?: error("No type") }
    private val type: KType = if (collection) typeArgument else dataType
    val typeJvm: KClass<*> = type.jvmErasure
    val typeName: String = typeJvm.simpleName ?: error("Can't get value type name")

    val data: Boolean = typeJvm.isData
    val enum: Boolean = type.withNullability(false).isSubtypeOf(enumType)
    val basicType: Boolean = typeJvm in basicTypes
    val enumValues: List<Any> = if (enum) typeJvm.java.enumConstants.toList() else emptyList()

    private val dataClassField: KProperty1<out Any, *>? = fieldType.declaredProperties[name]
    private val annotations: List<Annotation> = dataClassField?.annotations ?: emptyList()
    val deprecated: Boolean = annotations.firstOrNull { it is Deprecated } as? Deprecated != null

    private val typePrimaryConstructor = typeJvm.primaryConstructor
    private val typeParameters: List<KParameter> = typePrimaryConstructor?.parameters ?: emptyList()
    val typeFirstParameter: KParameter? = typeParameters.firstOrNull()
    private val typeFirstParameterJvm: KClass<*>? = typeFirstParameter?.type?.jvmErasure

    val isSingleFieldData: Boolean =
        data && typeParameters.size == 1 && (typeFirstParameterJvm in basicTypes)

    val singleFieldType: KClass<*>? = if (isSingleFieldData) typeFirstParameterJvm else null
}
