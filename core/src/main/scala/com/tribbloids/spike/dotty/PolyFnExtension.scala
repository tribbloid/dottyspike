package com.tribbloids.spike.dotty

object PolyFnExtension {

  type Base[O] = Function1[Any, Any] {

    def apply(x: Any): O
  }

  extension [O](base: Base[O]) {

    def applyOption(x: Any): Option[O] = Some(base.apply(x))
  }

  // TODO: none of these works
//  {
//    val poly = { (x: Any) => x: x.type }
//    val r1: Option[Int] = poly.applyOption(1)
//  }
//
//  {
//    val poly = { (x: Any) => Seq(x: x.type) }
//    val r1: Option[Seq[Int]] = poly.applyOption(1)
//  }

}
