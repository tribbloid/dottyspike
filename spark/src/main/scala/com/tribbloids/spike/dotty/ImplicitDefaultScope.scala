package com.tribbloids.spike.dotty

object ImplicitDefaultScope {

  trait Case[T]

  object case1 {

    trait Outer {

      case class A(v: String)

      implicit def caseA: Case[A] = ???
    }

    {
      def fn(o: Outer) = {

        implicitly[Case[o.A]]
      }
    }
  }
}
