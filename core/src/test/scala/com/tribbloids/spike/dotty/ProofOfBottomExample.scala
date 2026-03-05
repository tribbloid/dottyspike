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
      summon[Bottom <:< TSub]
    }

//    def proofWithCompiler2[TSub <: Peer] = {
//      summon[Bottom <:< TSub]
//    }
  }

  val c1 = Cons(1, Cons("a", Eye))
  type T1 = c1.Bottom

  // DO NOT CHANGE ANYTHING BELOW, sanity test
  summon[T1 =:= Nothing ><: Nothing ><: Eye]

}
