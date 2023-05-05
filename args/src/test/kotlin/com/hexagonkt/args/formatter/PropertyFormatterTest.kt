package com.hexagonkt.args.formatter

import com.hexagonkt.args.Option
import com.hexagonkt.args.Parameter
import com.hexagonkt.args.Property
import com.hexagonkt.helpers.camelToWords
import com.hexagonkt.helpers.wordsToSnake
import java.io.File
import java.net.InetAddress
import java.net.URI
import java.net.URL
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import kotlin.test.Test
import kotlin.test.assertEquals

internal class PropertyFormatterTest {

    private val formatter = PropertyFormatter()

    private fun <T : Any, P : Property<T>> P.assert(
        summary: String, definition: String, detail: String
    ) : P =
        apply {
            assertEquals(summary, formatter.summary(this))
            assertEquals(definition, formatter.definition(this))
            assertEquals(detail, formatter.detail(this))
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
            assertEquals("[-$sn $ts]", formatter.summary(it))
            assertEquals("-$sn, --$ln $ts", formatter.definition(it))
            assertEquals("Type: [$ts]", formatter.detail(it))
        }
    }

    @Test fun `Boolean option is formatted correctly for all types`() {
        Option(Boolean::class, 'b', "boolean", "Flag option").assert("[-b]", "-b, --boolean", "Flag option. Type: [BOOLEAN]")
    }

    @Test fun `Options have utility constructor`() {
        val re = "NAME|SIZE|DATE"
        val value = "NAME"
        Option(String::class, 's', "sort", "The field used to sort items", Regex(re), defaultValue = value)
            .assert(
                "[-s REGEX]",
                "-s, --sort REGEX",
                "The field used to sort items. Type: [$re]. Default: $value"
            )
    }

    @Test fun `Options with regular expressions are described correctly`() {
        val re = "NAME|SIZE|DATE"
        val str = Option(String::class, 's', "sort", "The field used to sort items", Regex(re))
            .assert( "[-s REGEX]", "-s, --sort REGEX", "The field used to sort items. Type: [$re]")
        str.copy(multiple = true)
            .assert("[-s REGEX]...", "-s, --sort REGEX", "The field used to sort items. Type: [$re]...")
        str.copy(optional = false)
            .assert("-s REGEX", "-s, --sort REGEX", "The field used to sort items. Type: $re")
        str.copy(optional = false, multiple = true)
            .assert("-s REGEX...", "-s, --sort REGEX", "The field used to sort items. Type: $re...")
        str.copy(optional = false, multiple = true)
            .assert("-s REGEX...", "-s, --sort REGEX", "The field used to sort items. Type: $re...")
        str.copy(defaultValues = listOf("NAME"))
            .assert(
                "[-s REGEX]",
                "-s, --sort REGEX",
                "The field used to sort items. Type: [$re]. Default: NAME"
            )
        str.copy(multiple = true, defaultValues = listOf("NAME", "SIZE"))
            .assert(
                "[-s REGEX]...",
                "-s, --sort REGEX",
                "The field used to sort items. Type: [$re].... Default: [NAME, SIZE]"
            )
    }

    @Test fun `Options are described correctly`() {
        val f = File("./a")
        val files = listOf(f, File("./b"))
        val file = Option(File::class, 'f', "file", "The file whose checksum to calculate")
            .assert("[-f FILE]", "-f, --file FILE", "The file whose checksum to calculate. Type: [FILE]")
        file.copy(names = setOf("f"))
            .assert("[-f FILE]", "-f FILE", "The file whose checksum to calculate. Type: [FILE]")
        file.copy(description = null)
            .assert("[-f FILE]", "-f, --file FILE", "Type: [FILE]")
        file.copy(names = setOf("f"), description = null)
            .assert("[-f FILE]", "-f FILE", "Type: [FILE]")
        file.copy(multiple = true)
            .assert("[-f FILE]...", "-f, --file FILE", "The file whose checksum to calculate. Type: [FILE]...")
        file.copy(optional = false)
            .assert("-f FILE", "-f, --file FILE", "The file whose checksum to calculate. Type: FILE")
        file.copy(optional = false, multiple = true)
            .assert("-f FILE...", "-f, --file FILE", "The file whose checksum to calculate. Type: FILE...")
        file.copy(optional = false, multiple = true)
            .assert("-f FILE...", "-f, --file FILE", "The file whose checksum to calculate. Type: FILE...")
        file.copy(defaultValues = listOf(f))
            .assert(
                "[-f FILE]",
                "-f, --file FILE",
                "The file whose checksum to calculate. Type: [FILE]. Default: $f"
            )
        file.copy(names = setOf("file"))
            .assert("[--file FILE]", "--file FILE", "The file whose checksum to calculate. Type: [FILE]")
        file.copy(multiple = true, defaultValues = files)
            .assert(
                "[-f FILE]...",
                "-f, --file FILE",
                "The file whose checksum to calculate. Type: [FILE].... Default: $files"
            )
    }

    @Test fun `Parameters have utility constructor`() {
        val re = "NAME|SIZE|DATE"
        Parameter<String>("sort", "The field used to sort items", Regex(re), defaultValue = "NAME")
            .assert(
                "[<sort>]",
                "<sort>",
                "The field used to sort items. Type: [$re]. Default: NAME"
            )
    }

    @Test fun `Parameters with regular expressions are described correctly`() {
        val re = "NAME|SIZE|DATE"
        val str = Parameter(String::class, "sort", "The field used to sort items", Regex(re))
            .assert("[<sort>]", "<sort>", "The field used to sort items. Type: [$re]")
        str.copy(multiple = true)
            .assert("[<sort>]...", "<sort>", "The field used to sort items. Type: [$re]...")
        str.copy(optional = false)
            .assert("<sort>", "<sort>", "The field used to sort items. Type: $re")
        str.copy(optional = false, multiple = true)
            .assert("<sort>...", "<sort>", "The field used to sort items. Type: $re...")
        str.copy(optional = false, multiple = true)
            .assert("<sort>...", "<sort>", "The field used to sort items. Type: $re...")
        str.copy(defaultValues = listOf("NAME"))
            .assert("[<sort>]", "<sort>", "The field used to sort items. Type: [$re]. Default: NAME")
        str.copy(multiple = true, defaultValues = listOf("NAME", "SIZE"))
            .assert(
                "[<sort>]...",
                "<sort>",
                "The field used to sort items. Type: [$re].... Default: [NAME, SIZE]"
            )
    }

    @Test fun `Parameters are described correctly`() {
        val files = listOf(File("./a"), File("./b"))
        val file = Parameter(File::class, "file", "The file whose checksum to calculate")
            .assert("[<file>]", "<file>", "The file whose checksum to calculate. Type: [FILE]")
        file.copy(description = null)
            .assert("[<file>]", "<file>", "Type: [FILE]")
        file.copy(multiple = true)
            .assert("[<file>]...", "<file>", "The file whose checksum to calculate. Type: [FILE]...")
        file.copy(optional = false)
            .assert("<file>", "<file>", "The file whose checksum to calculate. Type: FILE")
        file.copy(optional = false, multiple = true)
            .assert("<file>...", "<file>", "The file whose checksum to calculate. Type: FILE...")
        file.copy(optional = false, multiple = true)
            .assert("<file>...", "<file>", "The file whose checksum to calculate. Type: FILE...")
        file.copy(multiple = true, defaultValues = files)
            .assert(
                "[<file>]...",
                "<file>",
                "The file whose checksum to calculate. Type: [FILE].... Default: $files"
            )
        file.copy(defaultValues = files.dropLast(1))
            .assert(
                "[<file>]",
                "<file>",
                "The file whose checksum to calculate. Type: [FILE]. Default: ${files.first()}"
            )
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
            .map { Parameter(it, it.simpleName?.camelToWords()?.joinToString("-") ?: "") }
            .forEach {
                val n = it.names.first()
                val t = it.type.simpleName
                assertEquals("[<$n>]", formatter.summary(it))
                assertEquals("<$n>", formatter.definition(it))
                assertEquals("Type: [${t?.camelToWords()?.wordsToSnake()?.uppercase()}]", formatter.detail(it))
            }
    }
}
