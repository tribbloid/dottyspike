package com.tribbloids.spike.dotty.quoted.autolift.spike

import com.tribbloids.spike.dotty.quoted.autolift.spike.TASTyInspectionSpike.MyInspector
import org.scalatest.funspec.AnyFunSpec

import scala.quoted.*
import scala.tasty.inspector.*

class TASTyInspectionSpike extends AnyFunSpec {

  ignore("on file") {

    val tastyFiles = List("foo/Bar.tasty")
    TastyInspector.inspectTastyFiles(tastyFiles)(new MyInspector)

  }
}

object TASTyInspectionSpike {

  class MyInspector extends Inspector:

    def inspect(
        using
        Quotes
    )(tastys: List[Tasty[quotes.type]]): Unit = {
      for { tasty <- tastys } {
        val tree = tasty.ast
        // Do something with the tree
      }
    }

}
