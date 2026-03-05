package com.tribbloids.spike.dotty

import com.tribbloids.spike.dotty.ProofOfBottomExample.><:

object ProofOfBottomExample {

  trait Coe[-I, +O] {
    def apply(v: I): O
  }

  type Inhabited = Eye.type | ? ><: ?

  sealed trait TupleThing {

    type Peer >: this.type <: TupleThing

    type Bottom <: Peer

    def proofOfBottom[TSub >: (Eye.type | ? ><: ?) <: Peer]: Coe[Bottom, TSub]

    def proofWithCompiler[TSub >: (Eye.type | ? ><: ?) <: Peer]: Bottom <:< TSub
  }

  case object Eye extends TupleThing {

    override type Peer = Eye.type

    override type Bottom = Eye.type

    def proofOfBottom[TSub >: (Eye.type | ? ><: ?) <: Peer]: Coe[Bottom, TSub] = { v =>
      v
    }

    def proofWithCompiler[TSub >: (Eye.type | ? ><: ?) <: Peer] = {
      summon[Bottom <:< TSub]
    }
  }

  sealed trait ><:[+H, +T <: TupleThing] extends TupleThing {
    val tail: T
  }

  // IMPORTANT: DO NOT CHANGE ANYTHING ABOVE

  type KK = Cons[Int, TupleThing]

  final case class Cons[H, T <: TupleThing](tail: T) extends (H ><: T) {

    override type Peer = H ><: T

    override type Bottom = (Nothing ><: (tail.Bottom & T))

    override def proofOfBottom[TSub >: (Eye.type | ? ><: ?) <: ><:[H, T]]: Coe[Bottom, TSub] = { v =>
      v
    }

    def proofWithCompiler[TSub >: (Eye.type | ? ><: ?) <: Peer] = {
      summon[Bottom <:< TSub]
    }

//    def proofWithCompiler2[TSub <: Peer] = {
//      summon[Bottom <:< TSub]
//    }
  }
}
