package com.tribbloids.spike.dotty

import cats.Functor

object AdHocFunctor {

  trait Expr

  trait Var extends Expr

  trait X extends Var
  trait Y extends Var
  trait Z extends Var

  trait Plus[T <: Expr](x: T, y: Double) extends Expr
  trait Times[T <: Expr](x: T, y: Double) extends Expr

//  trait Unary extends (Double => Double)
////  trait Binary extends ((Double, Double) => Double)
//
//  case class Plus(y: Double) extends Unary {
//
//    override def apply(x: Double): Double = x + y
//  }
//
//  case class Times(y: Double) extends Unary {
//
//    override def apply(x: Double): Double = x * y
//  }
//
//  case class DiffX[N](fn: N)
//
//  object DiffX {
//
//    case class IsFt() extends Functor[DiffX] {
//
//      override def map[A, B](fa: DiffX[A])(f: A => B): DiffX[B] = {}
//    }
//  }
}
