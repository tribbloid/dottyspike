package com.tribbloids.spike.dotty.quoted.autolift

import scala.quoted.staging.Compiler

object Fixture {

  given Compiler.Settings = Compiler.Settings.make(compilerArgs = List("-experimental"))

  lazy val testCompiler: Compiler = Compiler.make(getClass.getClassLoader)

}
