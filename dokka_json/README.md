
# Module dokka_json
Dokka plugin that outputs documentation to a JSON file. The plugin can be added as a dependency to
any Dokka renderer, and it will create the `module_{name}.json` file along the rendered
documentation. You can add it to your build with the following code:

* Gradle Kotlin
```kotlin
tasks.named<DokkaTaskPartial>("dokkaHtmlPartial") {
  dependencies {
    plugins("com.hexagonkt.extra:dokka_json:$version")
  }
}
```

* Gradle Groovy
```groovy
dokkaHtmlPartial {
  dependencies {
    plugins("com.hexagonkt.extra:dokka_json:$version")
  }
}

```

* Maven
```xml
<plugin>
  <groupId>org.jetbrains.dokka</groupId>
  <artifactId>dokka-maven-plugin</artifactId>
  <configuration>
    <dokkaPlugins>
      <plugin>
        <groupId>com.hexagonkt.extra</groupId>
        <artifactId>dokka_json</artifactId>
        <version>${version}</version>
      </plugin>
    </dokkaPlugins>
  </configuration>
</plugin>
```

# Package com.hexagonkt.dokka.json
Package with the plugin code.
