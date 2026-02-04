package com.tribbloids.spike.turbolift

import org.scalatest.funsuite.AnyFunSuite
import turbolift.{!!, effects}
import turbolift.effects.{Error, Reader, State}

class FirstExampleTest extends AnyFunSuite {

  test("First Example from Turbolift README") {
    val program: !![Unit, State[Int] & Reader[Int] & Error[String]] = for
      a <- State.get[Int]
      b <- Reader.ask[Int]
      c <- if b != 0 then !!.pure(a / b) else Error.raise(s"Tried to divide $a by zero")
      _ <- State.put(c)
    yield ()

    val result = program
      .handleWith(State.handler(100))
      .handleWith(Reader.handler(3))
      .handleWith(Error.handler)
      .run

    println(result)
    assert(result == Right(((), 33)))
  }

  test("partial evaluation") {

    val program: Unit !! (State[Int] & State[String] & Error[String]) = for
      a <- State.get[Int]
      b <- State.get[String]
      c <- if a != 0 then !!.pure(a + 1) else Error.raise(s"Tried to divide $a by zero")
      _ <- State.put(b + c)
    yield ()
  }
}
