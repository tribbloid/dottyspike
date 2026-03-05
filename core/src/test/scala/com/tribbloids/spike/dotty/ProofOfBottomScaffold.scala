package com.tribbloids.spike.dotty

object ProofOfBottomScaffold {

  sealed trait TupleThing {

    type Peer >: this.type <: TupleThing

//    def proofOfPeer: this.type <:< Peer

    def peer: Peer = this

    type Bottom <: Peer

    def proofOfBottom[TSub <: Peer](v: TSub): Bottom <:< TSub
  }

  case object Eye extends TupleThing {

    override type Peer = Eye.type

//    override lazy val proofOfPeer: <:<[Eye.this.type, ProofOfBottomScaffold.Eye.type] = summon

    override type Bottom = Eye.type

    override def proofOfBottom[TSub <: Peer](v: TSub): Bottom <:< TSub =
      v match {
        case v0 @ Eye =>
          summon[Bottom <:< v0.type].andThen(summon[v0.type <:< TSub])
      }

  }

  type ><:[+H, +T <: TupleThing] = Cons[? <: H, ?] { type Tail <: T }

  // IMPORTANT: DO NOT CHANGE ANYTHING ABOVE

  type KK = Cons[Int, TupleThing]

  final case class Cons[H, T <: TupleThing](tail: T) extends TupleThing {

    type Tail = tail.type

    override type Peer = H ><: tail.Peer

//    override def proofOfPeer: <:<[Cons.this.type, H ><: Cons.this.tail.Peer] = summon

//    override lazy val proofOfPeer: <:<[Cons.this.type, H ><: Cons.this.tail.Peer] = {
//      val ev: tail.type <:< tail.Peer = tail.proofOfPeer
//
//      H ><:
//    }

    override type Bottom = Nothing ><: tail.Bottom

    override def proofOfBottom[TSub <: Peer](v: TSub): Bottom <:< TSub = {

      val head: Nothing <:< H = summon[Nothing <:< H]
      val recursive1: tail.Bottom <:< tail.Peer = tail.proofOfBottom(tail.peer)

      val widenTail: ><:[Nothing, tail.Bottom] <:< ><:[Nothing, tail.Peer] = {
        type Lift[+X] = ><:[Nothing, tail.Bottom] <:< ><:[Nothing, X & TupleThing]
        recursive1.substituteCo[Lift](summon[><:[Nothing, tail.Bottom] <:< ><:[Nothing, tail.Bottom & TupleThing]])
      }

      val widenHead: ><:[Nothing, tail.Peer] <:< ><:[H, tail.Peer] = {
        type Lift[+X] = ><:[Nothing, tail.Peer] <:< ><:[X, tail.Peer]
        head.substituteCo[Lift](summon[><:[Nothing, tail.Peer] <:< ><:[Nothing, tail.Peer]])
      }

      // TODO: finish this proof!
      ???
    }

  }
}
