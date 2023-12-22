package com.tribbloids.spike.dotty

object ExtendingMixinOfSameType {

  trait A[+T] {}

  type AA = A[Any] & A[Serializable]

  // not working, doesn't work that way :-<
//  class B extends AA
//  class C extends A[Any] with A[Serializable]
}
