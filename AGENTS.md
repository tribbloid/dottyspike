# DottySpike - Scala 3 Experiments

## Build & Test Commands
- **Build all**: `./gradlew build`
- **Test all**: `./gradlew test`
- **Test single subproject**: `./gradlew :core:test` or `./gradlew :spark:test` or `./gradlew :six:typetag:test`
- **Test single class**: `./gradlew :core:test --tests "com.tribbloids.spike.dotty.ForComprehensionSpec"`
- **Format code**: `./gradlew scalafmtAll`
- **Scalafix**: `./gradlew scalafix`

## Architecture
- **Subprojects**: `:core`, `:spark`, `:zio-schema`, `:six:typetag`, `:six:spark`
- **Main packages**: `com.tribbloids.spike.dotty.*` (experiments), `ai.acyclic.six.*` (utilities/libraries)
- **Scala version**: Scala 3 with experimental features (`-language:experimental.dependent`)
- **Build system**: Gradle with Kotlin DSL

## Code Style
- **Format**: Scalafmt v3.8.5 (Scala 3 dialect), max column 120, sorted imports
- **Tests**: ScalaTest with `AnyFunSpec`, using JUnit Platform
- **Package naming**: `com.tribbloids.spike.*` for experiments, `ai.acyclic.*` for libraries
- **Import style**: Standard Scala 3 imports, use `scala.quoted.*` for macros
- **No code comments**: Keep code clean without explanatory comments unless complex
