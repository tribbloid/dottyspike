package com.tribbloids.spike.dotty

object ProofOfBottomExample {

  trait Coe[-I, +O] {
    def apply(v: I): O
  }

  sealed trait TupleThing {

    type Peer >: this.type <: TupleThing

    type Bottom <: Peer

    def proofOfBottom[TSub >: Inhabited <: Peer]: Coe[Bottom, TSub]

    def proofWithCompiler[TSub >: Inhabited <: Peer]: Bottom <:< TSub
  }

  case object Eye extends TupleThing {

    override type Peer = Eye.type

    override type Bottom = Eye.type

    def proofOfBottom[TSub >: Inhabited <: Peer]: Coe[Bottom, TSub] = { v =>
      v
    }

    def proofWithCompiler[TSub >: Inhabited <: Peer] = {
      summon[Bottom <:< TSub]
    }
  }

  sealed trait ><:[+H, +T <: TupleThing] extends TupleThing {
    val tail: T
  }

  type Inhabited = Eye.type | ? ><: ?

  // IMPORTANT: DO NOT CHANGE ANYTHING ABOVE

  type KK = Cons[Int, TupleThing]

  final case class Cons[H, T <: TupleThing](tail: T) extends (H ><: T) {

    override type Peer = H ><: T

    override type Bottom = (Nothing ><: (tail.Bottom & T))

    override def proofOfBottom[TSub >: Inhabited <: ><:[H, T]]: Coe[Bottom, TSub] = { v =>
      v
    }

    def proofWithCompiler[TSub >: Inhabited <: Peer] = {
      summon[Bottom <:< TSub]
    }

//    def proofWithCompiler2[TSub <: Peer] = {
//      summon[Bottom <:< TSub]
//    }
  }
}
