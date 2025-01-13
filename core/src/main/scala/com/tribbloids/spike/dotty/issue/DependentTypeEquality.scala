package com.tribbloids.spike.dotty.issue

object DependentTypeEquality {
  // https://github.com/scala/scala3/issues/21509

  trait T1 {
    trait D {
      def compose(that: D): D = ???
    }

    val d: D = ???
  }

  def dummy(left: T1, right: T1)(
      using
      ev: left.type =:= right.type // singleton path equality
  ): Unit = {

    // TODO: these won't work
//    summon[left.D =:= right.D]
//    left.d.compose(right.d)
  }

  {
    trait T2[-T] {}

    def dummy[X, Y](
        using
        ev: X <:< Y // singleton path equality
    ): Unit = {

      // TODO: won't work
//      summon[T2[Y] <:< T2[X]]
    }
  }
}
