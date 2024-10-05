package com.tribbloids.spike.dotty

object CompileTimeOps {

  import compiletime.ops.int.*

  class Foo[T <: Int with Singleton](t: T) {
    val x: T + T = (t + t).asInstanceOf[T + T]

    type Y = T / T
    type Z = T =:= 1

    lazy val y: T / T = (t / t).asInstanceOf[T / T]
  }

  val f1 = new Foo(1)
  val f2 = new Foo(2)
  val x6: 6 = f1.x + f2.x

  val f3 = new Foo(0)
//  val xE = f3.y + f3.x
//  type EE = f3.Y //ERROR!
  type FF = f3.Z

  type NN = (2 + 3) =:= 6

  def main(args: Array[String]): Unit = {
    println(x6)
  }
}
