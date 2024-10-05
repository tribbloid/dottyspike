package com.tribbloids.spike.dotty

object TypeSystemRosetta {

  trait Expressions {

    val value = 1 // H-M DOT
    val term = (value: Int) + 2 // H-M DOT

    val lambda = { (x: Int) => x * term } // H-M DOT
    val application = lambda(term) // H-M DOT

    val selection = (??? : (Boolean, Int))._2 // H-M DOT

    def context(): Unit = { val a1: Int = 1 } // H-M DOT
  }

  trait Types {

    type MONO1 = Int // H-M DOT
    type MONO2 = (String, Int) // H-M DOT

    trait Fields { val _1: String; val _2: Int } // DOT

    type INTERSECTION = String with Product with Serializable // DOT

    type POLY1 = [X] =>> (X => X) => X // H-M DOT
    type POLY2 = (x: (Boolean, Int)) => x._2.type // H-M DOT

    type BOUND >: Tuple <: Product // DOT

    type UNION = Int | String // --

    type RECURSIVE = Tuple { val _1: String } // DOT

    type TOP = Any // DOT
    type BOTTOM = Nothing // DOT
  }

  trait FreeTypeVariables {

    import FreeTypeVariables.*

    type T1 = F1
    type T2 = F1 with Product

    type T3 = (x: F3) => x._2.type
    type T4 = (x: Tau) => x.a1.type
  }

  object FreeTypeVariables {

    type F1 = String
    val f2 = 1
    type F3 = (String, Int)

    trait Tau {
      val a1: Int
    }
  }
}
