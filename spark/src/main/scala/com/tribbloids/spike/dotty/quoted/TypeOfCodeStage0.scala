package com.tribbloids.spike.dotty.quoted

import scala.compiletime.summonInline
import scala.quoted.{Expr, Quotes, Type}

object TypeOfCodeStage0 {

  //  val tt = summonInline[Type[Int]] // oops not working

  //  inline final val t1 = Type.show[Int]
  //  inline final val t2 = Type.of[Int]

  inline def typeShow[T <: AnyKind]: String = {
    ${ typeShowCode[T]() }
  }

  def typeShowCode[T <: AnyKind]()(
      using
      Type[T],
      Quotes
  ): Expr[String] = {

    val value = Type.show[T]
    Expr(value)
  }

  inline def typeOf[T <: AnyKind]: Type[T] = {
    ${ typeOfCode[T]() }
  }

  def typeOfCode[T <: AnyKind]()(
      using
      Type[T],
      Quotes
  ): Expr[Type[T]] = {

    val value = Type.of[T]
//    Expr(value) // No AutoLifter
    ???
  }

  //  implicit def summon
}
