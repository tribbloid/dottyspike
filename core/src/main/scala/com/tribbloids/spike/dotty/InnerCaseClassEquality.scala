package com.tribbloids.spike.dotty

object InnerCaseClassEquality {

  case class A(x: Int) {

    case class B(x: Int)
  }

  @main def main(): Unit = {

    val a1 = A(1)
    val a2 = A(1)
    val a3 = A(2)

    val b11 = a1.B(1)
    val b12 = a1.B(1)
    val b2 = a2.B(1)
    val b3 = a3.B(1)

    println(a1 == a2)
    println(b11 == b12)

//    val c = b2.$outer

    println(b11 == b2)
    println(b11.## == b2.##)

    println(b11 == b3)
    println(b11.## == b3.##)
  }
}
