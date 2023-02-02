package com.hexagonkt.application.test

import com.hexagonkt.args.Program
import com.hexagonkt.terminal.AnsiCursor
import com.hexagonkt.terminal.AnsiScreen

fun main(vararg args: String) {
    val p = Program("test")
    println(">>> (${args.size})\n${args.toList()}")
    println(">>>\n${p.input()}")
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
