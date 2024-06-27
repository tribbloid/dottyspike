package com.tribbloids.spike.dotty

object CompliantType {

  trait Fn {
    type In
    type Out
  }

  case class FnOps[F <: Fn](final val self: F) extends Serializable {

    type In = self.In
    private type Out = self.Out

    type FCompliant = F & Fn { type In <: FnOps.this.In }

    implicitly[self.type <:< FCompliant] // won't work in Scala 2
  }
}
