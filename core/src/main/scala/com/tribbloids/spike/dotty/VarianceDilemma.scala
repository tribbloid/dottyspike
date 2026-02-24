package com.tribbloids.spike.dotty

object VarianceDilemma {

  trait SS[T]

  trait SS1[+T] extends SS[T]
  trait SS2[-T] extends SS[T]

}
