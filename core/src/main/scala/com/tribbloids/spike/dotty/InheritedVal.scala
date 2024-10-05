package com.tribbloids.spike.dotty

object InheritedVal {

  trait Test {
    val x = 5
    println(x)
  }

  def main(args: Array[String]): Unit = {

    val t1 = new Test {
      override val x = 6
    }

//    val t2 = new {
//
//      override val x = 6
//    } with Test {}
    // TODO: doesn't work:
    //  Early definitions are not supported; use trait parameters instead
  }

}
