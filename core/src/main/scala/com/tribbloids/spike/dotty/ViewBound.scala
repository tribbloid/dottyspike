package com.tribbloids.spike.dotty

object ViewBound {

  object V1 {

    trait Boxed[T] {
      def self: T
    }

    object Boxed {

      implicit def unbox[T, R](
          boxed: Boxed[T]
      )(
          implicit
          more: T => R
      ): R = more(boxed.self)
    }

    case class B0(self: String) extends Boxed[String]
    case class B1(self: B0) extends Boxed[B0]
    case class B2(self: B1) extends Boxed[B1]

    val b2 = B2(B1(B0("a")))

//    val ab = b2.concat("b") // TODO: doesn't work
  }

  object V2 {

    trait Boxed[T] {
      def self: T
    }

    object Boxed {

      trait ViewBound[S, R] extends (S => R) {}

      implicit def unbox1[T]: ViewBound[Boxed[T], T] = v => v.self

      implicit def unboxMore[T, R](
          implicit
          more: ViewBound[T, R]
      ): ViewBound[Boxed[T], R] = v => more(v.self)

      implicit def convert[T, R](v: Boxed[T])(
          implicit
          unbox: ViewBound[Boxed[T], R]
      ): R = unbox(v)
    }

    case class B0(self: String) extends Boxed[String]
    val b0 = B0("a")
    b0.concat("b")

    case class B1(self: B0) extends Boxed[B0]
    val b1 = B1(B0("a"))
//    b1.concat("b")

    case class B2(self: B1) extends Boxed[B1]
    val b2 = B2(B1(B0("a")))
//    b2.concat("b")
  }

}
