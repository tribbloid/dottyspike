package com.tribbloids.spike.dotty.capture

object TraitCast {

  def main(args: Array[String]): Unit = {
    ByDirectMixin
  }

  object ByDirectMixin {

    val a = new HasNone
    val b = a.asInstanceOf[HasNone & T01] // TODO: runtime error

    trait T0

    trait T01 extends T0 {

      def canDo(): Unit = {
        println("canDo")
      }
    }

//    class HasCap1 extends HasNoCap with Cap1

    trait T02 extends T0

    class HasNone

    b.canDo()

    type A = (Int, String)
    type B = Product *: A
  }
}
