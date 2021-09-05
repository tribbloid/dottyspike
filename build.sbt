//import sbt.Keys.libraryDependencies

val scala3Version = "3.0.1"

lazy val root = project
  .in(file("."))
  .settings(
    name := "dottyspike",
    version := "0.1.0",
    scalaVersion := scala3Version,
//    libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % "test"
    libraryDependencies ++= Seq(
      "org.scala-lang" %% "scala3-staging" % scala3Version withSources () withJavadoc (),
      // https://mvnrepository.com/artifact/dev.zio/izumi-reflect
      "dev.zio" %% "izumi-reflect" % "2.0.0" withSources () withJavadoc (),
      // tests
      "com.novocode" % "junit-interface" % "0.11" % "test"
//      "org.scala-lang.modules" % "scala-parallel-collections_2.13" % "0.2.0"
    )
  )
