package com.tribbloids.spike.dotty

object MatchTypeOverriding {

  trait T1 {

    type K[_]
  }

  trait T2 {

    type K[T <: Seq[?]]
  }

  trait T3 {

    type K[T <: String]
  }

  {
    trait R extends T1 {

      type K[T] = T match {
        case Seq[i] => i
      }
    }
  }

  {
    trait R extends T2 {

      type K[T] = T match {
        case Seq[i] => i
      }
    }
  }

  {
    trait R extends T3 {

      type K[T] = T match {
        case Seq[i] => i
      }
    }

//    trait R2 extends T3 {
//
//      type K[T <: Seq[?]] = T
//    }
  }
}
