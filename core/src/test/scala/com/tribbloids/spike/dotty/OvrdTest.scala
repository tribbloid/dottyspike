package com.tribbloids.spike.dotty

import org.tribbloid.scaffold.BaseSpec

class OvrdTest extends BaseSpec {

  import Ovrd.*

  it("should k") {

    val ff = Ovrd.f

    println(ff)

    val vv = ff.apply(new Node[Nothing] {
      override def value: Nothing = ???
    })

    println(vv)
  }

}
