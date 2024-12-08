package com.tribbloids.spike.dotty

object EtaExpandedType {

  trait A {

    val fn: Int => String
  }

  object B extends A {

    override val fn = ???

//    def fn(v: Int): String = v.toString
    // doens't work:
    // object creation impossible, since val fn: Int => String in trait A in object EtaExpandedType is not defined
  }
}
