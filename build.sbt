import sbt.Keys.libraryDependencies

val dottyVersion = "3.0.0-M1"

lazy val root = project
  .in(file("."))
  .settings(
    name := "dottyspike",
    version := "0.1.0",

    scalaVersion := dottyVersion,

    libraryDependencies ++= Seq(
      "org.scala-lang" %% "scala3-staging" % dottyVersion,

//      "org.scala-lang" %% "scala-library" % dottyVersion,

//      "org.scala-lang.modules" %% "scala-parallel-collections" % "0.2.0",
      "com.novocode" % "junit-interface" % "0.11" % "test",

      "org.scala-lang.modules" % "scala-parallel-collections_2.13" % "0.2.0"
    )


  )
