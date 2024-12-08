package com.tribbloids.spike.dotty

import com.tribbloids.spike.dotty.CompileTimeIsObject.{isObject, MyClass, MyObject}
import org.scalatest.funspec.AnyFunSpec

class CompileTimeIsObjectSpec extends AnyFunSpec {

  it("sanity") {

    val results = List(
      isObject[MyObject.type], // true
      isObject[MyClass], // false
      isObject[Int], // false
      isObject[String] // false
    )

    assert(results == List(true, false, false, false))

//    def test(): Unit =
  }
}

object CompileTimeIsObjectSpec {}
