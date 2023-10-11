
# Module settings
Combine `args` and `serialization` to provide a full package to support applications configuration.

It allows to load a parameter from command line, environment variable or a set of files with a
priority:

1. command line
2. environment variable
3. java property (only for testing)
4. $HOME/.config/app/*.{json,yaml}
5. $HOME/.config/app/*.{json,yaml}
