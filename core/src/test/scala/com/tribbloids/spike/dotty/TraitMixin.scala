package com.tribbloids.spike.dotty

object TraitMixin {

  trait T1[X]

  val x = Int.asInstanceOf[Int & T1[1]]

  def mixinT2[V](v: V) = v.asInstanceOf[V & T1[2]]

  val xx = mixinT2(x)

  (xx: Int & T1[1] & T1[2]) // this is possible, but extension won't work
}
