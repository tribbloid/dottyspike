package com.tribbloids.spike.dotty

object ForComprehension {

  trait Tracer[+X] {
    def value: X
  }
  object Tracer {
    case class Value[X](value: X) extends Tracer[X] {}

    case object PlaceHolder extends Tracer[Nothing] {
      override def value: Nothing = ???
    }

    given unbox[T]: Conversion[Tracer[T], T] = { v =>
      v.value
    }
  }

  abstract class Traceable[T] {

    def flatMap[TT](fn: Tracer[T] => Traceable[TT])(
        implicit
        src: sourcecode.Line
    ): FlatMap[T, TT] = FlatMap(this, Fn(src, fn), fn(Tracer.PlaceHolder))

    def map[TT](fn: T => TT)(
        implicit
        src: sourcecode.Line
    ): Map[T, TT] = Map(this, Fn(src, fn))

    def withFilter(fn: T => Boolean)(
        implicit
        src: sourcecode.Line
    ): Filter[T] = Filter(this, Fn(src, fn))
  }

  object Traceable {

    extension [X, Y](tuple: (Traceable[X], Traceable[Y])) {

      def _cartesian = ><(tuple._1, tuple._2)

      def flatMap[TT](fn: (Tracer[(X, Y)]) => Traceable[TT])(
          implicit
          src: sourcecode.Line
      ): FlatMap[(X, Y), TT] = FlatMap(_cartesian, Fn(src, fn), fn(Tracer.PlaceHolder))

      def map[TT](fn: ((X, Y)) => TT)(
          implicit
          src: sourcecode.Line
      ): Map[(X, Y), TT] = Map(_cartesian, Fn(src, fn))

      def withFilter(fn: ((X, Y)) => Boolean)(
          implicit
          src: sourcecode.Line
      ): Filter[(X, Y)] = Filter(_cartesian, Fn(src, fn))
    }
  }

  case class ID[T](name: String) extends Traceable[T]

  case class Const[T](v: T) extends Traceable[T]

  case class Fn[S, T](src: sourcecode.Line, fn: S => T) extends Traceable[T] {

    override def toString: String = src.toString
  }

  case class Map[S, T](src: Traceable[S], fn: Fn[S, T]) extends Traceable[T]
  case class FlatMap[S, T](
      src: Traceable[S],
      fn: Fn[Tracer[S], Traceable[T]],
      default: Traceable[T]
  ) extends Traceable[T]

  case class Filter[T](src: Traceable[T], fn: Fn[T, Boolean]) extends Traceable[T]

  case class ><[X, Y](x: Traceable[X], y: Traceable[Y]) extends Traceable[(X, Y)]
}
