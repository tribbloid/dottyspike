package com.tribbloids.spike.dotty

object ExtractUpperBound {

  trait Subject {

    type Dt // upper bound

    def fn: Any // ditto, but as self-type bound
  }

  type DtLt[X] = Subject { type Dt <: X }

  type FnLt[X] = Subject { def fn: X }

  trait SS1 extends Subject {

    type Dt <: Product
    override def fn: Product = ???
  }
  object SS1 extends SS1

  trait SS2 extends SS1 {

    type Dt <: Tuple
    override def fn: Tuple = ???
  }

  type Extract[T <: Subject] = T match { // should be equivalent to getting the upper bound of Dt

    case FnLt[x] => x
  }

//  val v1: SS1.Dt & Any = (1, 2) // doesn't work
  val v2: Extract[SS1.type] = SS1.fn // works

//  summon[Extract[SS1.type] =:= Product] // fail
//  summon[Extract[SS1.type] <:< Product] // fail
  summon[Product <:< Extract[SS1.type]] // works
}
