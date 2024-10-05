package com.tribbloids.spike.dotty.quoted.autolift.spike

object TypeOfCode {

  import TypeOfCodeStage0.*

  def main(args: Array[String]): Unit = {

    val s1 = typeShow[String]

    println(s1)

//    val t1 = typeOf[String]
//    println(t1)
    // need SerializingFallBackToExpr!
  }
}
