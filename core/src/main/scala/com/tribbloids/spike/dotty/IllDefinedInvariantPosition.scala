package com.tribbloids.spike.dotty

object IllDefinedInvariantPosition {

  { // primary

    trait II[+T]

    trait JJ[+T] {

//      type IJ = II[T]
    }
  }

  { // dual

    trait II_* { type _T }
    trait JJ_* { type _T; type IJ }
    trait TGen {
      type T_/\

      trait II extends II_* { type _T <: T_/\ }

      trait JJ extends JJ_* {
        type _T <: T_/\
        type IJ = II
      }
    }
  }
}
