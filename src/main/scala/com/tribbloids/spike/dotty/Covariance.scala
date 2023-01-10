package com.tribbloids.spike.dotty

import com.tribbloids.spike.dotty.Covariance.AsDependentType.Gen.Upper

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

      type Cov = { type T <: Upper }
      type ContraV = { type T >: Upper }
    }

    object Gen extends Gen {

      type Upper = Any
      trait P {

        val vv: Upper
      }
    }

    object GenY extends Gen {

      type Upper = Product
      trait P1 extends Gen.P {

        val vv: Product
      }
    }

    object GenZ extends Gen {

      type Upper = Tuple1[Int]
      trait P2 extends GenY.P1 with Gen.P {

        val vv: Tuple1[Int]
      }
    }
  }
}
