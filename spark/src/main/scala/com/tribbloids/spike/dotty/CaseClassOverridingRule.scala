package com.tribbloids.spike.dotty

object CaseClassOverridingRule {
  {
    //  unary function (without eta-expansion) overriden by unary case class constructor

    trait A {
      type CC
//      def CC: Int => CC  // only works in Scala 2
      def CC: { def apply(v: Int): CC }
    }

    object AA extends A {

      case class CC(v: Int) {}
    }
  }

  {
    //  nullary function (without eta-expansion) overriden by nullary case class constructor

    trait A {
      type CC
//      def CC: () => CC // only works in Scala 2
      def CC: { def apply(): CC }
    }

    object AA extends A {

      case class CC() {}
    }
  }

  object Mk {

    type Mk1[I, O] = { def apply(v: I): O }

    type Mk0[O] = { def apply(): O }

    type Mk2[I1, I2, O] = { def apply(v1: I1, v2: I2): O }
  }

}
