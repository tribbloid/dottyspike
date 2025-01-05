package com.tribbloids.spike.dotty

object ConversionToSameClass {

  case class SameClass[T](
      vs: Seq[T]
  ) {

    def ++(that: SameClass[T]): SameClass[T] = {
      SameClass(vs ++ that.vs)
    }
  }

  given upcast[T]: Conversion[SameClass[? <: T], SameClass[T]] = { cc =>
    cc.copy[T]()
  }

  trait T1
  trait T2 extends T1

  val c1 = SameClass[T2](Seq(new T2 {}))
  val c2 = SameClass[T1](Seq(new T1 {}))

  c1 ++ c2
  c2 ++ c1
  // both works
}
