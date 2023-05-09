package com.tribbloids.spike.dotty.conjecture

object Covariance {

  object AsArg {

    trait P[+TT] {
      val vv: TT
    }

    trait P1 extends P[Product] {
      val vv: Product
    }

    trait P2 extends P1 with P[Tuple1[Int]] {
      val vv: Tuple1[Int]
    }
  }

  object AsDependentType {

    trait Gen {

      type Upper

      trait CoVP {
        type T <: Upper

        type _Upper = Gen.this.Upper
      }
    }

    object GenY extends Gen {

      type Upper = Product
      trait P1 extends CoVP {}
    }

    object GenZ extends Gen {

      type Upper = Tuple1[Int]
      trait P2 extends CoVP with GenY.P1 {}
    }
  }
}
