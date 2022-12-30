package com.hexagonkt.args

/*
 # Command processing
 1. Read system.in (check if there is input piped from other command)
 2. Parse command line tokens into options and positional parameters
 3. Check command line definition for invalid options, etc.
 4. Apply defaults: option defaults, defaults in configuration files -user dir, and current dir-
 5. Check missing options: fail if no interactive, ask for input if interactive
 6. Return result
 */

// TODO Create Swing gui from program definition
// TODO For inspiration, check:
//  https://clig.dev/#the-basics
//  http://docopt.org/
// TODO Support interactive settings prompt if mandatory options are missing

// Program, can have stream input, config files (XDG dirs), and parameters
data class Program(
    val version: String? = null,
    val options: Set<Option<*>> = emptySet(), // common options used in all commands
    val commands: Set<Command> = emptySet(),
)

// A program can have multiple commands with their own set of options and positional parameters
data class Command(
    val description: String? = null,
    val examples: List<String> = emptyList(),
    val action: Set<String> = emptySet(),
    val options: Set<Option<*>> = emptySet(),
    val parameters: List<String> = emptyList(),
)

// Parameters can come from the shell or from config files
data class Action(
    val command: Command,
    val options: Map<Option<*>, *>,
    val parameters: List<String>,
)

fun f() {
    val console = System.console()
    val text = if (console != null) {

        val reader = console.reader()
        if (reader.ready())
            reader.readText()
        else ""
    }
    else {
        ""
    }
}
