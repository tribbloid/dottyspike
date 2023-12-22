package com.tribbloids.spike.dotty

object CapabilityTracking {

  object ByDirectMixin {

    trait Cap
    trait Cap1 extends Cap {

      def canDo(): Unit = {
        println("canDo")
      }
    }
    trait Cap2 extends Cap

    class HasNoCap

//    class HasCap1 extends HasNoCap with Cap1

    val a = new HasNoCap
    val b = a.asInstanceOf[HasNoCap & Cap1]

    b.canDo()
  }

  def main(args: Array[String]): Unit = {
    ByDirectMixin
  }
}
