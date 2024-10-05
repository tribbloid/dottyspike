package com.tribbloids.spike.dotty

object TypeArgMixinRule {

  object MixinOfType {

    trait C1[+T1, +T2]

    type CK1 = C1[Int, Any]

    type CK2 = C1[Any, String]

    val _: C1[Int, String] = ??? : CK1 & CK2
    val _: CK1 & CK2 = ??? : C1[Int, String] // good

    summon[(CK1 & CK2) <:< C1[Int, String]]
    summon[C1[Int, String] <:< (CK1 & CK2)]
    summon[(CK1 & CK2) =:= C1[Int, String]]

    type CK = CK1 & CK2

    summon[CK <:< C1[Int, String]]
    summon[C1[Int, String] <:< CK]
    summon[CK =:= C1[Int, String]]
  }

  object MixinOfTrait {

    trait C1[+T1, +T2]

    trait CK1 extends C1[Int, Any]

    trait CK2 extends C1[Any, String]

    type CK12 = CK1 & CK2
  }

  object MixinAsTrait {

    trait C1[+T1, +T2]

    trait CK1 extends C1[Int, Any]

    trait CK2 extends C1[Any, String]

    trait CK12 extends CK1 with CK2

    object CK12 extends CK12 // works miraculously, this will break Scala2, but the new type checker can handle it
  }
}
