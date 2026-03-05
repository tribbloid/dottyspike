package com.tribbloids.spike.dotty

import com.tribbloids.spike.dotty.ProofOfBottomAlt.Eye.{Bottom, Peer}
import com.tribbloids.spike.dotty.ProofOfBottomAlt.TupleThing

object ProofOfBottomAlt {

  trait Coe[-I, +O] {
    def apply(v: I): O
  }

  sealed trait TupleThing {

    type Peer >: this.type <: TupleThing

    type Bottom <: Peer

    def proofOfBottom[TSub >: (Eye.type | ? ><: ?) <: Peer]: Coe[Bottom, TSub]
  }

  case object Eye extends TupleThing {

    override type Peer = Eye.type

    override type Bottom = Eye.type

    val coeEye: Coe[Eye.type, Eye.type] = v => v

    def proofOfBottom[TSub >: (Eye.type | ? ><: ?) <: Peer]: Coe[Bottom, TSub] = {
      coeEye
    }
  }

  trait ><:[+H, +T <: TupleThing] extends TupleThing {
    val tail: T
  }

  // IMPORTANT: DO NOT CHANGE ANYTHING ABOVE

  type KK = Cons[Int, TupleThing]

  final case class Cons[H, T <: TupleThing](tail: T) extends (H ><: T) {

    override type Peer = H ><: T

    override type Bottom = (Nothing ><: (tail.Bottom & T))

    def co[H2 >: H, T2 >: T <: TupleThing]: Coe[H ><: T, H2 ><: T2] = v => v

    override def proofOfBottom[TSub >: (Eye.type | ? ><: ?) <: ><:[H, T]]: Coe[Bottom, TSub] = {
      case v0: Cons[Nothing, (tail.Bottom & T)] =>
        val v1 = v0.co[H, (tail.Bottom & T)]

    }
  }
}
