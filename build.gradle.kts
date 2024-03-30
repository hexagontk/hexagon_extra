import org.gradle.api.tasks.wrapper.Wrapper.DistributionType.ALL

/*
 * Main build script, responsible for:
 *
 *  1. Publishing: upload binaries and templates to Maven Central
 *  2. Releasing: tag source code in GitHub
 *  3. Coverage report: aggregated coverage report for all modules
 *  4. Handle Docker containers: take care of tasks depending on Docker and containers clean up
 *
 * Plugins that are not used in the root project (this one) are only applied by the modules that use
 * them.
 */

plugins {
    kotlin("jvm") version("1.9.23") apply(false)

    id("idea")
    id("eclipse")
    id("org.jetbrains.dokka") version("1.9.20")
    id("org.graalvm.buildtools.native") version("0.10.1") apply(false)
    id("io.gitlab.arturbosch.detekt") version("1.23.6") apply(false)
}

ext.set("gradleScripts", "https://raw.githubusercontent.com/hexagontk/hexagon/$version/gradle")

defaultTasks("build")

repositories {
    mavenCentral()
}

task("setUp") {
    group = "build setup"
    description = "Set up project for development. Creates the Git pre push hook (run build task)."

    doLast {
        exec { commandLine("docker version".split(" ")) }

        val dotfiles = "https://raw.githubusercontent.com/hexagontk/.github/master"
        exec { commandLine("curl $dotfiles/.gitignore -o .gitignore".split(" ")) }
        exec { commandLine("curl $dotfiles/commit_template.txt -o .git/message".split(" ")) }
        exec { commandLine("curl $dotfiles/.editorconfig -o .editorconfig".split(" ")) }
        exec { commandLine("git config commit.template .git/message".split(" ")) }

        val prePush = file(".git/hooks/pre-push")
        prePush.writeText("""
            #!/usr/bin/env sh
            set -e
            ./gradlew clean build
        """.trimIndent() + "\n")
        prePush.setExecutable(true)
    }
}

task("release") {
    group = "publishing"
    description = "Tag the source code with the version number after publishing all artifacts."
    dependsOn(project.getTasksByName("publish", true))

    doLast {
        val release = version.toString()
        val actor = System.getenv("GITHUB_ACTOR")

        project.exec { commandLine = listOf("git", "config", "--global", "user.name", actor) }
        project.exec { commandLine = listOf("git", "tag", "-m", "Release $release", release) }
        project.exec { commandLine = listOf("git", "push", "--tags") }
    }
}

tasks.wrapper {
    gradleVersion = "8.7"
    distributionType = ALL
}
