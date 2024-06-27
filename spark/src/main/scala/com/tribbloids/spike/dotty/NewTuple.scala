package com.tribbloids.spike.dotty

object NewTuple {

  def main(args: Array[String]): Unit = {

    val t1 = (1, "ss", 1.1)
//    {
//      val (a, b) = tuple // succeed
//      println(a)
//      println(b)
//    }
////    println(c)
//
////    summon[tuple.type <:< *:[Int, String]]

//    val _: *:[Int, *:[String, Double]] = tuple
//
//    val _: *:[Double, *:[String, Int]] = tuple

//    val _: Double *: String *: Int *: EmptyTuple = tuple

    val _: Int *: String *: Double *: EmptyTuple = t1

    // unapply
    {
      val *:(a, b) = t1

      println("" + a + "," + b)
    }

    {
      val t2 = t1.*:("a")
      t2.toArray
    }

    {
      val t2 = t1.:*("a")
      t2.toArray
    }

    val tt = Tuple.fromArray(Array("a", 1, 1.1))

  }
}
