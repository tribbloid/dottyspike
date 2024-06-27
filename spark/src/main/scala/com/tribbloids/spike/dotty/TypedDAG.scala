package com.tribbloids.spike.dotty

object TypedDAG {

  trait Plus[A <: Int, B <: Int]

  type R = Plus[3, 4]

  object Forward {

    trait Unary {

      trait Binary {

        type Plus
      }
    }

    object `3` extends Unary {
      object `4` extends Binary {

        trait Plus
      }
    }
  }

  object Reverse {

    trait Binary {

      type A <: Int
      type B <: Int

      trait Plus {}
    }

    object `34` extends Binary {

      type A = 3
      type B = 4
    }

  }
}
