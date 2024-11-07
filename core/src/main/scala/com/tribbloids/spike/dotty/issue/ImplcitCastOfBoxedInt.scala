package com.tribbloids.spike.dotty.issue

object ImplcitCastOfBoxedInt {

  def foo(x: Integer): Option[Int] = Option(x)

  def main(args: Array[String]): Unit = {
    val v = foo(null)

    println(v) // wrong, bug in Predef.scala
  }
}
