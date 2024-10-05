package com.tribbloids.spike.dotty.quoted.autolift.issue

import scala.quoted.{Expr, Quotes, Type}

object Demo2 {

  object TASTyLowering {

    def deserialize[T](expr: Expr[List[String]])(
        using
        qt: Quotes,
        tt: Type[T]
    ): Expr[T] = {

      '{ ($expr).asInstanceOf[T] }
    }
  }

  def deserialize[T](expr: Expr[List[String]])(
      using
      Quotes
  ): Expr[T] = {

//    TASTyLowering.deserialize[T](expr)
    TASTyLowering.deserialize(expr) // reducce to Nothing

  }
}
