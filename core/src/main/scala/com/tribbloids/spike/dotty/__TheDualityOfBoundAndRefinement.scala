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
      // Using the implicit evidence to cast into type bound
      // ev: T <:< T1 proves T <: T1
      // ev2: T0 <:< T proves T0 <: T
      // Combined: T0 <: T <: T1, so T satisfies >: T0 <: T1

      // fn1 requires type parameter U >: T0 <: T1
      // We use T0 as the type argument since it definitely satisfies the bounds
      val t0: T0 = t.asInstanceOf[T0]
      val result: Set[T0] = fn1[T0](t0)

      // Widen from Set[T0] to Set[T & T1]
      // Since T0 <: T (ev2) and T <: T1 (ev), we have T0 <: T & T1
      result.asInstanceOf[Set[T & T1]]
    }
  }
}
