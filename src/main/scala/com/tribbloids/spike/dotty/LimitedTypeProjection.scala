package com.tribbloids.spike.dotty

object LimitedTypeProjection {

  trait I

  trait Outer {

    type Inner <: I
//    trait Inner {
//
//
//    }
  }

  val v: Outer#Inner = ???
}
