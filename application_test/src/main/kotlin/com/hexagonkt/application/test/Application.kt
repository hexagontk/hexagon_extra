package com.hexagonkt.application.test

import com.hexagonkt.core.Ansi.CSI
import com.hexagonkt.helpers.exec
import com.hexagonkt.terminal.AnsiCursor
import com.hexagonkt.terminal.AnsiScreen
import java.lang.management.ManagementFactory.getRuntimeMXBean

fun main() {
    val pid = getRuntimeMXBean().pid
    val pts = System.getenv("PTS") ?: "ps o tty= $pid".exec()

    "stty raw -echo -F/dev/$pts".exec()
    print(AnsiScreen.privateMode())
    print(AnsiCursor.HIDE)

    print(AnsiCursor.position(999, 999))
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

    print(AnsiCursor.HOME)
    print(AnsiCursor.SHOW)
    print(AnsiScreen.clear())
    "stty cooked echo -F/dev/$pts".exec()
}
