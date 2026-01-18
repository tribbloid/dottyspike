package com.tribbloids.spike.dotty

object ExtendingDuck {

  trait D1 { type K }

  type Aux[T] = D1 { type K = T }

  trait Duck1 extends Aux[Int] // <-- requires "-language:experimental.modularity"
  trait Duck2 extends D1 { type K = Int }
}
