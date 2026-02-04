//val versions = gradle.rootProject.versions()

include("six")
project(":six").projectDir = file("six-scala/module")

include(
    ":six:typetag",
    ":six:spark",
    ":six:congruence",
    ":core",
    ":spark",
    ":zio-schema",
    ":kyo",
    ":turbolift"
)

pluginManagement.repositories {
    gradlePluginPortal()
    mavenCentral()
    // maven("https://dl.bintray.com/kotlin/kotlin-dev")
}
