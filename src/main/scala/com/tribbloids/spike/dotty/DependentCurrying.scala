package com.tribbloids.spike.dotty

object DependentCurrying {

  trait Outer {

    type Inner
  }

  case class Traverse[N](o: Outer)(
      doIt: o.Inner => o.Inner
  ) {}
}
