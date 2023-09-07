package com.hexagonkt.processor.schema

import com.hexagonkt.processor.Type
import com.hexagonkt.processor.Field
import com.hexagonkt.processor.schema.PropertyType.*
import com.hexagonkt.helpers.camelToWords
import com.hexagonkt.models.Country
import com.hexagonkt.models.Email
import com.hexagonkt.models.Language
import java.net.URI
import java.net.URL
import java.nio.file.Path
import java.time.LocalDate
import java.time.YearMonth
import java.time.Period
import java.util.*
import kotlin.reflect.KClass

// TODO When creating schema, also include calculated vals with 'read only' values and examples
// TODO Post process Schema/Properties to replace repeated types by $ref to $def
// TODO Use KSP? https://kotlinlang.org/docs/ksp-quickstart.html#pass-options-to-processors
data class SchemaGenerator<T : Any>(
    val dataClass: Type<T>,
    val id: URI,
    val documentation: Documentation? = null,
    val titleCaseNames: Boolean = false,
    val sentenceCaseEnums: Boolean = false,
    val includeSchema: Boolean = true,
    val definitions: String = "\$defs",
//    val examples: List<T> = emptyList(),
) {
    private val numbers: Set<KClass<out Any>> = setOf(
        Int::class,
        Long::class,
        Float::class,
        Double::class
    )

    private val strings: Set<KClass<out Any>> = setOf(
        String::class,
        YearMonth::class,
        LocalDate::class,
        Locale::class,
        URL::class,
        URI::class,
        Path::class,
    )

    private val schemaProperty = Property(
        name = "\$schema",
        title = "Schema used for the document",
        description = "Used to deal with different versions. It also eases the use on some editors",
        type = setOf(STRING),
        format = PropertyFormat.URI
    )

    fun toMap(): Map<*, *> =
        schema().toMap()

    fun schema(): Schema =
        Schema(
            id = id,
            property = Property(ref = "#/$definitions/${dataClass.name}"),
            defs = (dataClass.nestedDataClasses)
                .flatMap(::struct)
                .map { s ->
                    if (s.name == dataClass.name)
                        s.copy(
                            required = emptySet(),
                            properties =
                                (
                                    if (includeSchema) setOf(schemaProperty) + s.properties
                                    else s.properties
                                )
                                .map { if (it.name == "Schema") it.copy(name = "\$schema") else it }
                                .toSet()
                        )
                    else
                        s
                }
                .toSet() + setOf(
                    Property(setOf(NULL, STRING), "NullableYearMonth", """\d{4}-\d{2}"""),
                    Property(setOf(STRING), "YearMonth", """\d{4}-\d{2}"""),
                    Property(
                        setOf(NULL, STRING),
                        "NullablePeriod",
                        """[pP]?\s*(\d+[yY])?\s*(\d+[wW])?\s*(\d+[dD])?"""
                    ),
                    Property(
                        setOf(STRING), "Period", """[pP]?\s*(\d+[yY])?\s*(\d+[wW])?\s*(\d+[dD])?"""
                    ),
                    Property(setOf(NULL, STRING), "NullableLocale", "[a-z]{2}(_[A-Z]{2})?"),
                    Property(setOf(STRING), "Locale", "[a-z]{2}(_[A-Z]{2})?"),
                    Property(setOf(NULL, STRING), "NullableDate", format = PropertyFormat.DATE),
                    Property(setOf(STRING), "Date", format = PropertyFormat.DATE),
                    Property(setOf(NULL, STRING), "NullableUri", format = PropertyFormat.URI),
                    Property(setOf(STRING), "Uri", format = PropertyFormat.URI),
                    Property(setOf(NULL, STRING), "NullableString"),
                    Property(setOf(NULL, NUMBER), "NullableNumber"),
                ),
            definitions = definitions,
        )

    private fun propertyType(field: Field): Set<PropertyType> =
        (if (field.nullable) setOf(NULL) else emptySet()) + when {
            field.collection -> ARRAY
            field.isSingleFieldData -> propertyType(field.singleFieldType ?: error(""))
            field.map -> OBJECT
            field.data -> OBJECT
            else -> propertyType(field.typeJvm)
        }

    private fun propertyType(type: KClass<*>): PropertyType =
        when (type) {
            in strings -> STRING
            Boolean::class -> BOOLEAN
            in numbers -> NUMBER
            else -> STRING
        }

    private fun property(field: Field): Property =
        Property(
            type = propertyType(field),
            name = casedName(field.name),
            enum = casedEnums(field.enumValues),
            title = documentation?.title(field.qualifiedName),
            description = documentation?.description(field.qualifiedName),
            ref =
                when {
                    field.data -> "#/$definitions/" + fieldValueTypeName(field)
                    field.nullable && field.typeJvm == URL::class -> "#/$definitions/NullableUri"
                    field.typeJvm == URL::class -> "#/$definitions/Uri"
                    field.nullable && field.typeJvm == LocalDate::class -> "#/$definitions/NullableDate"
                    field.typeJvm == LocalDate::class -> "#/$definitions/Date"
                    field.nullable && field.typeJvm == Locale::class -> "#/$definitions/NullableLocale"
                    field.typeJvm == Locale::class -> "#/$definitions/Locale"
                    field.nullable && field.typeJvm == Period::class -> "#/$definitions/NullablePeriod"
                    field.typeJvm == Period::class -> "#/$definitions/Period"
                    field.nullable && field.typeJvm == YearMonth::class -> "#/$definitions/NullableYearMonth"
                    field.typeJvm == YearMonth::class -> "#/$definitions/YearMonth"
                    field.nullable && field.typeJvm == String::class -> "#/$definitions/NullableString"
                    propertyType(field) == setOf(NULL, NUMBER) -> "#/$definitions/NullableNumber"
                    else -> null
                },
            format = when (field.typeJvm) {
                Email::class -> PropertyFormat.EMAIL
                URL::class -> PropertyFormat.URI
                LocalDate::class -> PropertyFormat.DATE
                else -> null
            },
            pattern = when (field.typeJvm) {
                Locale::class -> Regex("[a-z]{2}(_[A-Z]{2})?")
                Period::class -> Regex("""[pP]?\s*(\d+[yY])?\s*(\d+[wW])?\s*(\d+[dD])?""")
                YearMonth::class -> Regex("""\d{4}-\d{2}""")
                else -> null
            },
            deprecated = field.deprecated,
//            default = ,
        )
        .let {
            if (field.collection)
                it.copy(
                    items = it.copy(
                        type = setOf(
                            when {
                                field.map -> OBJECT
                                field.data -> OBJECT
                                field.enum -> STRING
                                field.typeJvm in strings -> STRING
                                field.typeJvm == Boolean::class -> BOOLEAN
                                field.typeJvm in numbers -> NUMBER
                                field.typeJvm == String::class -> STRING
                                else -> error("Unhandled case: ${field.typeJvm}")
                            }
                        )
                    ),
                    uniqueItems = field.set,
                )
            else
                it
        }

    private fun fieldValueTypeName(field: Field): String =
        if (field.nullable) "Nullable${field.typeName}" else field.typeName

    private fun casedEnums(values: List<*>): Set<*> =
        if (sentenceCaseEnums)
            values.map { it.toString().replace("_", " ").lowercase() }.toSet()
        else
            values.map { it.toString() }.toSet()

    private fun casedName(name: String): String =
        if (titleCaseNames)
            name
                .camelToWords()
                .joinToString(" ") { w ->
                    w.replaceFirstChar { c -> c.uppercase() }
                }
                .trim()
        else
            name

    private fun struct(dataClass: Type<*>): Set<Property> =
        (
        if (dataClass.isSingleBasicField)
            Property(
                type = setOf(propertyType(dataClass.firstField.typeJvm)),
                name = dataClass.name,
                title = documentation?.title(dataClass.qualifiedName),
                description = documentation?.description(dataClass.qualifiedName),
                deprecated = dataClass.deprecated,
                format = when (dataClass.dataClass) {
                    Email::class -> PropertyFormat.EMAIL
                    else -> null
                },
                pattern = when (dataClass.dataClass) {
                    Language::class -> Regex("[a-z]{2}")
                    Country::class -> Regex("[A-Z]{2}")
                    else -> null
                },
//                default = ,
            )
        else
            Property(
                type = setOf(OBJECT),
                name = dataClass.name,
                title = documentation?.title(dataClass.qualifiedName),
                description = documentation?.description(dataClass.qualifiedName),
                properties = dataClass.fields.map { property(it) }.toSet(),
                required = dataClass.requiredFields.map { casedName(it.name) }.toSet(),
                deprecated = dataClass.deprecated,
//                default = ,
            )
        )
        .let {
            linkedSetOf(
                it,
                it.copy(type = it.type + NULL, name = "Nullable" + it.name)
            )
        }
}
