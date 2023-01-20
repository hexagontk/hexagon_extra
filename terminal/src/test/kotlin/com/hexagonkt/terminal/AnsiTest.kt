package com.hexagonkt.terminal

import kotlin.test.Test

internal class AnsiTest {

    @Test fun `ANSI control codes are printed properly`() {
        print("DEMO")
        Thread.sleep(2_000)
        print(AnsiScreen.privateMode())
        Thread.sleep(2_000)
        print(AnsiCursor.HOME)
        print(AnsiCursor.SAVE)
        Thread.sleep(2_000)
        print("Start")
        Thread.sleep(2_000)
        print(AnsiCursor.position(5, 10))
        Thread.sleep(2_000)
        print("End")
        Thread.sleep(2_000)
        print(AnsiCursor.RESTORE)
        Thread.sleep(2_000)
        print("Overwrite")
        Thread.sleep(2_000)
        print(AnsiScreen.privateMode(false))
        Thread.sleep(2_000)
        print("END")
        Thread.sleep(5_000)
    }
}
