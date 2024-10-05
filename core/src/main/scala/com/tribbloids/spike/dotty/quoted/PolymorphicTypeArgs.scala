package com.tribbloids.spike.dotty.quoted

object PolymorphicTypeArgs {

  class Dummy {

    implicit def getThis: this.type = this
  }
  object Dummy1 extends Dummy
  object Dummy2 extends Dummy
  object Dummy3 extends Dummy

  class HasFns {

    def fn[T](
        implicit
        d: Dummy1.type
    ): T = ???

    def fn[T, T2](
        implicit
        d: Dummy2.type
    ): T2 = ???

    def fn[T, T2, T3](
        implicit
        d: Dummy3.type
    ): T3 = ???
  }

  object HasFns extends HasFns

  def main(args: Array[String]): Unit = {

    val x: Int = HasFns.fn[Int]
    val x2: String = HasFns.fn[Int, String]
    val x3: Boolean = HasFns.fn[Int, String, Boolean]
  }
}
