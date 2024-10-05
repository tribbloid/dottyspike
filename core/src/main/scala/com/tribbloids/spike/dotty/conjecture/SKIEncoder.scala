package com.tribbloids.spike.dotty.conjecture

object SKIEncoder {

  object Problems {

    trait Term {
      type ap[x <: Term] <: Term
      type eval <: Term
    }

    // The S combinator
    trait S extends Term {
      type ap[x <: Term] = S1[x]
      type eval = S
    }

    trait S1[x <: Term] extends Term {
      type ap[y <: Term] = S2[x, y]
      type eval = S1[x]
    }

    trait S2[x <: Term, y <: Term] extends Term {
//      type ap[z <: Term] = S3[x, y, z]
      type eval = S2[x, y]
    }

//    trait S3[x <: Term, y <: Term, z <: Term] extends Term {
//      type ap[v <: Term] = eval#ap[v]
//      type eval = x#ap[z]#ap[y#ap[z]]#eval
//    }
//
//    // The K combinator
//    trait K extends Term {
//      type ap[x <: Term] = K1[x]
//      type eval = K
//    }
//
//    trait K1[x <: Term] extends Term {
//      type ap[y <: Term] = K2[x, y]
//      type eval = K1[x]
//    }
//
//    trait K2[x <: Term, y <: Term] extends Term {
//      type ap[z <: Term] = eval#ap[z]
//      type eval = x#eval
//    }
//
    // The I combinator
//    trait I extends Term {
//      type ap[x <: Term] = I1[x]
//      type eval = I
//    }
//
//    trait I1[x <: Term] extends Term {
//      type ap[y <: Term] = eval#ap[y]
//      type eval = x#eval
//    }
  }
}
