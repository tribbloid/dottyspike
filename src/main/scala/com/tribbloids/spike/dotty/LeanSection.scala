package com.tribbloids.spike.dotty

import scala.language.implicitConversions

trait LeanSection {

  //  trait Impl extends Section {
  //
  //    override def toString: String = this.getClass.getSimpleName
  //  }

  implicit def generalise[Self >: this.type, Select[_ <: Self]](peer: Select[Self]): Select[this.type] =
    peer.asInstanceOf[Select[this.type]]
}

object LeanSection {

  //  implicit def memoize[T] TODO: implementing memoization

  class ForAll[T] extends LeanSection {

    trait Proof[R]

    trait ProofInt extends Proof[Int]
  }

  object ForAll {}

//  trait CanGen[Spec, Gen] {}
//
//  object CanGen {
//
//    implicit def usingSection[
//        S <: LeanSection,
//        Choose: (section: S) => Any
//    ](
//        implicit
//        sec: S
//    ): CanGen[Choose[sec.type], Choose[S]] = ???
//  }
//
//  def toProof[S <: ForAll[_]]: (sec: S) => sec.ProofInt = ???

//  val k: Int = toProof

  val f1 = ForAll[Int]
  val f2 = ForAll[Int]

  val k: f1.ProofInt = ???
//  val k2: f2.ProofInt = f2.generalise[ForAll[Int], (sec: ForAll[Int]) =>> sec.ProofInt](k)
  // TODO: compiler says "Not yet implemented: (...) =>> ..."
}
