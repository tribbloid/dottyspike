package com.tribbloids.spike.dotty.issue

import ai.acyclic.six.Show

object BoundLambda {

  trait Dummy[+Inc, -Dec, Invar] {}

  trait Thing extends Product

  type K[T] = T => Dummy[T, T, T]

//  type KI = K[? >: Thing <: Product] // doesn't work

  type KI2 = Dummy[? >: Thing <: Product, ? >: Thing <: Product, ? >: Thing <: Product] // works

  type KI3 = Dummy[Product, Thing, ? >: Thing <: Product] // works

//  summon[KI2 =:= KI3]

  object Dual_partial {

    trait KGen {
      type TMax
      type TMin <: TMax

      type _Dummy = Dummy[? >: TMin <: TMax, ? >: TMin <: TMax, ? >: TMin <: TMax]
    }

    object KGen2 extends KGen {

      type TMax = Product
      override type TMin = Thing
    }

    type KI2 = KGen2._Dummy // works

    type KI3 = Dummy[Product, Thing, ? >: Thing <: Product]

//    summon[KI2 =:= KI3] // oops, evidence not found
  }

  object Dual_Full {

    trait DummyGen {
      type IncMax

      type DecMin

      type InvarMax
      type InvarMin <: InvarMax

      trait Dummy {
        type Inc <: IncMax
        type Dec >: DecMin
        type Invar >: InvarMin <: InvarMax
      }
    }

    trait KGen {
      type TMax
      type TMin <: TMax

      type _Dummy = DummyGen#Dummy {

        type Inc <: TMax
        type Dec >: TMin
        type Invar >: TMin <: TMax
      }
    }

    object KGen2 extends KGen {

      override type TMax = Product
      override type TMin = Thing
    }

    type KI2 = KGen2._Dummy // works

    type KI3 = DummyGen#Dummy {
      type Inv <: Product
      type Dec >: Thing
      type Invar >: Thing <: Product
    }

    // summon[KI2 =:= KI3]
    /*
    still failed, but their dealiased form are already very similar:
      KI2:
com.tribbloids.spike.dotty.issue.BoundLambda.Dual_Full.DummyGen#Dummy {
  type Inc >: scala.Nothing <: com.tribbloids.spike.dotty.issue.BoundLambda.Dual_Full.KGen2.TMax
  type Dec >: com.tribbloids.spike.dotty.issue.BoundLambda.Dual_Full.KGen2.TMin <: scala.Any
  type Invar >: com.tribbloids.spike.dotty.issue.BoundLambda.Dual_Full.KGen2.TMin <: com.tribbloids.spike.dotty.issue.BoundLambda.Dual_Full.KGen2.TMax
}
      KI3:
com.tribbloids.spike.dotty.issue.BoundLambda.Dual_Full.DummyGen#Dummy {
  type Inv >: scala.Nothing <: scala.Product
  type Dec >: com.tribbloids.spike.dotty.issue.BoundLambda.Thing <: scala.Any
  type Invar >: com.tribbloids.spike.dotty.issue.BoundLambda.Thing <: scala.Product
}
     */
  }

  @main def main(): Unit = {

    val v0 = Show.showType[String]()

    val v1 = Show.showType[Dual_Full.KI2]()

    val v2 = Show.showType[Dual_Full.KI3]()

    println(v1.toString + "\n" + v2.toString)
  }
}
