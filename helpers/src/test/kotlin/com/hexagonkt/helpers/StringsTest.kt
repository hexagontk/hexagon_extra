package com.hexagonkt.helpers

import com.hexagonkt.core.eol
import com.hexagonkt.helpers.StringsTest.Size.S
import com.hexagonkt.helpers.StringsTest.Size.X_L
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.condition.DisabledInNativeImage
import kotlin.test.Test
import kotlin.IllegalArgumentException
import kotlin.test.*

internal class StringsTest {

    private enum class Size { S, M, L, X_L }

    @Test fun `Strings can be converted to enum values`() {
        assertEquals(S, "s".toEnum(Size::valueOf))
        assertEquals(S, "S".toEnum(Size::valueOf))
        assertEquals(X_L, "x l".toEnum(Size::valueOf))
        assertEquals(X_L, "X L".toEnum(Size::valueOf))
        assertEquals(X_L, "X_L".toEnum(Size::valueOf))

        val e = assertFailsWith<IllegalArgumentException> {
            assertEquals(Size.M, "z".toEnum(Size::valueOf))
        }
        val message = e.message ?: "_"
        assertContains(message, "No enum constant com.hexagonkt.helpers.StringsTest")
        assertContains(message, "Size.Z")

        assertEquals(S, "s".toEnumOrNull(Size::valueOf))
        assertNull("z".toEnumOrNull(Size::valueOf))
    }

    @Test
    @DisabledInNativeImage
    fun `Find groups takes care of 'nulls'`() {
        val reEmpty = mockk<Regex>()
        every { reEmpty.find(any()) } returns null

        assert(reEmpty.findGroups("").isEmpty())

        val matchGroupCollection = mockk<MatchGroupCollection>()
        every { matchGroupCollection.size } returns 1
        every { matchGroupCollection.iterator() } returns listOf<MatchGroup?>(null).iterator()
        val matchResult = mockk<MatchResult>()
        every { matchResult.groups } returns matchGroupCollection
        val reNullGroup = mockk<Regex>()
        every { reNullGroup.find(any()) } returns matchResult

        assert(reNullGroup.findGroups("").isEmpty())
    }

    @Test fun `Banner logs the proper message`() {
        var banner = "alfa line".banner()
        assert(banner.contains("alfa line"))
        assert(banner.contains("*********"))

        banner = "".banner()
        assertEquals(eol + eol, banner)

        banner =
            """alfa
            looong line
            beta
            tango""".trimIndent().trim().banner()
        assert(banner.contains("alfa"))
        assert(banner.contains("beta"))
        assert(banner.contains("tango"))
        assert(banner.contains("looong line"))
        assert(banner.contains("***********"))

        assertEquals(123, sequenceOf<Int>().maxOrElse(123))

        val banner1 = "foo".banner(">")
        assert(banner1.contains("foo"))
        assert(banner1.contains(">>>"))
    }

    @Test fun `Normalize works as expected`() {
        val striped = "áéíóúñçÁÉÍÓÚÑÇ".stripAccents()
        assertEquals("aeiouncAEIOUNC", striped)
    }

    @Test fun `Utf8 returns proper characters`() {
        assertEquals("👍", utf8(0xF0, 0x9F, 0x91, 0x8D))
        assertEquals("👎", utf8(0xF0, 0x9F, 0x91, 0x8E))
    }

    @Test fun `Texts are translated properly to enum values`() {
        val sources = listOf("A", "Ab", "ab c", "DE f", "  Gh Y  ")
        val expected = listOf("A", "AB", "AB_C", "DE_F", "GH_Y")
        assertEquals(expected, sources.map(String::toEnumValue))
    }
}
