//val versions = gradle.rootProject.versions()

include("six")
project(":six").projectDir = file("six-scala/module")

include(
    ":six:typetag",
    ":six:spark",
    ":core",
    ":spark"
)

pluginManagement.repositories {
    gradlePluginPortal()
    mavenCentral()
    // maven("https://dl.bintray.com/kotlin/kotlin-dev")
}
