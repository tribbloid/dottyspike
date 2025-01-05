package com.tribbloids.spike.dotty

object PrivateEscapingScope {

  trait X1

  trait T1 {

    object R1 extends X1
    type R1 = R1.type
  }

  trait K1 {
    private lazy val t1: T1 = new T1 {}

//    def r1 = t1.R1 // the only difference with K2 is lazy caching

    /**
      * non-private method r1 in trait K1 refers to private lazy value t1 in its type signature => K1.this.t1.R1.type
      * def r1 = t1.R1 // the only difference with K2 is lazy caching
      */
  }

  type T1R1 = T1#R1

  trait K1_2 {
    private lazy val t1: T1 = new T1 {}

//    def r1: T1#R1 = t1.R1

    /**
      * Found: K1_2.this.t1.R1.type
      *
      * Required: com.tribbloids.spike.dotty.PrivateEscapingScope.T1#R1
      *
      * Explanation \===========
      *
      * Tree: this.t1.R1 I tried to show that K1_2.this.t1.R1.type conforms to
      * com.tribbloids.spike.dotty.PrivateEscapingScope.T1#R1 but none of the attempts shown below succeeded:
      *
      * \==> K1_2.this.t1.R1.type <: com.tribbloids.spike.dotty.PrivateEscapingScope.T1#R1 CachedTermRef CachedTypeRef
      * \==> object K1_2.this.t1.R1 <: com.tribbloids.spike.dotty.PrivateEscapingScope.T1#R1 CachedTypeRef CachedTypeRef
      * \= false
      *
      * The tests were made under the empty constraint
      *
      * def r1: T1#R1 = t1.R1
      */
  }

  trait K1_3 {
    private lazy val t1: T1 = new T1 {}

    def r1: X1 = t1.R1 // explicitly widen to Product
  }

  trait K2 {
    private def t1: T1 = new T1 {}

    def r1 = t1.R1 // widen to Product
  }
}
