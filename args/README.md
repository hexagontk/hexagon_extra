
# Module args
TODO .

## Command processing
1. Read system.in (check if there is input piped from other command)
2. Parse command line tokens into options and positional parameters
3. Check command line definition for invalid options, etc.
4. Apply defaults: option defaults, defaults in configuration files -user dir, and current dir-
5. Check missing options: fail if no interactive, ask for input if interactive
6. Return result
7. Support - and --
8. Take care of environment variables and .env files in current dir
9. Support interactive settings prompt if mandatory options are missing
10. Support @files for long list of parameters

Program, can have stream input, config files (XDG dirs), and parameters

Check: https://clig.dev/#the-basics

TODO Handle standard options like --help, help command -q --quiet, -v verbose, etc.

TODO Control rendering with flags on `ArgsManager`

## Install the Dependency

=== "build.gradle"

    ```groovy
    repositories {
        mavenCentral()
    }

    implementation("com.hexagonkt.extra:args:$hexagonVersion")
    ```

=== "pom.xml"

    ```xml
    <dependency>
      <groupId>com.hexagonkt.extra</groupId>
      <artifactId>args</artifactId>
      <version>$hexagonVersion</version>
    </dependency>
    ```

# Package com.hexagonkt.args
TODO .
