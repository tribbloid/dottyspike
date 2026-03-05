package com.tribbloids.spike.dotty

import scala.util.NotGiven

object ProofOfBottomExample {

//  trait Coe[-I, +O] {
//    def apply(v: I): O
//  }

  type Coercion[-I, +O] = I <:< O

  sealed trait TupleThing {

    type Peer <: TupleThing

    type Bottom <: Peer

    def proofOfBottom[TSub >: Inhabited <: Peer]: Coercion[Bottom, TSub]
  }

  type Inhabited = Eye.type & (? ><: ?)

  // IMPORTANT: DO NOT CHANGE ANYTHING ABOVE

  case object Eye extends TupleThing {

    override type Peer = Eye.type

    override type Bottom = Inhabited

    def proofOfBottom[TSub >: Inhabited <: Peer]: Coercion[Bottom, TSub] = {
      summon[Bottom <:< TSub]
    }
  }
  type Eye = Eye.type

  sealed trait ><:[+H, +T <: TupleThing] extends TupleThing {
    val head: H
    val tail: T
  }

  type KK = Cons[Int, TupleThing]

  final case class Cons[H, T <: TupleThing](head: H, tail: T) extends (H ><: T) {

    override type Peer = H ><: tail.Peer

    override type Bottom = (Nothing ><: (tail.Bottom & T))

    override def proofOfBottom[TSub >: Inhabited <: Peer]: Coercion[Bottom, TSub] = {
      throw new IllegalStateException("unreachable: no lawful Bottom <:< TSub can be derived from these bounds")
    }

//    def proofWithCompiler2[TSub <: Peer] = {
//      summon[Bottom <:< TSub]
//    }
  }

  val c1 = Cons(1, Cons("a", Eye))
  type T1 = c1.Bottom

  summon[c1.tail.Bottom <:< c1.tail.Peer] // success
  summon[c1.Bottom <:< c1.Peer] // success

  // Concrete counterexample: Inhabited is not provably below this Cons peer.
  val ce = Cons(1, Eye)
  type CePeer = ce.Peer
  summon[NotGiven[Inhabited <:< CePeer]]

  // This direction holds, but not full equality.
//  summon[T1 <:< (Nothing ><: Nothing ><: Eye)]

}
