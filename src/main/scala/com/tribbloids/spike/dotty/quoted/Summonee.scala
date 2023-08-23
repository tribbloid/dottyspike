package com.tribbloids.spike.dotty.quoted

object Summonee {

  import com.tribbloids.spike.dotty.quoted.Summoner._

  def main(args: Array[String]): Unit = {

    val s1 = typeShow[String]

    println(s1)

//    val t1 = typeOf[String]
//    println(t1)
    // need SerializingFallBackToExpr!
  }
}
