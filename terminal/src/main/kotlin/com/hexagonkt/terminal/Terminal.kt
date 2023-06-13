package com.hexagonkt.terminal

import com.hexagonkt.helpers.exec
import java.lang.System.getenv
import java.lang.management.ManagementFactory.getRuntimeMXBean
import kotlin.Int.Companion.MAX_VALUE

object Terminal {
    val pid: Long by lazy { getRuntimeMXBean().pid }
    val pts: String by lazy { getenv("PTS") ?: "ps o tty= $pid".exec() }

    fun raw() {
        "stty raw -echo -F/dev/$pts".exec()
    }

    fun cooked() {
        "stty cooked echo -F/dev/$pts".exec()
    }

    fun size(): Pair<Int, Int> {
        print(AnsiCursor.SAVE)
        print(AnsiCursor.position(MAX_VALUE, MAX_VALUE))
        print(AnsiCursor.PRINT)
        val r = System.`in`
        val builder = StringBuilder(8)
        while (true) {
            val c = r.read().toChar()
            if (c == 'R')
                break
            else
                if (c !in setOf('[', '\u001B'))
                    builder.append(c)
        }
        print(AnsiCursor.RESTORE)
        return builder.split(';').map { it.toInt() }.let { it.first() to it.last() }
    }
}
