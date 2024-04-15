package com.tribbloids.spike.dotty.quoted.autolift

import scala.quoted.staging

object Fixture {

  lazy val testCompiler: staging.Compiler = staging.Compiler.make(getClass.getClassLoader)

}
