package com.tribbloids.spike.dotty.issue

import ai.acyclic.six.verification.Verify

object FnAsDepFn {
// https://github.com/scala/scala3/issues/22363

  trait DepFnK[-I] extends Function1[I, ?] {

    type Out

    def apply(v: I): Out
  }

  trait DepFn[-I, +O] extends Function1[I, O] with DepFnK[I] {
    type Out <: O
  }

  given fn2DepFn[I, O]: Conversion[(I => O), DepFn[I, O]] = {
    new Conversion[(I => O), DepFn[I, O]] {

      def apply(v1: I => O): DepFn[I, O] = {
        new DepFn[I, O] {
          type Out = O
          def apply(v: I): O = v1(v)
        }
      }
    }
  }

  val fnGround: DepFn[Int, String] = fn2DepFn { v =>
    "" + v
  }

  Verify typeError
    """
  val fn1: DepFn[Int, String] = { v =>
    "" + v
  }
  """

  Verify typeError
    """
  val fn2: DepFn[Int, String] = { (v: Int) =>
    " " + v
  }
  """

  """
      |
      |""".stripMargin

  Verify typeError
    """
  val fn3: DepFn[?, ?] = { (v: Int) =>
    " " + v
  }
  """
}
