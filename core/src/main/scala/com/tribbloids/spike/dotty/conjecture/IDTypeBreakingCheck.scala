package com.tribbloids.spike.dotty.conjecture

object IDTypeBreakingCheck {
  object Primary {

    type ID[+T] = T

    trait Under[IUB, TTUB] {
      // all members have to be type, no trait allowed
      type ID2[T <: IUB] = T

      type TT[-I[_ <: IUB] <: TTUB]
    }

    object UnderString extends Under[String, String]

    type TT2 = UnderString.TT[UnderString.ID2] // works
    //  type TT1 = UnderString.TT[ID] // doesn't
  }
}
