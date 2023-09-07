package com.hexagonkt.application.test

import com.hexagonkt.terminal.AnsiCursor
import com.hexagonkt.terminal.AnsiMouse
import com.hexagonkt.terminal.AnsiScreen
import com.hexagonkt.terminal.Terminal

fun main() {
    Terminal.raw()
    print(AnsiScreen.privateMode())
    print(AnsiCursor.HIDE)
    print(AnsiCursor.HOME)
    print(Terminal.size())
    print(AnsiMouse.ENABLE)

    val r = System.`in`
    while (true) {
        when (val c = r.read().toChar()) {
            'q' -> break
            '\u001B' -> print("ESC")
            else -> print(c)
        }
    }

    print(AnsiMouse.DISABLE)
    print(AnsiScreen.privateMode(false))
    print(AnsiCursor.HOME)
    print(AnsiCursor.SHOW)
    print(AnsiScreen.clear())
    Terminal.cooked()
}
