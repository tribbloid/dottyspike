package com.tribbloids.spike.dotty.kyo

object InferToMostSpecificType {

  type <[+T, -S] >: T

  trait Options

  trait Options2

  trait Others

  object Options {

    def run[T, S](v: T < (Options with S)): Option[T] < S = ???
  }

  val v1: Int < (Options with Others) = ???

  val v2 = Options.run(v1)

  val v3: Option[Int] < Others = v2

}
