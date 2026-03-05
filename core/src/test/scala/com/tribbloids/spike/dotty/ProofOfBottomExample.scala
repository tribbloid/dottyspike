package com.tribbloids.spike.dotty

object ProofOfBottomExample {

//  trait Coe[-I, +O] {
//    def apply(v: I): O
//  }

  type Coe[-I, +O] = I <:< O

  sealed trait TupleThing {

    type Peer >: this.type <: TupleThing

    type Bottom <: Peer

    def proofOfBottom[TSub >: Inhabited <: Peer]: Coe[Bottom, TSub]
  }

  type Inhabited = Eye.type & (? ><: ?)

  // IMPORTANT: DO NOT CHANGE ANYTHING ABOVE

  case object Eye extends TupleThing {

    override type Peer = Eye.type

    override type Bottom = Inhabited

    def proofOfBottom[TSub >: Inhabited <: Peer]: Coe[Bottom, TSub] = {
      ???
    }
  }

  sealed trait ><:[+H, +T <: TupleThing] extends TupleThing {
    val head: H
    val tail: T
  }

  type KK = Cons[Int, TupleThing]

  final case class Cons[H, T <: TupleThing](head: H, tail: T) extends (H ><: T) {

    override type Peer = H ><: T

    override type Bottom = (Nothing ><: (tail.Bottom & T))

    override def proofOfBottom[TSub >: Inhabited <: ><:[H, T]]: Coe[Bottom, TSub] = {
      // TODO: implement this, you can only use [[Coe]] to convert values
      //  do not extract head or tail
      //  do not use recursion
      //  write a test case to ensure that it works reliably

      ???
    }

//    def proofWithCompiler2[TSub <: Peer] = {
//      summon[Bottom <:< TSub]
//    }
  }
}
