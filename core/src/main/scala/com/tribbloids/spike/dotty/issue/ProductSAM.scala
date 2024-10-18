package com.tribbloids.spike.dotty.issue

object ProductSAM {

  trait Example extends Product {

    def apply(v: Int): String

    override def canEqual(that: Any): Boolean = that.isInstanceOf[Example]
  }

//  object e0 extends Example {
//    override def apply(v: Int): String = v.toString
//  }
//
//  val e1: Example = new Example {
//    override def apply(v: Int): String = v.toString
//  }
//
//  val e2: Example = { (v: Int) =>
//    v.toString
//  } // TODO: fails: https://github.com/scala/scala3/issues/21777
}
