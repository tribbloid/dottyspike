package com.tribbloids.spike.dotty

object ContravariantFn {

  def random(): Double = ??? // unknown

  object Fn {

    trait Raw {

      type _I
      type _O[T]

      def apply(v: _I): _O[v.type]
    }

    type K[-I, +O] = Raw { type _I >: I; type _O[_] <: O }

    trait K_[I, O] extends Raw {

      type _I = I
      type _O[T] = O
    }

    type ID[-I] = Raw { type _I >: I; type _O[T] <: T }
    type Filter[-I] = Raw { type _I >: I; type _O[T] <: Option[T] }

    implicitly[Filter[String] <:< K[String, Option[?]]]
  }

  type Drop1[T, R] = R

  // TODO: doesn't work, need a compiler patch
//  trait FilterHalf[-I] extends Fn.Filter[I] {
//
//    type _O[T] = Option[T]
//    def apply(t: I): _O[t.type] = {
//
//      if (random() < 0) Some(t)
//      else None
//    }
//  }
}
