package com.tribbloids.spike.dotty

import scala.language.implicitConversions

object UnificationOnConversionFromFn {

  trait :=>[I, R]

  implicit def fromVanilla[I, R](
      vanilla: I => R
  ): I :=> R = ??? // fail

  implicit def fromVanilla2[I, R, F <: I => R](
      vanilla: F
  ): I :=> R = ??? // fail

  implicit def fromVanilla3[I, R, F[A, B] <: A => B](
      vanilla: F[I, R]
  ): I :=> R = ??? // fail

  implicit def fromVanilla4[F[A, B] <: A => B, I, R](
      vanilla: F[I, R]
  ): I :=> R = ??? // fail

  lazy val _fn0: Int :=> Int = {

    val a: Int :=> Int = fromVanilla { v =>
      v + 1
    }

//    val b: Int :=> Int = { v =>
//      v + 1
//    }
//
//    b
    ???
  }
}
