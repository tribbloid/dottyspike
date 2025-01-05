package com.tribbloids.spike.dotty.t_proj

import ai.acyclic.six.testing.Assert

object CaseAgainst {
  // by Odersky: https://github.com/scala/scala3/issues/1050

  trait C { type A }

  object C {

    type K[T] = C { type A = T }
  }

  {
    type T = C { type A >: Any }
    type U = C { type A <: Nothing }
    type X = T & U // <- in DOT, determining its soundness is NP-hard?

    { // Odersky's case

      Assert.typeError("val y: X#A = 1")
    }

    {
      type R = X match {
        case C.K[a] => a
      }

      Assert.typeError("val y: R = 1")
    }
  }

  {
    type T = C { type A >: Int }
    type U = C { type A <: Int }
    type X = T & U // <- in DOT, determining its soundness is NP-hard?

    {
      type R = X match {
        case C.K[a] => a
      }

      Assert typeError "summon[Int <:< R]"
      Assert typeError "summon[R <:< Int]"

      Assert typeError "val y: R = 1"
    }
  }

  {
    trait T extends C { type A >: Int }
    trait U extends C { type A <: Int }
    final class X extends T with U {} // <- in DOT, determining its soundness is NP-hard?

    {
      type R = X match {
        case C.K[a] => a
      }

      Assert.typeError("summon[Int <:< R]")
      Assert.typeError("summon[R <:< Int]")

      Assert.typeError("val y: R = 1")
    }

    {
      val x: X = ???

      type R = x.A

      summon[Int <:< R]
      summon[R <:< Int]

      val y: R = 1
    }
  }
}
