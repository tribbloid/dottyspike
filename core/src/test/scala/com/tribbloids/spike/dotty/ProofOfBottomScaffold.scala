package com.tribbloids.spike.dotty

object ProofOfBottomScaffold {

  sealed trait TupleThing {

    type Peer >: this.type <: TupleThing

    def peer: Peer = this

    type Bottom <: Peer

    def proofOfBottom[TSub <: Peer](v: TSub): Bottom <:< TSub
  }

  case object Eye extends TupleThing {

    override type Peer = Eye.type

    override type Bottom = Eye.type

    override def proofOfBottom[TSub <: Peer](v: TSub): Bottom <:< TSub =
      v match {
        case Eye => ???
      }
  }

  type ><:[+H, +T <: TupleThing] = Cons[? <: H, ? <: T]

  final case class Cons[H, T <: TupleThing](tail: T) extends TupleThing {

    override type Peer = ><:[H, T]

    override type Bottom = ><:[Nothing, tail.Bottom] & Peer

    override def proofOfBottom[TSub <: Peer](v: TSub): Bottom <:< TSub = {
      (tail: TupleThing) match {
        case Eye =>
          ()
        case cons: Cons[_, _] =>
          cons.proofOfBottom(cons.peer)
      }

      ???
    }

  }
}
