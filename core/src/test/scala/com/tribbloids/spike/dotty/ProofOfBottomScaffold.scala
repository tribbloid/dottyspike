package com.tribbloids.spike.dotty

object ProofOfBottomScaffold {

  sealed trait Tuple {

    type Peer >: this.type <: Tuple

//    def proofOfPeer: this.type <:< Peer

    def peer: Peer = this

    type Bottom <: Peer

    def proofOfBottom[TSub <: Peer](v: TSub): Bottom <:< TSub
  }

  case object Eye extends Tuple {

    override type Peer = Eye.type

//    override def proofOfPeer: this.type <:< Peer = summon

    override type Bottom = Eye.type

    override def proofOfBottom[TSub <: Peer](v: TSub): Bottom <:< TSub =
      v match {
        case v0 @ Eye =>
          summon[Bottom <:< v0.type].andThen(summon[v0.type <:< TSub])
      }
  }

  // IMPORTANT: DO NOT CHANGE ANYTHING ABOVE

  type ><:[+H, +T <: Tuple] = Cons[? <: H, ?] { type Tail <: T }

  final case class Cons[H, T <: Tuple](tail: T) extends Tuple {

    type Head = H
    type Tail = tail.type

    override type Peer = H ><: tail.Peer

//    override def proofOfPeer: this.type <:< Peer = summon

    override type Bottom = Nothing ><: tail.Bottom

    type Peer2 = H ><: tail.Peer

    override def proofOfBottom[TSub <: Peer](v: TSub): Bottom <:< TSub = {

      v match {
        case v0 @ Cons(v0Tail) =>
          val preamble1 = summon[v0.type <:< TSub]

          val prev: v0Tail.Bottom <:< v0Tail.Peer = v0Tail.proofOfBottom(v0Tail)

          val head: Nothing <:< v0.Head = summon

          val zipped: (Nothing ><: v0Tail.Bottom) <:< (v0.Head ><: v0Tail.Peer) = ???
          val zipped2: (Nothing ><: v0Tail.Bottom) <:< (v0.Peer) = ???
          val zipped3: v0Tail.Bottom <:< v0.Peer = ???
          val zipped4: v0Tail.Bottom <:< v0.type = ???

          ???
      }

      ???
    }

  }
}
