package com.tribbloids.spike.dotty

object HigherKindBound {

  trait T1 {
    type H1[_ <: Nothing]
  }

  trait T2 extends T1 {
    override type H1[T <: String] = List[T]
  }
}
