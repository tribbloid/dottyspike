package com.tribbloids.spike.dotty

object OverloadPriority {

  trait A
  trait B
  trait C extends A with B

  trait LowLevel {

    def f[T <: A](x: T): Seq[T] = Seq(x)
  }

  object HighLevel extends LowLevel {

    def f[T <: B](x: T): Vector[T] = Vector(x)
  }

  HighLevel.f(new C {}) // Vector(C@1)
}
