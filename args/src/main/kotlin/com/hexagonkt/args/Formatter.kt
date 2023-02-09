package com.hexagonkt.args

interface Formatter {

    fun overview(program: Program): String
    fun version(program: Program): String
    fun help(program: Program): String
    fun help(command: Command): String
}
