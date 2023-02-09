package com.hexagonkt.args.formatter

import com.hexagonkt.args.Parameter
import com.hexagonkt.core.camelToWords
import com.hexagonkt.core.wordsToSnake
import java.io.File
import java.net.InetAddress
import java.net.URI
import java.net.URL
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import kotlin.test.Test
import kotlin.test.assertEquals

internal class ParameterFormatterTest {

    private val formatter = ParameterFormatter()

    private fun <T : Any> Parameter<T>.assert(
        summary: String, definition: String, detail: String
    ) : Parameter<T> =
        apply {
            assertEquals(summary, formatter.summary(this))
            assertEquals(definition, formatter.definition(this))
            assertEquals(detail, formatter.detail(this))
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
        str.copy(values = listOf("NAME"))
            .assert("[<sort>]", "<sort>", "The field used to sort items. [$re] Default: NAME")
        str.copy(multiple = true, values = listOf("NAME", "SIZE"))
            .assert(
                "[<sort>]...",
                "<sort>",
                "The field used to sort items. [$re]... Default: [NAME, SIZE]"
            )
    }

    @Test fun `Parameters are described correctly`() {
        val files = listOf(File("./a"), File("./b"))
        val file = Parameter(File::class, "file", "The file whose checksum to calculate")
            .assert("[<file>]", "<file>", "The file whose checksum to calculate. [FILE]")
        file.copy(description = null)
            .assert("[<file>]", "<file>", "[FILE]")
        file.copy(multiple = true)
            .assert("[<file>]...", "<file>", "The file whose checksum to calculate. [FILE]...")
        file.copy(optional = false)
            .assert("<file>", "<file>", "The file whose checksum to calculate. FILE")
        file.copy(optional = false, multiple = true)
            .assert("<file>...", "<file>", "The file whose checksum to calculate. FILE...")
        file.copy(optional = false, multiple = true)
            .assert("<file>...", "<file>", "The file whose checksum to calculate. FILE...")
        file.copy(multiple = true, values = files)
            .assert(
                "[<file>]...",
                "<file>",
                "The file whose checksum to calculate. [FILE]... Default: $files"
            )
        file.copy(values = files.dropLast(1))
            .assert(
                "[<file>]",
                "<file>",
                "The file whose checksum to calculate. [FILE] Default: ${files.first()}"
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
                assertEquals("[${t?.camelToWords()?.wordsToSnake()?.uppercase()}]", formatter.detail(it))
            }
    }
}
