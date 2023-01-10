package com.tribbloids.spike.dotty

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

      type _CoVP = P { type T <: Upper }
      trait CoVP extends P {
        type T <: Upper
        val vv: Upper
      }
    }

    trait P {}

    object GenY extends Gen {

      type Upper = Product
      trait P1 extends P with CoVP {

        val vv: Product
      }
    }

    object GenZ extends Gen {

      type Upper = Tuple1[Int]
      trait P2 extends GenY.P1 with P with CoVP {

        val vv: Tuple1[Int]
      }
    }

    implicitly[P with GenZ._CoVP <:< P with GenY._CoVP]
  }
}
