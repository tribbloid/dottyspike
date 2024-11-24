package com.tribbloids.spike.dotty

object HigherKindTuples {

  trait TuplesKind[F[_]] {

    trait TupleF {

      type Repr <: Tuple
    }

    object Nil extends TupleF {

      type Repr = EmptyTuple
    }
    type Nil = Nil.type

    case class ><[T <: TupleF, H](tail: TupleF) extends TupleF { // ket-bra notation

      type Repr = F[H] *: tail.Repr
    }
  }

  object VecTuples extends TuplesKind[Vector] {

    val k: Nil >< String >< Int >< Double = ???

    val v: k.Repr = ???
  }
}
