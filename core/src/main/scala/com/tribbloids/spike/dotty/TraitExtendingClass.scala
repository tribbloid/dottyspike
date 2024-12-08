package com.tribbloids.spike.dotty

object TraitExtendingClass {

  abstract class A(
      implicit
      val x: Int = 3
  )

  trait B extends A

  def main(): Unit = {

    val b = new B {}

    object C extends B

    println(b.x)
  }
}
