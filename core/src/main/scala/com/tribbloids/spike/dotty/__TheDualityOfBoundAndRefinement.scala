package com.tribbloids.spike.dotty

object __TheDualityOfBoundAndRefinement {

  // the following 2 functions behave the same:

  trait T1
  trait T0 extends T1

  def fn1[T >: T0 <: T1](t: T): Set[T] = ???

  def fn2[T](t: T)(
      implicit
      ev: T <:< T1,
      ev2: T0 <:< T
  ): Set[T & T1] = ???

  // we should allow them to override each other:

  {
    def fn1[T >: T0 <: T1](t: T): Set[T & T1] = fn2[T](t) // trivial
  }

  {
    def fn2[T](t: T)(
        implicit
        ev: T <:< T1,
        ev2: T0 <:< T
    ): Set[T & T1] = {
      val down = ev(t)

    }
  }
}
