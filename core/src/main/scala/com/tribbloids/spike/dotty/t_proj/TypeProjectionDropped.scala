package com.tribbloids.spike.dotty.t_proj

import ai.acyclic.six.testing.Assert

object TypeProjectionDropped {

  /**
    * in short, type projection only works on type or kinds with concrete path, in which case the compiler can cast it
    * into a match type.
    *
    * the compiler should be smart enough to do it for sealed traits, have to wait
    */

  trait I

  sealed trait E1 {

    type Key

    type Key2 <: I
  }

  object E1 {

    type K[TKey] = E1 { type Key = TKey }
  }

  final class E2[T] {

    type Key = T
    type Key2 <: T
  }

  Assert.typeError("type R[T <: E1] = Map[T#Key, T]")
  { type R = Map[E1#Key, E1] }
  { type R = Map[E1#Key2, E1] }

  {
    def __sanity[T <: E1](): Unit = {

      Assert.typeError("type R = Map[T#Key, Int]")

      type R = T match {
        case E1.K[k] => Map[k, Int]
      } // this is an illusion, see next section
    }

    case class Of[T <: E1]() {

      type R = T match {
        case E1.K[k] => Map[k, Int]
      }

      def take(v: R): Unit = {}
    }

    object E1X extends E1 {
      type Key = Int
      type Key2 = I
    }

    val of = Of[E1X.type]()
//    of.take(2: of.R)
    Assert typeError "of.take(2)"
  }

  { type R = Map[E2[Int]#Key, Int] } // works
  { type R = Map[E2[Int]#Key2, Int] } // works

  {
    def __sanity[T <: E2[?]](): Unit = {

      Assert.typeError("type R = Map[T#Key, Int]")

      type R = T match {
        case E2[k] => Map[E2[k]#Key, Int]
      }
    }
  }

  {
    def __sanity[K](): Unit = {

      type R = Map[E2[K]#Key, Int] // works, despite equivalent
    }
  }
}
