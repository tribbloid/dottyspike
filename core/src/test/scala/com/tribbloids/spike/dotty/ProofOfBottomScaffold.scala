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
        case v0 @ Eye =>
          summon[Bottom <:< v0.type].andThen(summon[v0.type <:< TSub])
      }
  }

  type ><:[+H, +T <: TupleThing] = Cons[? <: H, ? <: T]

  // IMPORTANT: DO NOT CHANGE ANYTHING ABOVE

  final case class Cons[H, T <: TupleThing](tail: T) extends TupleThing {

    override type Peer = (H ><: tail.Peer) | this.type

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
