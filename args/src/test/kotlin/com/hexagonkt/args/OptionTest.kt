package com.hexagonkt.args

import kotlin.test.Test
import kotlin.IllegalArgumentException
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import com.hexagonkt.args.ParameterTest.Companion.assert
import com.hexagonkt.core.camelToWords
import com.hexagonkt.core.wordsToSnake
import java.io.File
import java.net.InetAddress
import java.net.URI
import java.net.URL
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

internal class OptionTest {

    @Test fun `Options with null optional values are correct`() {
        assertEquals(Option(Boolean::class, 'b'), Option(Boolean::class, 'b', name = null))
        assertEquals(Option(Boolean::class, 'b'), Option(Boolean::class, 'b', description = null))
    }

    @Test fun `Invalid options raise errors`() {
        listOf('#', ' ').forEach {
            assertEquals(
                "Short name must be a letter or a digit: $it",
                assertFailsWith<IllegalArgumentException> { Option(String::class, it) }.message
            )
        }
    }

    @Test fun `Options have utility constructor`() {
        val re = "NAME|SIZE|DATE"
        Option(String::class, 's', "sort", "The field used to sort items", Regex(re), value = "NAME")
            .assert(
                "[-s REGEX]",
                "-s REGEX, --sort REGEX",
                "The field used to sort items. [$re] Default: NAME"
            )
    }

    @Test fun `Options with regular expressions are described correctly`() {
        val re = "NAME|SIZE|DATE"
        val str = Option(String::class, 's', "sort", "The field used to sort items", Regex(re))
            .assert(
                "[-s REGEX]",
                "-s REGEX, --sort REGEX",
                "The field used to sort items. [$re]"
            )
        str.copy(multiple = true)
            .assert(
                "[-s REGEX]...",
                "-s REGEX, --sort REGEX",
                "The field used to sort items. [$re]..."
            )
        str.copy(optional = false)
            .assert(
                "-s REGEX",
                "-s REGEX, --sort REGEX",
                "The field used to sort items. $re"
            )
        str.copy(optional = false, multiple = true)
            .assert(
                "-s REGEX...",
                "-s REGEX, --sort REGEX",
                "The field used to sort items. $re..."
            )
        str.copy(optional = false, multiple = true)
            .assert(
                "-s REGEX...",
                "-s REGEX, --sort REGEX",
                "The field used to sort items. $re..."
            )
        str.copy(multiple = true, values = listOf("NAME", "SIZE"))
            .assert(
                "[-s REGEX]...",
                "-s REGEX, --sort REGEX",
                "The field used to sort items. [$re]... Default: [NAME, SIZE]"
            )
        str.copy(values = listOf("NAME", "SIZE"))
            .assert(
                "[-s REGEX]",
                "-s REGEX, --sort REGEX",
                "The field used to sort items. [$re] Default: NAME"
            )
    }

    // TODO ------------------------------------------------------------------------------------------------------------
    @Test fun `Options are described correctly`() {
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

    @Test fun `Options is formatted correctly for all types`() {
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
