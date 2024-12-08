package com.tribbloids.spike.dotty

class DependentTypeOfSuper {

  trait A {

    val x: Any
  }

  trait B extends A {

    // oops, doesn't work
//    override val x: super.x.type with Product = {
//      ???
//    }
  }
}
