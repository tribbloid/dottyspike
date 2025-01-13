package com.tribbloids.spike.dotty

object ExtractingTypeArg {

  trait Vec[+T <: AnyRef]

  trait ArgOf[V <: Vec[?]] {

    type TT
  }

  object ArgOf {

    implicit def impl1[V <: Vec[?], T0 <: AnyRef](
        implicit
        ev: V <:< Vec[T0]
    ): ArgOf[V] { type TT = T0 } =
      new ArgOf[V] {
        override type TT = T0
      } // this is written by GitHub copilot

    //    implicit def impl2[T0 <: AnyRef, V <: Vec[T0]]: Extractor[V] { type TT = T0 } =
    //      new Extractor[V] {
    //        override type TT = T0
    //      } // this is written by me
  }

  trait StringVec extends Vec[String]

  val strEx = summon[ArgOf[Vec[String]]]
//  val strEx = summon[ArgOf[StringVec]]
  //  implicitly[intEx.TT =:= Int] // disabled temporarily for not explaining type reduction

  val x: String = ??? : strEx.TT
}
