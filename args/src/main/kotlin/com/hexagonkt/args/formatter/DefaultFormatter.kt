package com.hexagonkt.args.formatter

import com.hexagonkt.args.Command
import com.hexagonkt.args.Formatter
import com.hexagonkt.args.Program

class DefaultFormatter : Formatter {

    override fun overview(program: Program): String = ""
    override fun version(program: Program): String = ""
    override fun help(program: Program): String = ""
    override fun help(command: Command): String = ""
}
