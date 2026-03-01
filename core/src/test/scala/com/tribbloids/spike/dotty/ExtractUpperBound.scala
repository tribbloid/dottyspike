package com.tribbloids.spike.dotty

object ExtractUpperBound {

  trait Subject {

    type Dt

    def fn[T](v: T): T & Any
  }

  type DtLt[X] = Subject { type Dt <: X }

  type FnLt[X] = Subject { def fn[T](v: T): T & X }

  object SS1 extends Subject {

    type Dt <: Product
    def fn[T](v: T): T & Product = ???
  }

//  val v1: SS1.Dt & Any = (1, 2)
  val v2: (Int, Int) = SS1.fn((1, 2))

  type Extract[T <: Subject] = T match {

    case FnLt[X] => X
  }

  type E1 = Extract[SS1.type]
  summon[E1 =:= Product]
}
