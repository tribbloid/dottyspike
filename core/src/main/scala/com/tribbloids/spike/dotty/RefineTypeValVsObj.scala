package com.tribbloids.spike.dotty

object RefineTypeValVsObj {

  trait Gen {

    type _T

  }

  object T1 {
    trait Proto {
      val gen: Gen
    }

  }

  type T1[+T] = T1.Proto { val gen: Gen { type _T <: T } }

  trait Example extends T1.Proto {

    object gen extends Gen {

      type _T = Int
    }
  }

  summon[Example <:< T1[Int]]
}
