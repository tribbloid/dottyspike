val vs = versions()

buildscript {
    repositories {
        // Add here whatever repositories you're already using
        mavenCentral()
    }

//    dependencies {
//        classpath("ch.epfl.scala:gradle-bloop_2.12:1.5.3") // suffix is always 2.12, weird
//    }
}

plugins {
    java
    `java-library`
    `java-test-fixtures`

    scala

    idea

    id("com.github.ben-manes.versions") version "0.44.0"
}

allprojects {

    apply(plugin = "java")
    apply(plugin = "java-library")
    apply(plugin = "java-test-fixtures")

    apply(plugin = "scala")
    apply(plugin = "idea")

    group = "com.tribbloids.dottyspike"
    version = vs.projectV

    repositories {
        mavenLocal()
        mavenCentral()
//        jcenter()
        maven("https://dl.bintray.com/kotlin/kotlin-dev")
//        maven("https://scala-ci.typesafe.com/artifactory/scala-integration/") // scala SNAPSHOT
    }

    task("dependencyTree") {

        dependsOn("dependencies")
    }

    tasks {
//        val jvmTarget = JavaVersion.VERSION_1_8.toString()

        withType<ScalaCompile> {

//            targetCompatibility = jvmTarget

            scalaCompileOptions.apply {

                loggingLevel = "verbose"

                val compilerOptions =

                    mutableListOf(
                        "-encoding", "UTF-8",

                        "-verbose", "-explain",
                    
                        "-language:experimental.dependent"
                    )

                additionalParameters = compilerOptions

                forkOptions.apply {

                    memoryInitialSize = "1g"
                    memoryMaximumSize = "4g"

                    // this may be over the top but the test code in macro & core frequently run implicit search on church encoded Nat type
                    jvmArgs = listOf(
                        "-Xss256m"
                    )
                }
            }
        }

        test {

            minHeapSize = "1024m"
            maxHeapSize = "4096m"

            testLogging {
                showExceptions = true
                showCauses = true
                showStackTraces = true

                // stdout is used for occasional manual verification
                showStandardStreams = true
            }

            useJUnitPlatform {
                includeEngines("scalatest")
                testLogging {
                    events("passed", "skipped", "failed")
                }
            }
        }
    }

    java {
        withSourcesJar()
        withJavadocJar()
    }
}

allprojects {

    dependencies {

        // see https://github.com/gradle/gradle/issues/13067
        fun bothImpl(constraintNotation: Any) {
            implementation(constraintNotation)
            testFixturesImplementation(constraintNotation)
        }

//        fun bothApi(constraintNotation: Any) {
//
//            api(constraintNotation)
//            testFixturesApi(constraintNotation)
//        }

        constraints {}

        api("org.scala-lang:scala3-compiler_3:${vs.scalaV}")
        api("org.scala-lang:scala3-library_3:${vs.scalaV}")
        api("org.scala-lang:scala3-staging_3:${vs.scalaV}")

        testImplementation("org.typelevel:cats-laws_3:2.9.0")

        // https://mvnrepository.com/artifact/org.typelevel/shapeless3-deriving
        api("org.typelevel:shapeless3-deriving_3:3.3.0")

        // https://mvnrepository.com/artifact/eu.timepit/refined
        implementation("eu.timepit:refined_3:0.11.0")


        api("org.typelevel:cats-free_3:2.9.0")
        api("org.typelevel:cats-effect_3:3.4.5")

        api("dev.zio:izumi-reflect_3:2.3.8")

//      "dev.zio" %% "izumi-reflect" % "2.0.0" withSources () withJavadoc (),

        testImplementation("org.scalatest:scalatest_3:3.2.15")

        testImplementation("com.novocode:junit-interface:0.11")

//        val scalaTestV = "3.2.11"
//        testFixturesApi("org.scalatest:scalatest_${vs.scalaBinaryV}:${scalaTestV}")
//        testImplementation("org.junit.jupiter:junit-jupiter:5.9.0")
//
//        testRuntimeOnly("co.helmethair:scalatest-junit-runner:0.1.11")
    }
}

idea {

    targetVersion = "2020"

    module {

        excludeDirs = excludeDirs + listOf(
            file(".gradle"),
//            file(".github"),

            file("target"),
            file("bin"),
            file ("out"),

            file(".idea"),
            file(".vscode"),
            file(".bloop"),
            file(".bsp"),
            file(".metals"),
            file(".ammonite"),

            file("logs"),

            file("spike"),
        )

        isDownloadJavadoc = true
        isDownloadSources = true
    }
}
