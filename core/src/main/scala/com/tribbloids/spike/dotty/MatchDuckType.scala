package com.tribbloids.spike.dotty


object MatchDuckType {

  trait Sup
  class Sub1() extends Sup {
    def v1: String = "a"
  }

  def example(v: Any) = {

    v match {

      case _: Sup { def v1: String } => println(1)
      case _ => println(2)
    }
  }

  def main(args: Array[String]): Unit = {
    example(Sub1())
    example(1)
  }
}
