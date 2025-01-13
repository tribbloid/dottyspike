package com.tribbloids.spike.dotty

object TypeArgMixin {

  trait Base[+A, +B] {

    val a: A
    val b: B
  }

  trait First extends Base[Product, Int]

  type Second[T] = Base[T, ?]

  trait Mixed extends First with Second[Tuple1[Int]]

  val fn = { (mixed: Mixed) =>
    mixed.a: Tuple1[Int]
  }

  trait Mixed2 extends First with Base[Tuple1[Int], ?] // this doesn't work in Scala 2

  val fn2 = { (mixed: Mixed2) =>
    mixed.b
  }
}
