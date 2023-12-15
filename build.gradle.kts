buildscript {
    repositories {
        // Add here whatever repositories you're already using
        mavenCentral()
    }
}

plugins {
    id("ai.acyclic.scala3-conventions")
    id("ai.acyclic.publish-conventions")
}

allprojects {

    dependencies {

        val catsV = "2.10.0"

        api("org.typelevel:cats-laws_3:${catsV}")
        api("org.typelevel:cats-free_3:${catsV}")
        api("org.typelevel:cats-effect_3:3.5.2")

        // https://mvnrepository.com/artifact/org.typelevel/shapeless3-deriving
        api("org.typelevel:shapeless3-deriving_3:3.3.0")

        // https://mvnrepository.com/artifact/eu.timepit/refined
        api("eu.timepit:refined_3:0.11.0")

        api("dev.zio:izumi-reflect_3:2.3.8")
    }

    tasks {

        withType<ScalaCompile> {

            scalaCompileOptions.apply {

                additionalParameters.addAll(
                    listOf(
                        "-language:experimental.dependent"
                    )
                )
            }
        }
    }
}


idea {

    module {
        excludeDirs.add(file("doc"))
    }
}