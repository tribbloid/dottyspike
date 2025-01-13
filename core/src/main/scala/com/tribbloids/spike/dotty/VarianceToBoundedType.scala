package com.tribbloids.spike.dotty

object VarianceToBoundedType {

  trait Vec[+T] {

    type TT <: T

    type MM = Mat[TT]
  }

  trait VecR[T] extends Vec[T] {
    type TT = T
  }

  trait Mat[+T]

  implicitly[Mat[Product] <:< Mat[? <: Product]]

//  implicitly[Mat[_ <: Product] <:< Mat[Product]]

//  implicitly[Mat[Product] =:= Mat[_ <: Product]]

  object V1 extends Vec[String]

}
