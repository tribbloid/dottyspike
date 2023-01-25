package com.tribbloids.spike.dotty

class DependentPoly {

  sealed trait Col[V] {

    trait Wrapper
    val wrapper: Wrapper = ???
  }

  object Col1 extends Col[Int]

  object Col2 extends Col[Double]

  val polyFn = [C <: Col[?]] => (x: C) => x.wrapper // doesn't work if type annotated

  val x: (x: Int) => x.type = ???
}
