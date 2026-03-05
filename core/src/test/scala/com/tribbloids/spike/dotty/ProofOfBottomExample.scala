package com.tribbloids.spike.dotty

import scala.util.NotGiven

object ProofOfBottomExample {

//  trait Coe[-I, +O] {
//    def apply(v: I): O
//  }

  type Coercion[-I, +O] = I <:< O

  sealed trait Tuple {

    type Peer <: Tuple

    type Bottom <: Peer

    def proofOfBottom[TSub >: Tuple.Inhabited <: Peer]: Coercion[Bottom, TSub]
  }

  object Tuple {

    type Inhabited = Eye.type & (? ><: ?)

    // IMPORTANT: DO NOT CHANGE ANYTHING ABOVE

    case object Eye extends Tuple {

      override type Peer = Eye.type

      override type Bottom = Inhabited

      def proofOfBottom[TSub >: Inhabited <: Peer]: Coercion[Bottom, TSub] = {
        summon[Bottom <:< TSub]
      }
    }

    type Eye = Eye.type

    sealed trait ><:[+H, +T <: Tuple] extends Tuple {
      val head: H
      val tail: T
    }

    type KK = Cons[Int, Tuple]

    final case class Cons[H, T <: Tuple](head: H, tail: T) extends (H ><: T) {

      override type Peer = H ><: tail.Peer

      override type Bottom = (Nothing ><: (tail.Bottom & T))

      override def proofOfBottom[TSub >: Inhabited <: Peer]: Coercion[Bottom, TSub] = {
        // TODO: write a proof here
        ???
      }
    }
  }

  import Tuple.*

  {
    // positive examples
    val c1 = Cons(1, Cons("a", Eye))
    type T1 = c1.Bottom

    summon[c1.tail.Bottom <:< c1.tail.Peer] // success
    summon[c1.Bottom <:< c1.Peer] // success
  }

  { // TODO: fill in counterexample stub

    type C1 <: Tuple // write a concrete type here
    val c1: C1 = ???
    summon[NotGiven[c1.Bottom <:< c1.Peer]]
  }

}
