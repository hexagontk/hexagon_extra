package com.hexagonkt.application.test

import com.hexagonkt.core.Ansi.CSI
import com.hexagonkt.terminal.AnsiCursor
import com.hexagonkt.terminal.AnsiScreen
import com.hexagonkt.terminal.Terminal
import kotlin.Int.Companion.MAX_VALUE

fun main() {
    Terminal.raw()
    print(AnsiScreen.privateMode())
    print(AnsiCursor.HIDE)

    (AnsiCursor.position(MAX_VALUE, MAX_VALUE))
    print("${CSI}6n") // Print cursor position (placed at bottom-right corner)
    print(AnsiCursor.HOME)

    val r = System.`in`
    while (true) {
        val c = r.read()
        if (c == 'q'.code)
            break
        else
            print("${c.toChar()} ($c)")
    }

    print(AnsiScreen.privateMode(false))
    print(AnsiCursor.HOME)
    print(AnsiCursor.SHOW)
    print(AnsiScreen.clear())
    Terminal.cooked()
}
