package com.hexagonkt.args

interface Formatter<T> {
    fun summary(component: T, program: Program? = null): String
    fun definition(component: T, program: Program? = null): String
    fun detail(component: T, program: Program? = null): String
}
