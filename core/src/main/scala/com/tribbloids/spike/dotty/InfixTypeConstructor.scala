package com.tribbloids.spike.dotty

object InfixTypeConstructor {

  trait Has[T]

  type <>:[X, T] = X & Has[T]

  type <>[X, T] = X & Has[T]

  trait S
  trait T1
  trait T2

  summon[(S <>: T1) =:= (S <> T1)]

  summon[((S <>: T1) <>: T2) =:= (S <> T1 <> T2)]
}
