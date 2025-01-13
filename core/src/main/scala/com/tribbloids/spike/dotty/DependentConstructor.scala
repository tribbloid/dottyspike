package com.tribbloids.spike.dotty

object DependentConstructor {

  // these won't work in Scala 2
  class C1[K](key: K)(
      implicit
      lemma: Seq[key.type]
  ) {}

  case class C2[K](key: K)(
      implicit
      lemma: Seq[key.type] // this won't work
  ) {}

  class C3[K](key: K) {

    def this(seq: Seq[K]) = this(seq.headOption.getOrElse(throw new IllegalArgumentException("Seq cannot be empty")))
    def this() = this(Seq.empty[K])
  }

  class C4[K]() extends C3[K]()

  object WithoutPrefix {

    trait Base

    implicit class Ext(v: Base) {

      case class Ops()
    }

    def base1(): Base = ???

    val b1 = base1()
    val o1 = b1.Ops()
  }
}
