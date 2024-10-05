package com.tribbloids.spike.dotty

object LimitedTypeProjection {

  trait I

  trait Entity {

    type Key

    type Key2 <: I
  }

//  type Dictionary[T <: Entity] = Map[T#Key, T] // doesn't work lol
  type Dictionary2 = Map[Entity#Key, Entity] // work lol
  type Dictionary3 = Map[Entity#Key2, Entity] // work lol
}
