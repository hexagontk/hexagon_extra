package com.hexagonkt.args

import kotlin.test.Test

internal class ProgramTest {

    @Test fun `Program is created properly`() {
        val args = arrayOf("-a", "--b" , "-c")
        val iterator = args.iterator()

        iterator.forEach {
            when {
                it.startsWith("--") -> iterator.next()
                it.startsWith("-") -> println(it)
            }
        }

//        for (arg in iterator) {
//            when {
//                arg.startsWith("--") -> iterator.next()
//                arg.startsWith("-") -> println(arg)
//            }
//        }
    }
}
