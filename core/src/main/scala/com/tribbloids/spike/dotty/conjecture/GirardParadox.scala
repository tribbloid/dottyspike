package com.tribbloids.spike.dotty.conjecture

object GirardParadox {

  object Paradox {

    { // Primary
      trait Vec[T]

//      type VV1 = List[Vec] // paradox!

      type VV2 = [T] =>> Vec[T]

//      type NN = List[VV2] // still paradox!
    }

    { // Dual
      trait Vec_* {
        type TT
      }

      trait TGen {

        type T

        trait Vec extends Vec_* {
          type TT = T
        }
      }

      type VV2 = [TT] =>> (TGen { type T = TT })#Vec

//      type NN = List[VV2] // paradox!
    }
  }
}
