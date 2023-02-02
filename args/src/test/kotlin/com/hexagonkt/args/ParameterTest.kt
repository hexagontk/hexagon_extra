package com.hexagonkt.args

import com.hexagonkt.core.camelToWords
import com.hexagonkt.core.wordsToSnake
import java.io.File
import java.lang.IllegalArgumentException
import java.net.InetAddress
import java.net.URI
import java.net.URL
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class ParameterTest {

    companion object {
        fun <T : Any, P : Property<T>> P.assert(
            summary: String, definition: String, detail: String
        ) : P =
            apply {
                assertEquals(summary, summary())
                assertEquals(definition, definition())
                assertEquals(detail, detail())
            }
    }

    @Test fun `Invalid parameters fail with exceptions`() {
        assertFailsWith<IllegalArgumentException> { Parameter(Regex::class) }
            .message.let { assert(it?.contains("not in allowed types") ?: false) }

        setOf("", " ", "a", "Ab", "ab_c").forEach { n ->
            assertFailsWith<IllegalArgumentException> { Parameter(String::class, n) }
                .message.let { assert(it?.contains("Name must comply with") ?: false) }
        }

        assertFailsWith<IllegalArgumentException> { Parameter(Int::class, regex = Regex(".*")) }
            .message.let { assert(it?.contains("Regex can only be used for 'string'") ?: false) }

        assertFailsWith<IllegalArgumentException> { Parameter(String::class, regex = Regex("A"), values = listOf("a")) }
            .message.let { assert(it?.contains("Value should match the 'A' regex: a") ?: false) }

        assertFailsWith<IllegalArgumentException> { Parameter(Int::class, description = " ") }
    }

    @Test fun `Parameters have utility constructor`() {
        val re = "NAME|SIZE|DATE"
        Parameter(String::class, "sort", "The field used to sort items", Regex(re), value = "NAME")
            .assert("[<sort>]", "<sort>", "The field used to sort items. [$re] Default: NAME")
    }

    @Test fun `Parameters with regular expressions are described correctly`() {
        val re = "NAME|SIZE|DATE"
        val str = Parameter(String::class, "sort", "The field used to sort items", Regex(re))
            .assert("[<sort>]", "<sort>", "The field used to sort items. [$re]")
        str.copy(multiple = true)
            .assert("[<sort>]...", "<sort>", "The field used to sort items. [$re]...")
        str.copy(optional = false)
            .assert("<sort>", "<sort>", "The field used to sort items. $re")
        str.copy(optional = false, multiple = true)
            .assert("<sort>...", "<sort>", "The field used to sort items. $re...")
        str.copy(optional = false, multiple = true)
            .assert("<sort>...", "<sort>", "The field used to sort items. $re...")
        str.copy(multiple = true, values = listOf("NAME", "SIZE"))
            .assert("[<sort>]...", "<sort>", "The field used to sort items. [$re]... Default: [NAME, SIZE]")
        str.copy(values = listOf("NAME", "SIZE"))
            .assert("[<sort>]", "<sort>", "The field used to sort items. [$re] Default: NAME")
    }

    @Test fun `Parameters are described correctly`() {
        val file = Parameter(File::class, "file", "The file whose checksum to calculate")
            .assert("[<file>]", "<file>", "The file whose checksum to calculate. [FILE]")
        file.copy(name = null)
            .assert("[<null>]", "<null>", "The file whose checksum to calculate. [FILE]")
        file.copy(description = null)
            .assert("[<file>]", "<file>", "[FILE]")
        file.copy(name = null, description = null)
            .assert("[<null>]", "<null>", "[FILE]")
        file.copy(multiple = true)
            .assert("[<file>]...", "<file>", "The file whose checksum to calculate. [FILE]...")
        file.copy(optional = false)
            .assert("<file>", "<file>", "The file whose checksum to calculate. FILE")
        file.copy(optional = false, multiple = true)
            .assert("<file>...", "<file>", "The file whose checksum to calculate. FILE...")
        file.copy(optional = false, multiple = true)
            .assert("<file>...", "<file>", "The file whose checksum to calculate. FILE...")
        file.copy(multiple = true, values = listOf(File("./a"), File("./b")))
            .assert("[<file>]...", "<file>", "The file whose checksum to calculate. [FILE]... Default: [./a, ./b]")
        file.copy(values = listOf(File("./a"), File("./b")))
            .assert("[<file>]", "<file>", "The file whose checksum to calculate. [FILE] Default: ./a")
    }

    @Test fun `Summary is formatted correctly for all types`() {
        listOf(
            Boolean::class,
            Int::class,
            Long::class,
            Float::class,
            Double::class,
            String::class,
            InetAddress::class,
            URL::class,
            URI::class,
            File::class,
            LocalDate::class,
            LocalTime::class,
            LocalDateTime::class,
        )
        .map { Parameter(it, it.simpleName?.camelToWords()?.joinToString("-")) }
        .forEach {
            val n = it.name
            val t = it.type.simpleName
            assertEquals("[<$n>]", it.summary())
            assertEquals("<$n>", it.definition())
            assertEquals("[${t?.camelToWords()?.wordsToSnake()?.uppercase()}]", it.detail())
        }
    }
}
