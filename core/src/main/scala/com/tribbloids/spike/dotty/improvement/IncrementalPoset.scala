package com.tribbloids.spike.dotty.improvement

object IncrementalPoset {

  object SubType1 {

    trait X {
      type B;
      type A <: B
    }

    trait Y {
      type A;
      type B >: A
    }
//    object C1 extends X with Y // should be equivalent to:
//    object C2 {
//      type A <: B
//      type B >: A
//    } // should be equivalent to:
    object C3 {
      type B
      type A <: B
    }
  }

  object SubType2 {

//    object C1 {
//      type A <: B
//      type B <: A
//    } // v3.5.2: illegal cyclic type reference error
    // expectation: cycle with <=1 trait detected, should add A and B into an e-class and keep compiling, which leads to the equivalent form:
    object C2 {
      type A
      type B = A
    }
  }

  object SubType2_traits {

    trait X {
      trait B
      trait A extends B
    }
    trait Y {
      type A;
      type B <: A
    }
//    object C1 extends X with Y // v3.5.2: succeed, really shouldn't happen
    // expectation: cycle with >=2 traits detected, should throw an error immediately

    /*
    actually it causes the following error:

[Error] /home/peng/git/dottyspike/core/src/main/scala/com/tribbloids/spike/dotty/improvement/IncrementalPoset.scala:50:12: error overriding type B in trait Y with bounds <: com.tribbloids.spike.dotty.improvement.IncrementalPoset.SubType2_traits.C1.A;
  trait B in trait X has incompatible type

Explanation
===========
I tried to show that
   = X.this.B
conforms to
   <: com.tribbloids.spike.dotty.improvement.IncrementalPoset.SubType2_traits.C1.A
but none of the attempts shown below succeeded:

  ==> type bounds [ = X.this.B]  <:  type bounds [ <: com.tribbloids.spike.dotty.improvement.IncrementalPoset.SubType2_traits.C1.A] TypeAlias RealTypeBounds
    ==> X.this.B  <:  com.tribbloids.spike.dotty.improvement.IncrementalPoset.SubType2_traits.C1.A CachedTypeRef CachedTypeRef  = false

The tests were made under the empty constraint


     */
  }

  object SubType3 {

//    object C1 {
//      type A <: B
//      type B <: C
//      type C <: A
//    } // v3.5.2: illegal cyclic type reference error
    // should be equivalent to:
    object C2 {
      type A
      type B = A
      type C = A
    }
  }

  object TypeClass3 { // functionally equivalent to SubType3
    trait A; trait B; trait C

    given (
        using
        b: B
    ): A = ???
    given (
        using
        c: C
    ): B = ???

    given (
        using
        a: A
    ): C = ???

    { // succeed in v3.5.2, but can be made faster
      given C = ???
      summon[A]
      summon[B]
    }

    { // succeed in v3.5.2, but can be made faster
      given A = ???
      summon[C]
      summon[B]
    }
  }

  object TransitiveConversion3 { // functionally equivalent to SubType3
    trait A; trait B; trait C

    given identity[_X]: Conversion[_X, _X] = { x =>
      x
    }

    given [_B](
        using
        cc: Conversion[_B, B]
    ): Conversion[A, _B] = ???

    given [_C](
        using
        cc: Conversion[_C, C]
    ): Conversion[B, _C] = ???

    given [_A](
        using
        cc: Conversion[_A, A]
    ): Conversion[C, _A] = ???

    var a: A = ???
    var b: B = ???
    var c: C = ???
//    a = c
    /* implicit not found error in v3.5.2
    Found:    (com.tribbloids.spike.dotty.improvement.IncrementalPoset.TransitiveConversion3.c
   :
  com.tribbloids.spike.dotty.improvement.IncrementalPoset.TransitiveConversion3.
    C
)
Required: com.tribbloids.spike.dotty.improvement.IncrementalPoset.TransitiveConversion3.A

Explanation
===========

Tree: com.tribbloids.spike.dotty.improvement.IncrementalPoset.TransitiveConversion3.c
I tried to show that
  (com.tribbloids.spike.dotty.improvement.IncrementalPoset.TransitiveConversion3.c
   :
  com.tribbloids.spike.dotty.improvement.IncrementalPoset.TransitiveConversion3.
    C
)
conforms to
  com.tribbloids.spike.dotty.improvement.IncrementalPoset.TransitiveConversion3.A
but none of the attempts shown below succeeded:

  ==> (com.tribbloids.spike.dotty.improvement.IncrementalPoset.TransitiveConversion3.c    :   com.tribbloids.spike.dotty.improvement.IncrementalPoset.TransitiveConversion3.     C )  <:  com.tribbloids.spike.dotty.improvement.IncrementalPoset.TransitiveConversion3.A CachedTermRef CachedTypeRef
    ==> com.tribbloids.spike.dotty.improvement.IncrementalPoset.TransitiveConversion3.C  <:  com.tribbloids.spike.dotty.improvement.IncrementalPoset.TransitiveConversion3.A CachedTypeRef CachedTypeRef  = false

The tests were made under the empty constraint
     */
  }
}
