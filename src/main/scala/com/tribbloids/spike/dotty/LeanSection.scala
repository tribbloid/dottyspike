package com.tribbloids.spike.dotty

import scala.language.implicitConversions

object LeanSection {

  /**
    * intend to mimic the `section` keyword of LEAN 4 & assuming the lambdaP2 vs CoC conjecture, this trait only serves
    * to group shared type arguments of its dependent types.
    *
    * e.g. `T |- R` and T |-\-R` can be grouped into `ForAll[T] { |-[R]; |-\-[R] }`, where `ForAll extends
    * Section[ForAll]`
    *
    * Summoned section instances are automatically memoized by type argument. Dependent types of section instances can
    * be freely cast into each other if the section instances are of the same type
    */
  trait Section[Self <: Section[Self]] {

    implicit def convertPeer(peer: Self): this.type = peer.asInstanceOf[this.type]
  }

  object Section {

    implicit def allPeersAreEqual[
        Self <: Section[Self],
        S1 <: Self & Singleton,
        S2 <: Self & Singleton
    ]: (S1 =:= S2) =
      ???
  }

  object SectionSpec {

    class Gen[T] extends Section[Gen[T]] {

      trait Fn[R]
    }

    object genInt1 extends Gen[Int] {}
    object genInt2 extends Gen[Int] {}
  }

  class SectionSpec {

    import SectionSpec._

    val t1: genInt1.Fn[String] = ???
    val t2: Gen[Int]#Fn[String] = t1

    summon[genInt1.type =:= genInt2.type]

//    summon[genInt1.Fn[String] =:= genInt2.Fn[String]]
// doesn't work, see stack overflow problem
  }
}
