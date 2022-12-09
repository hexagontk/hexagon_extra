package com.hexagonkt.args

import java.net.URL

// TODO Create Swing gui from program definition
// TODO For inspiration, check:
//  https://clig.dev/#the-basics
//  https://picocli.info/
// TODO Support interactive settings prompt if mandatory options are missing

val buildProperties = URL("classpath:META-INF/build.properties").readText()

// Program, can have stream input, config files (XDG dirs), and parameters
data class Program(
    val description: String? = null,
    val options: Set<Option<*>> = emptySet(),
    val commands: Set<Command> = emptySet(),
)

// A program can have multiple commands with their own set of options and positional parameters
data class Command(
    val action: Set<String> = emptySet(),
    val options: Set<Option<*>> = emptySet(),
    val parameters: List<String> = emptyList(),
)

// Parameters can come from the shell or from config files
data class Action(
    val command: Command,
    val options: Map<Option<*>, *>,
)
