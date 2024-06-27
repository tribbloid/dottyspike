package com.tribbloids.spike.dotty

object ChainedVariance {

  trait AA[+X[+n], +Y] {

    def x: X[Y]
  }
}
