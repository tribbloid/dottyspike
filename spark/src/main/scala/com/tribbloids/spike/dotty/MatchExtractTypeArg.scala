package com.tribbloids.spike.dotty

import scala.reflect.Typeable

object MatchExtractTypeArg {

  def mapType(k: Typeable[_], v: Typeable[_]) = {

    (k, v) match {
      case (k: Typeable[a], v: Typeable[b]) =>
        given kk: Typeable[a] = k
        given vv: Typeable[b] = v
        summon[Typeable[Map[a, b]]]
    }
  }

  def main(args: Array[String]): Unit = {

    val t1 = summon[Typeable[Int]]
    val t2 = summon[Typeable[String]]

    val r = mapType(t1, t2)
    println(r)
  }
}
