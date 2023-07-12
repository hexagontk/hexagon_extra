package com.hexagonkt.processor.codec

import com.hexagonkt.processor.Type
import com.hexagonkt.processor.Field

data class EncodersGenerator(val dataClass: Type<*>, val convertEnums: Boolean = true) {

    private val name: String = dataClass.name
    private val function: String = dataClass.name.replaceFirstChar { it.lowercase() }
    private val enumEncoder: String = if (convertEnums) "toWords" else "toString"

    fun allEncoderFunctions(): String =
        dataClass.nestedDataClasses
            .filter { !it.isSingleBasicField }
            .map(::EncodersGenerator)
            .joinToString("\n\n", transform = EncodersGenerator::encoderFunction)

    private fun encoderArgument(field: Field): String {
        val name = field.name
        val singleFieldData = field.isSingleFieldData
        val enum = field.enum
        val data = field.data
        val map = field.map
        val serializableClass = field.basicType
        val valueTypeName = field.typeName
        val nullable = field.nullable
        val collection = field.collection
        val valueTypeConstructorFunction = field.typeName.replaceFirstChar { it.lowercase() }
        val className = field.fieldType.name
        val singleFieldName = field.typeFirstParameter?.name

        return "|        $className::$name to instance.$name" + when {
            map -> ""

            !nullable && !collection && singleFieldData -> ".$singleFieldName"
            !nullable && !collection && data -> ".let(::$valueTypeConstructorFunction)"
            !nullable && !collection && enum -> ".$enumEncoder()"
            !nullable && !collection && !serializableClass -> ".toString()"
            !nullable && collection && singleFieldData -> ".map($valueTypeName::$singleFieldName)"
            !nullable && collection && data -> ".map(::$valueTypeConstructorFunction)"
            !nullable && collection && enum -> ".map(${valueTypeName}::$enumEncoder)"
            !nullable && collection && !serializableClass -> ".map(${valueTypeName}::toString)"

            nullable && !collection && singleFieldData -> "?.$singleFieldName"
            nullable && !collection && data -> "?.let(::$valueTypeConstructorFunction)"
            nullable && !collection && enum -> "?.$enumEncoder()"
            nullable && !collection && !serializableClass -> "?.toString()"
            nullable && collection && singleFieldData -> "?.map($valueTypeName::$singleFieldName)"
            nullable && collection && data -> "?.map(::$valueTypeConstructorFunction)"
            nullable && collection && enum -> "?.map(${valueTypeName}::$enumEncoder)"
            nullable && collection && !serializableClass -> "?.map(${valueTypeName}::toString)"

            else -> ""
        }
    }

    private fun encoderFunction(): String =
        dataClass.fields
            .joinToString(
                prefix =
                """
                    |internal fun $function(instance: $name): Map<*, *> =
                    |    fieldsMapOf(
                    """,
                separator = ",\n",
                postfix = ",\n|    )",
                transform = ::encoderArgument
            )
            .trimMargin()
}
