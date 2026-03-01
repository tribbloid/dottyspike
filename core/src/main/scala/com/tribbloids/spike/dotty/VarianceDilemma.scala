package com.tribbloids.spike.dotty

object VarianceDilemma {

  trait SS[T]

  {
// these should completely safe, equivalent to TT1 & TT2, yet they fail to typecheck
//    trait SS1[+T] extends SS[T]
//    trait SS2[-T] extends SS[T]
  }

  trait TT1[T] extends SS[T] {

    implicit def co[T1, T2 <: T1]: TT1[T2] <:< TT1[T1]
  }

  trait TT2[T] extends SS[T] {

    implicit def contra[T1, T2 >: T1]: TT1[T2] <:< TT1[T1]
  }
}
