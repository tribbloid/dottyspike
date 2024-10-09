package com.tribbloids.spike.dotty

object SrcTracing {

  case class Traced[I, O](fn: I => O)(val src: String) extends (I => O) {

    override def apply(v1: I): O = fn(v1)
  }

  def trace[I, O](fn: I => O)(
      using
      file: sourcecode.File,
      line: sourcecode.Line
  ): Traced[I, O] = Traced(fn)(file.toString + ":" + line.toString)
}
