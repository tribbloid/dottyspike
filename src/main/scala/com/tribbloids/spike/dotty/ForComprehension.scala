package com.tribbloids.spike.dotty

object ForComprehension {

  abstract class Traceable[T] {

    def flatMap[TT](fn: T => Traceable[TT]): FlatMap[T, TT] = FlatMap(this, fn)

    def map[TT](fn: T => TT): FlatMap[T, TT] = flatMap(v => Const(fn(v)))

    def withFilter(fn: T => Boolean): Filter[T] = Filter(this, fn)
  }

  object Traceable {

    extension [X, Y](tuple: (Traceable[X], Traceable[Y])) {

      def _cartesian = ><(tuple._1, tuple._2)

      def flatMap[TT](fn: ((X, Y)) => Traceable[TT]): FlatMap[(X, Y), TT] = FlatMap(_cartesian, fn)

      def map[TT](fn: ((X, Y)) => TT): FlatMap[(X, Y), TT] = flatMap(v => Const(fn(v)))

      def withFilter(fn: ((X, Y)) => Boolean): Filter[(X, Y)] = Filter(_cartesian, fn)

    }
  }

  case class ID[T](name: String) extends Traceable[T]

  case class Const[T](v: T) extends Traceable[T]

  case class FlatMap[S, T](src: Traceable[S], fn: S => Traceable[T]) extends Traceable[T]

  case class Filter[T](src: Traceable[T], fn: T => Boolean) extends Traceable[T]

  case class ><[X, Y](x: Traceable[X], y: Traceable[Y]) extends Traceable[(X, Y)]
}
