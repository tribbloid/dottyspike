package com.tribbloids.spike.dotty.subtyping

import ai.acyclic.six.scaffold.BaseSpec

import scala.reflect.ClassTag

class ExtendInnerTraitSpike extends BaseSpec {

  import ExtendInnerTraitSpike.*

  it("class") {

    val s = Seq(
      _1.B.tagT,
      _2.B.tagT,
      C.tagT
    )

    s.foreach(println)
  }
}

object ExtendInnerTraitSpike {

  trait A {

    trait X
  }

  object A1 extends A
  object A2 extends A

  trait Tagged[T](
      implicit
      val tagT: ClassTag[T]
  )

  {
    trait B extends Tagged[Product] {}
    object B extends B
  }

  object _1 {
    trait B extends Tagged[A1.X & A2.X] {}
    object B extends B
  }

  object _2 {
    trait B extends Tagged[A1.X | A2.X] {}
    object B extends B
  }

  trait C extends Tagged[p1.X & p2.X] {}
  object C extends C

}
