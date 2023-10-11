package com.hexagonkt.terminal

import com.hexagonkt.core.text.Ansi.CSI

object AnsiMouse {
    const val ENABLE: String = "${CSI}?1003h"
    const val DISABLE: String = "${CSI}?1003l"
    const val ENABLE_SGR: String = "${CSI}?1006h"
    const val DISABLE_SGR: String = "${CSI}?1006l"
}
