# AGENTS.md — Critical instructions — READ THIS FIRST

This file contains information for AI agents working on the prover-commons project.

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

All the following links should be resolved using their absolute paths. Some of them are outside the project root.

## Initial Setup (One-time)

generate and validate local settings, see [this](buildSrc/.agent/init.md)

## Guardrails (violate only if user explicitly says "ignore AGENTS.md")

see [this](buildSrc/.agent/guardrails.md)

## Frequently Used Commands

see [this](buildSrc/.agent/common-commands.md)

## Code Style & Conventions

see [this](buildSrc/.agent/file-organization.md)

## Development Workflow

see [this](buildSrc/.agent/development-workflow.md)
