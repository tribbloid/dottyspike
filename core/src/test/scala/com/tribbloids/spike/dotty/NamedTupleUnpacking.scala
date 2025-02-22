package com.tribbloids.spike.dotty

import org.scalatest.funspec.AnyFunSpec
import scala.compiletime.constValueTuple

import scala.NamedTuple.NamedTuple

object NamedTupleUnpacking {

  inline def fn[N <: Tuple, V <: Tuple](tt: NamedTuple[N, V]): Unit = {
    println(constValueTuple[N])
    println(tt)
  }
}

class NamedTupleUnpacking extends AnyFunSpec {

  import NamedTupleUnpacking.*

  it("1") {

    fn(x = 1, y = 2, aa = 3)
  }
}
