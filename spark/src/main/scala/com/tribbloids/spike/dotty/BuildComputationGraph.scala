package com.tribbloids.spike.dotty

import com.tribbloids.spike.dotty.BuildComputationGraph.Tracer.FP32

object BuildComputationGraph {

  trait Tracer[T] {
    def compute: T

    def flatMap[U](f: Tracer[T] => Tracer[U]): Tracer[U] = { f(this) }
    def map[U](f: Tracer[T] => U): U = { f(this) }
  }

  object Tracer {

    case class Symbol[T](override val toString: String) extends Tracer[T]:
      override def compute: T = ???

    case class Pure[T](compute: T) extends Tracer[T] {

      @transient override lazy val toString: String = compute.toString
    }

    case class BiFn[X, Y, O](
        operatorSymbol: String,
        v1: Tracer[X],
        v2: Tracer[Y]
    )(
        val fn: (X, Y) => O
    ) extends Tracer[O] {
      override def compute: O = fn(v1.compute, v2.compute)

      @transient override lazy val toString: String = s"($v1 $operatorSymbol $v2)"
    }

    type FP32 = Tracer[Double]

    implicit class FP32Ops(self: FP32) {
      def *(that: FP32) = BiFn("*", self, that)(_ * _) // TODO: can these be replaced with dynamic + quote?
      def +(that: FP32) = BiFn("+", self, that)(_ + _)
    }
  }

  def l2Norm(x: FP32, y: FP32): FP32 = {

    for (
      xx <- x * x;
      yy <- y * y;
      res <- xx + yy
//      res2 <- res
    ) yield {
      res
    }
  }

  def main(args: Array[String]): Unit = {

    val x = Tracer.Symbol[Double]("x")
    val y = Tracer.Pure(4.0)

    val res = l2Norm(x, y)

    println(res)
    res
  }

}
