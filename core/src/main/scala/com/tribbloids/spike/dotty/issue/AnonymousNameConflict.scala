package com.tribbloids.spike.dotty.issue

object AnonymousNameConflict {

  // Define the type class
  trait CaseName[A] {}

  object CaseName {
    trait X1[T]
    trait X2[T]

    // For non-enum sum types
    given x1[A](
        using
        X1[A]
    ): CaseName[A] with {}

    // For any case type in non-enum sum types
//    given [A](
//        using
//        X2[A]
//    ): CaseName[A] with {}
  }
}
