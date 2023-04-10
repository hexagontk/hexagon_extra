package com.hexagonkt.application.test

import com.hexagonkt.terminal.AnsiCursor
import com.hexagonkt.terminal.AnsiScreen

fun main() {
    val s = ProcessBuilder().command("/bin/sh", "-c", "stty raw </dev/tty").start().waitFor()
    assert(s == 0)
    print(AnsiScreen.privateMode())
    val r = System.`in`
    print(AnsiCursor.HIDE)
    while (true) {
        val c = r.read()
        if (c == 'q'.code)
            break
        else
            println(c)
    }
    print(AnsiScreen.clear())
    val s1 = ProcessBuilder().command("/bin/sh", "-c", "stty cooked </dev/tty").start().waitFor()
    assert(s1 == 0)
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
