package com.tribbloids.spike.dotty

import java.util.Date

object MixinWithSingleAbstractMethod {

  trait T1 {

    def aa(x: Int): String
  }

  type T2 = T1 & Any
  type T3 = T1 & Serializable

  summon[T1 =:= T2]
  summon[T1 <:< T2]
  summon[T2 <:< T1]

  val _: T1 = { v => v.toString }
  val _: T2 = { v => v.toString }
  //  val _: T3 = { v => v.toString } // won't work

  //  val _: T3 = new T3 {
  //    override def aa(x: Int): String = x.toString
  //  }

  object ForConstructor {

    trait T[+T] {

      def aa(x: Int): String
    }

    type T1 = T[Any]
    type T2 = T[Serializable]
    type T3 = T1 & T2

    val _: T1 = { v => v.toString }
    val _: T2 = { v => v.toString }
    val _: T3 = { v => v.toString }
    // all works
  }
}
