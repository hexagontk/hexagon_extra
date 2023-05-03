package com.hexagonkt.terminal

import com.hexagonkt.helpers.exec
import java.lang.System.getenv
import java.lang.management.ManagementFactory.getRuntimeMXBean

object Terminal {

    val pid: Long by lazy { getRuntimeMXBean().pid }
    val pts: String by lazy { getenv("PTS") ?: "ps o tty= $pid".exec() }

    fun raw() {
        "stty raw -echo -F/dev/$pts".exec()
    }

    fun cooked() {
        "stty cooked echo -F/dev/$pts".exec()
    }

    fun size() {

    }
}
