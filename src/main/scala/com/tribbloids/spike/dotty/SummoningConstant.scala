package com.tribbloids.spike.dotty

object SummoningConstant {

  final val v1 = 3

  val w2 = summon[ValueOf[3]]
  implicitly[v1.type =:= w2.value.type]

  object V2

  val v2 = summon[ValueOf[V2.type]]

  // (3: ValueOf[3])

//  def acceptV[T <: Int](v: ValueOf[T]) = 0
//  acceptV(1)
//  acceptV("str")
}
