package com.tribbloids.spike.dotty

object TypeHoleInConversionFromFunction {

  trait T1[T, R]

  given[T, R]: Conversion[T => R, T1[T, R]] = {
    fn =>
      new T1 {}
  }

  def require(fn: Int => String) = {
    ???
  }

  require{v => v.toString} // this won't work in Scala 2
}
