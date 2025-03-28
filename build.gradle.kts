buildscript {
    repositories {
        // Add here whatever repositories you're already using
        mavenCentral()
    }

    dependencies {
        classpath("ch.epfl.scala:gradle-bloop_2.13:1.6.2")
    }
}

plugins {
    id("ai.acyclic.scala3-conventions")
    id("ai.acyclic.publish-conventions")
}

val vs = versions()

allprojects {

    dependencies {

        val catsV = "2.12.0"

        api("org.typelevel:cats-laws_3:${catsV}")
        api("org.typelevel:cats-free_3:${catsV}")
        api("org.typelevel:cats-effect_3:3.5.2")

        api("io.suzaku:boopickle_3:1.5.0")

        // https://mvnrepository.com/artifact/org.typelevel/shapeless3-deriving
        api("org.typelevel:shapeless3-deriving_3:3.4.3")

        // https://mvnrepository.com/artifact/eu.timepit/refined
        api("eu.timepit:refined_3:0.11.2")

        api("dev.zio:izumi-reflect_3:2.3.10")

        api("org.scala-lang:scala3-compiler_3:${vs.scala.v}")

        api("org.scala-lang:scala3-tasty-inspector_3:${vs.scala.v}")

        implementation("ch.epfl.scala:tasty-query_3:1.4.0")

        // https://mvnrepository.com/artifact/com.lihaoyi/pprint
        api("com.lihaoyi:pprint_3:0.9.0")

    }

    tasks {

        withType<ScalaCompile> {

            scalaCompileOptions.apply {

                additionalParameters.addAll(
                    listOf(
//                        "-experimental",
                        "-language:experimental.dependent"
                    )
                )
            }
        }
    }
}


idea {

    module {
        excludeDirs.addAll(
            listOf(
                file("doc"),
                file("six-scala")
            )
        )
    }
}