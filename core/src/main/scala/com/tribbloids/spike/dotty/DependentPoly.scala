package com.tribbloids.spike.dotty

object DependentPoly {

  def fn(x: Int): x.type = x

  val xRaw = fn;

  val xEta: (x: Int) => x.type = identity

  sealed trait Col[V] {

    trait Wrapper
    val wrapper: Wrapper = ???
  }
  val cc = new Col[Int] {}

  object Col1 extends Col[Int]

  object Col2 extends Col[Double]

  val polyFn = [C <: Col[?]] => (x: C) => x.wrapper // doesn't work if type annotated

  val z = [C <: Int] => (x: C) => [D <: Int] => (y: D) => x * y
}
