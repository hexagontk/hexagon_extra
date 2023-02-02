package com.hexagonkt.args

import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test
import kotlin.IllegalStateException
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class OptionParserTest {

    private lateinit var optionParser: OptionParser

    @BeforeEach fun setUp() {
        optionParser = OptionParser
    }

    @Test fun `Parse string option`() {
        val verbose = Option(String::class, 'o', "option")
        val actual = optionParser.parse(listOf(verbose), arrayOf("--option=value"))
        val expected: Map<Option<*>, *> = mapOf(verbose to "value")

        assertEquals(expected, actual)
    }

    @Test fun `Parse boolean option without value`() {
        val verbose = Option(Boolean::class, 'v', "verbose")
        val actual = optionParser.parse(listOf(verbose), arrayOf("--verbose"))
        val expected: Map<Option<*>, *> = mapOf(verbose to true)

        assertEquals(expected, actual)
    }
    @Test fun `Parse string option with two words name`() {
        val verbose = Option(String::class, 'o', "long-option")
        val actual = optionParser.parse(listOf(verbose), arrayOf("--long-option=value"))
        val expected: Map<Option<*>, *> = mapOf(verbose to "value")

        assertEquals(expected, actual)
    }

    @Test fun `Parse options with short name`() {
        val flag1 = Option(type = Boolean::class, 'a')
        val flag2 = Option(type = Boolean::class, 'b')
        val flag3 = Option(type = Boolean::class, 'c')

        val actual = optionParser.parse(listOf(flag1, flag2, flag3), arrayOf("-a", "-b", "-c"))
        val expected: Map<Option<*>, *> = mapOf(flag1 to true, flag2 to true, flag3 to true)

        assertEquals(expected, actual)
    }

    @Test fun `Parse options with short name in the compacted fashion`() {
        val flag1 = Option(type = Boolean::class, 'a')
        val flag2 = Option(type = Boolean::class, 'b')
        val flag3 = Option(type = Boolean::class, 'c')

        val actual = optionParser.parse(listOf(flag1, flag2, flag3), arrayOf("-abc"))
        val expected: Map<Option<*>, *> = mapOf(flag1 to true, flag2 to true, flag3 to true)

        assertEquals(expected, actual)
    }
    @Test fun `Parse should fail if the some of the args is invalid`() {

        val someArg = Option(String::class, 's', "some-arg")

        val e = assertFailsWith<IllegalStateException> {
            optionParser.parse(listOf(someArg), arrayOf("---some-arg=value"))
        }

        assertEquals("InvalidOptionSyntaxException", e.message)
    }

    @Test fun `Parse should fail if a short named arg is invalid`() {

        val someArg = Option(Boolean::class, 's', "some-arg")
        val another = Option(Boolean::class, 'a', "another-arg")

        val e = assertFailsWith<IllegalStateException> {
            optionParser.parse(listOf(someArg, another), arrayOf("-s-a"))
        }

        assertEquals("InvalidOptionSyntaxException", e.message)
    }

    @Test fun `Parse both long and short named options`() {
        val shortFlag = Option(type = Boolean::class, 's')
        val longFlag = Option(type = Boolean::class, 'l', name = "long")
        val longValue = Option(String::class, 'v', "value")
        val options = listOf(shortFlag, longFlag, longValue)

        val actual = optionParser.parse(options, arrayOf("-s", "--long", "--value=some"))
        val expected: Map<Option<*>, *> =
            mapOf(shortFlag to true, longFlag to true, longValue to "some")

        assertEquals(expected, actual)
    }

    @Test fun `Parse long named flag with false as value`() {
        val longFlag = Option(type = Boolean::class, 'l', name = "long")

        val actual = optionParser.parse(listOf(longFlag), arrayOf("--long=false"))
        val expected: Map<Option<*>, *> = mapOf(longFlag to false)

        assertEquals(expected, actual)
    }

    @Test fun `Parse options with int values`() {
        val level = Option(type = Int::class, 'l', name = "level")
        val another = Option(Boolean::class, 'a', "another-arg")
        val options = listOf(level, another)

        val actual = optionParser.parse(options, arrayOf("--another-arg", "--level=3"))
        val expected: Map<Option<*>, *> = mapOf(level to 3, another to true)

        assertEquals(expected, actual)
    }

    @Test fun `Parse options with double values`() {
        val time = Option(type = Double::class, 't', name = "time")
        val another = Option(Boolean::class, 'a', "another-arg")
        val options = listOf(time, another)

        val actual = optionParser.parse(options, arrayOf("--time=2.5", "--another-arg"))
        val expected: Map<Option<*>, *> = mapOf(time to 2.5, another to true)

        assertEquals(expected, actual)
    }
}
