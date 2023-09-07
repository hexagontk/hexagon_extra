package com.hexagonkt.terminal

import com.hexagonkt.core.Ansi.CSI

object AnsiMouse {
    const val ENABLE: String = "${CSI}?1003h"
    const val DISABLE: String = "${CSI}?1003l"
}
