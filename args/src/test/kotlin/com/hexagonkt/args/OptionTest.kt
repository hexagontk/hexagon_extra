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
        val value = "NAME"
        Option(String::class, 's', "sort", "The field used to sort items", Regex(re), value = value)
            .assert(
                "[-s REGEX]",
                "-s, --sort REGEX",
                "The field used to sort items. [$re] Default: $value"
            )
    }

    @Test fun `Options with regular expressions are described correctly`() {
        val re = "NAME|SIZE|DATE"
        val str = Option(String::class, 's', "sort", "The field used to sort items", Regex(re))
            .assert( "[-s REGEX]", "-s, --sort REGEX", "The field used to sort items. [$re]")
        str.copy(multiple = true)
            .assert("[-s REGEX]...", "-s, --sort REGEX", "The field used to sort items. [$re]...")
        str.copy(optional = false)
            .assert("-s REGEX", "-s, --sort REGEX", "The field used to sort items. $re")
        str.copy(optional = false, multiple = true)
            .assert("-s REGEX...", "-s, --sort REGEX", "The field used to sort items. $re...")
        str.copy(optional = false, multiple = true)
            .assert("-s REGEX...", "-s, --sort REGEX", "The field used to sort items. $re...")
        str.copy(values = listOf("NAME"))
            .assert("[-s REGEX]", "-s, --sort REGEX", "The field used to sort items. [$re] Default: NAME")
        str.copy(multiple = true, values = listOf("NAME", "SIZE"))
            .assert(
                "[-s REGEX]...",
                "-s, --sort REGEX",
                "The field used to sort items. [$re]... Default: [NAME, SIZE]"
            )
    }

    @Test fun `Options are described correctly`() {
        val f = File("./a")
        val files = listOf(f, File("./b"))
        val file = Option(File::class, 'f', "file", "The file whose checksum to calculate")
            .assert("[-f FILE]", "-f, --file FILE", "The file whose checksum to calculate. [FILE]")
        file.copy(name = null)
            .assert("[-f FILE]", "-f FILE", "The file whose checksum to calculate. [FILE]")
        file.copy(description = null)
            .assert("[-f FILE]", "-f, --file FILE", "[FILE]")
        file.copy(name = null, description = null)
            .assert("[-f FILE]", "-f FILE", "[FILE]")
        file.copy(multiple = true)
            .assert("[-f FILE]...", "-f, --file FILE", "The file whose checksum to calculate. [FILE]...")
        file.copy(optional = false)
            .assert("-f FILE", "-f, --file FILE", "The file whose checksum to calculate. FILE")
        file.copy(optional = false, multiple = true)
            .assert("-f FILE...", "-f, --file FILE", "The file whose checksum to calculate. FILE...")
        file.copy(optional = false, multiple = true)
            .assert("-f FILE...", "-f, --file FILE", "The file whose checksum to calculate. FILE...")
        file.copy(values = listOf(f))
            .assert("[-f FILE]", "-f, --file FILE", "The file whose checksum to calculate. [FILE] Default: $f")
        file.copy(shortName = null)
            .assert("[--file FILE]", "--file FILE", "The file whose checksum to calculate. [FILE]")
        file.copy(multiple = true, values = files)
            .assert(
                "[-f FILE]...",
                "-f, --file FILE",
                "The file whose checksum to calculate. [FILE]... Default: $files"
            )
    }

    @Test fun `Options are formatted correctly for all types`() {
        listOf(
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
        .map {
            val simpleName = it.simpleName
            Option(it, simpleName?.first()?.lowercaseChar(), simpleName?.camelToWords()?.joinToString("-"))
        }
        .forEach {
            val t = it.type.simpleName
            val ts = t?.camelToWords()?.wordsToSnake()?.uppercase()
            val sn = t?.first()?.lowercaseChar()
            val ln = t?.camelToWords()?.joinToString("-")
            assertEquals("[-$sn $ts]", it.summary())
            assertEquals("-$sn, --$ln $ts", it.definition())
            assertEquals("[$ts]", it.detail())
        }
    }

    @Test fun `Boolean option is formatted correctly for all types`() {
        Option(Boolean::class, 'b', "boolean", "Flag option").assert("[-b]", "-b, --boolean", "Flag option. [BOOLEAN]")
    }
}
