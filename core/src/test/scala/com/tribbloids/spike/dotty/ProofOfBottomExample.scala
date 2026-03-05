package com.tribbloids.spike.dotty

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
      ???
    }
  }

  sealed trait ><:[+H, +T <: TupleThing] extends TupleThing {
    val head: H
    val tail: T
  }

  type KK = Cons[Int, TupleThing]

  final case class Cons[H, T <: TupleThing](head: H, tail: T) extends (H ><: T) {

    override type Peer = H ><: tail.Peer

    override type Bottom = (Nothing ><: (tail.Bottom & T))

//    def proofWithCompiler2[TSub <: Peer] = {
//      summon[Bottom <:< TSub]
//    }
  }
}
