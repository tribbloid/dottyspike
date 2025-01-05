package com.tribbloids.spike.dotty

object DuckSubtyping {

  trait PlanK[T] {
    type Value
  }

  trait GraphKOfTheEngine[T] extends PlanK[T]

  type Plan[V] = PlanK[?] { type Value <: V }
  type Graph[V] = GraphKOfTheEngine[?] { type Value <: V }

  implicitly[Graph[Int] <:< Plan[Int]]
}
