package com.tribbloids.spike.dotty

object UnionOfPrimitives {

  type UU  = Int | Double | Null

//  implicitly[UU <:< AnyVal]
//  implicitly[UU <:< AnyRef]
  implicitly[UU <:< Any]
  
  val i1: UU = 1
  val i2 : UU = 2.0
  val i3: UU = null

  def main(args: Array[String]): Unit = {
    println(i1.getClass)
    println(i2.getClass)
//    println(i3.getClass)

    val ref: Int = 1
    println(ref.getClass)

    val ref2 = null
    ref2.getClass
  }
}
