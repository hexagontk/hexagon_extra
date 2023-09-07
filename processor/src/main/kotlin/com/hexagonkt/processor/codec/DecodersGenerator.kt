package com.hexagonkt.processor.codec

import com.hexagonkt.processor.Type
import com.hexagonkt.processor.Field
import java.net.URI
import java.net.URL
import java.nio.file.Path
import java.time.LocalDate
import java.time.Period
import java.time.YearMonth
import java.util.*

data class DecodersGenerator(val dataClass: Type<*>) {

    private val name: String = dataClass.name
    private val function: String = dataClass.name.replaceFirstChar { it.lowercase() }

    fun allDecoderFunctions(): String =
        dataClass.nestedDataClasses
            .filter { !it.isSingleBasicField }
            .map(::DecodersGenerator)
            .joinToString("\n\n", transform = DecodersGenerator::decoderFunction)

    private fun decoderArgument(field: Field): String {
        val name = field.name
        val singleFieldData = field.isSingleFieldData
        val enum = field.enum
        val data = field.data
        val map = field.map
        val serializableClass = field.basicType
        val valueTypeName = field.typeName
        val optional = field.optional
        val valueTypeJvm = field.typeJvm
        val collection = field.collection
        val valueTypeConstructorFunction = field.typeName.replaceFirstChar { it.lowercase() }
        val className = field.fieldType.name
        val singleFieldType = field.singleFieldType

        val serializableType = when {
            singleFieldData -> singleFieldType?.simpleName
            enum -> "String"
            data -> "Map"
            map -> "Map"
            serializableClass -> valueTypeName
            else -> "String"
        }
        val fetchType = if (optional) "get$serializableType" else "require$serializableType"
        val fetchFunction = if (collection) "${fetchType}s" else fetchType
        val decodeFunction = when (if (singleFieldData) singleFieldType else valueTypeJvm) {
            Locale::class -> "::parseLocale"
            LocalDate::class -> "::parseLocalDate"
            YearMonth::class -> "YearMonth::parse"
            URL::class -> "::URL"
            URI::class -> "::URI"
            Path::class -> "Path::of"
            Period::class -> "::parsePeriod"
            else -> ""
        }
        val conversionMapper = when {
            collection && enum -> ".map { text -> text.toEnum(${valueTypeName}::valueOf) }"
            collection && singleFieldData -> ".map(::$valueTypeName)"
            collection && data -> ".map(::${valueTypeConstructorFunction})"

            !collection && enum -> ".toEnum(${valueTypeName}::valueOf)"
            !collection && singleFieldData -> ".let(::$valueTypeName)"
            !collection && data -> ".let(::${valueTypeConstructorFunction})"

            decodeFunction == "" -> ""
            collection -> ".map($decodeFunction)"

            else -> ".let($decodeFunction)"
        }
        val setConverter =
            if (collection) if (field.set) ".toSet()" else ".toList()"
            else ""

        return if (optional) {
            val v = if (conversionMapper.isNotBlank()) "?$conversionMapper" else ""
            val s = if (setConverter.isNotBlank()) "?$setConverter" else ""
            "|            $name = data.$fetchFunction($className::$name)$v$s ?: it.$name"
        } else {
            "|        $name = data.$fetchFunction($className::$name)$conversionMapper$setConverter"
        }
    }

    private fun decoderFunction(): String {
        val requiredCode =
            if (dataClass.requiredFields.isEmpty())
                "|    $name()"
            else
                dataClass.requiredFields.joinToString(
                    prefix = "|    $name(\n",
                    separator = ",\n",
                    postfix = ",\n|    )",
                    transform = ::decoderArgument
                )

        val optionalCode =
            if (dataClass.optionalFields.isEmpty())
                ""
            else
                dataClass.optionalFields.joinToString(
                    prefix = "|    .let {\n|        it.copy(\n",
                    separator = ",\n",
                    postfix = ",\n|        )\n|    }",
                    transform = ::decoderArgument
                )

        return """
            |internal fun $function(data: Map<*, *>): $name =
            $requiredCode
            $optionalCode
        """.trimMargin().trim()
    }
}
