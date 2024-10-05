package com.tribbloids.spike.dotty

object TrueTypeAlias {

  trait Unaliased[+T] {

    def take1: List[Seq[(T, T)]]

    def take2: List[Seq[(T, T)]]

    def take3: List[Seq[(T, T)]]
  }

  trait Aliased[+T] {

    opaque type TakeAlias = List[Seq[(T, T)]]

    def take1: TakeAlias

    def take2: TakeAlias

    def take3: TakeAlias
  }

  trait Aliased2[+T] extends Aliased[T] {

    def take4: TakeAlias
  }
}
