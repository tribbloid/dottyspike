package com.tribbloids.spike.dotty.conjecture

object Poly1Group {

  { // primary

    trait Poly1Group[+OUB] {

      trait Case1 {
        type Out <: OUB // <------------------------------- success
      }
//      type Case1Aux[O <: OUB] = Case1 { type Out = O } // <------------------------------------- fail

//      trait Case2[
//          O <: OUB // <------------------------------------- fail
//      ] extends Case1 {}
    }
  }

  { // dual

    trait OUBGen {
      type OUB_/\

      trait Poly1Group {

        trait Case1 {

          type Out <: OUB_/\ // <------------------------------- success

          def apply(v: Any): Out
        }

        trait OGen {
          type O <: OUB_/\

          trait Case {
            type Out = O
          }
        }
      }
    }
  }
}
