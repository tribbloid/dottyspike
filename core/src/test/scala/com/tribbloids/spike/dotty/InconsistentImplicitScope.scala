package com.tribbloids.spike.dotty

// Example usage
object InconsistentImplicitScope {

  trait Gen[X]

  trait T2[X, Y]

  implicit def gt2[X, Y](
      implicit
      gx: Gen[String],
      gy: Gen[Path]
  ): Gen[Assignment] = ???

  implicit def gStr: Gen[String] = ???

  trait Path

  case class Assignment(user: String, path: Path) extends T2[String, Path]
  object Assignment {

    implicit def gp: Gen[Path] = {
      ???
    }
  }

//  implicitly[Gen[Assignment]] // doesn't work, gp dropping out of scope
}
