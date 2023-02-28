package com.hexagonkt.terminal

/**
 * Constants for console cursor handling with [ANSI](https://en.wikipedia.org/wiki/ANSI_escape_code)
 * codes.
 *
 * TODO https://invisible-island.net/xterm/ctlseqs/ctlseqs.html
 */
object AnsiCursor {
    /** Move cursor to origin. */
    const val HOME: String = "${Ansi.CSI}H"
    const val SAVE: String = "\u001B${'7'}"
    const val RESTORE: String = "\u001B${'8'}"
    const val SHOW: String = "${Ansi.CSI}?25h"
    const val HIDE: String = "${Ansi.CSI}?25l"

    fun position(r: Int = 1, c: Int = 1): String =
        "${Ansi.CSI}${r};${c}H"

    fun up(d: Int = 1): String =
        "${Ansi.CSI}${d}A"

    fun down(d: Int = 1): String =
        "${Ansi.CSI}${d}B"

    fun forward(d: Int = 1): String =
        "${Ansi.CSI}${d}C"

    fun backward(d: Int = 1): String =
        "${Ansi.CSI}${d}D"
}
