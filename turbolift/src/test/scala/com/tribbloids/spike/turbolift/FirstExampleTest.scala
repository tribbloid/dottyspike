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

  test("partial evaluation 1") {

    val program: Unit !! (State[Int] & State[String]) = for
      a <- State.get[Int]
      b <- State.get[String]
      c <- {

        !!.pure {
          println("c evaluated")
          a + 1
        }
      }
      _ <- State.put(b + c)
    yield ()

    val e1 = program.handleWith(State.handler(100))

    val e2 = e1
      .handleWith(State.handler("str"))
      .handleWith(Error.handler)

    val r1 = e2.run

    println(r1)
    assert(r1 == Right((((), 201), "str")))

  }

  test("partial evaluation 2") {

    val program: Unit !! (State[Int]) = for
      a <- {
        println("got a")
        State.get[Int]
      }
      c <- {
        !!.pure {
          println("got c")
          a + 1
        }
      }
      _ <- State.put(a + c)
    yield ()

    val e1 = program.handleWith(State.handler(100))

    val e2 = e1
      .handleWith(Error.handler)

    val r1 = e2.run

    println(r1)
  }
}
