package com.tribbloids.spike.dotty

object TraitConstructor {

  trait A(delegate: Any = this) // this won't work in Scala 2.13

  object A1 extends A(A1)
}
