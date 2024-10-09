package com.tribbloids.spike.dotty

import org.scalatest.funspec.AnyFunSpec

class SrcTracingSpec extends AnyFunSpec {

  it("simple") {

    val tt = SrcTracing.trace { (i: Int) =>
      i + 1
    }

    println(tt.src)
  }
}
