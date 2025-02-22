package com.tribbloids.spike.dotty.kyo

import scala.language.implicitConversions

object Pending {

  //  trait Must[-S]

  // DO NOT CHANGE! will be delegated to kyo in Scala 3
  type <[+T, -S] >: T // | Must[S]
  // S is contravariant because Pending can be added implicitly

  

  sealed trait Add[C] {

    def apply[V](v: V): V < C = v.asInstanceOf[V < C]
  }

  sealed trait Revoke[C] {

    def apply[V](v: V < C): V = v.asInstanceOf[V]
  }

  case class Annotator[C]() {

    object add extends Add[C]

    // TODO: sometimes left associated function won't work (generic collapse to Nothing), need to file a bug report for it
    def <<: : add.type = add

    object revoke extends Revoke[C]

    def <<--: : revoke.type = revoke
  }

  trait OrNull

  object OrNull extends Annotator[OrNull] {

    case class Ops[T](self: T < OrNull) {

      def asOption: Option[T] = self match {
        case null => None
        case v => Some(v.asInstanceOf[T])
      }
    }

    implicit def ops[T <: AnyRef](self: T < OrNull): Ops[T] = Ops(self)
  }

  {
    // sanity

    val v: String < OrNull = ???

    v.asOption
  }
}
