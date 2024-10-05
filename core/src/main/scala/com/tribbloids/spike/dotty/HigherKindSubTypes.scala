package com.tribbloids.spike.dotty

object HigherKindSubTypes {

  trait A1 {
    type Take[T]
  }

  trait A2 {
    type Take[T <: Nothing]
  }

  trait A3 {

    type UB >: Nothing
    type Take[T <: UB]
  }

  trait A4[TAKE[_ <: Nothing]] {

    type UB >: Nothing

//    type TB[T] = TAKE[T]

//    type Take <: [T <: UB] =>> TAKE[_ <: T]

//    type TT = TAKE[String]
  }

  trait A5[TAKE[_]]

  trait B1 extends A1 {
//    override type Take[T <: String] = List[T] // Not allowed
  }

  trait B2 extends A2 {
    override type Take[T <: String] = List[T] // can do
  }

  trait B3 extends A3 {

    override type UB = String
    override type Take[T <: UB] = List[T] // can do
  }

  trait B4[TAKE[T <: String]] extends A4[TAKE] {} // can do

//  trait B5 extends A5[[T <: String] =>> List[T]]

  type A[T] = List[T]
  type B[T <: String] = List[T]
}
