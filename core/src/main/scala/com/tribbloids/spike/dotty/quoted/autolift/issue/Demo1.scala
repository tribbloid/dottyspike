package com.tribbloids.spike.dotty.quoted.autolift.issue

import scala.quoted.{Expr, Quotes, Type}

case class Thing[+T](vs: List[String])

case class ExactThing[T](vs: List[String])

object Demo1 {

  object TASTyLowering {

    def deserialize[T](expr: Expr[List[String]])(
        using
        qt: Quotes,
        tt: Type[T]
    ): Expr[Thing[T]] = {

      '{ Thing[T]($expr) }
    }
  }

  def deserialize[T](expr: Expr[List[String]])(
      using
      Quotes
  ): Expr[Thing[T]] = {

    TASTyLowering.deserialize(expr)
  }
}
