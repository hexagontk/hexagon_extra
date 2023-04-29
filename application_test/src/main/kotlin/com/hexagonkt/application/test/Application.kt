package com.hexagonkt.application.test

import com.hexagonkt.helpers.exec
import com.hexagonkt.terminal.AnsiCursor
import com.hexagonkt.terminal.AnsiScreen

fun main() {
    // ps o tty=,pid= <jvm-pid>
    // Pass variable to program (maybe in launcher) PTS=$(tty) command
    val pts = System.getenv("PTS") ?: "/dev/pts/0"
    "stty raw -echo -F$pts".exec()
    print(AnsiScreen.privateMode())
    val r = System.`in`
    print(AnsiCursor.HIDE)
    print(AnsiCursor.HOME)
    while (true) {
        val c = r.read()
        if (c == 'q'.code)
            break
        else
            println(c)
    }
    print(AnsiCursor.HOME)
    print(AnsiCursor.SHOW)
    print(AnsiScreen.clear())
    "stty cooked echo -F$pts".exec()
}

private fun terminal() {
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
