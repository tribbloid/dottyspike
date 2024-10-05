package com.tribbloids.spike.dotty

object GenericEtaExpansion {

  def fn[T](v: T): String = " " + v

  val ee: [T] => (T => String) = {
    [T] => { (v: T) =>
      " " + v
    }
  }

  def main(args: Array[String]): Unit = {

    println(ee.getClass)
  }
}
