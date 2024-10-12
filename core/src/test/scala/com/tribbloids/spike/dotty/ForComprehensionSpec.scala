package com.tribbloids.spike.dotty

import org.scalatest.funspec.AnyFunSpec

class ForComprehensionSpec extends AnyFunSpec {

  it("example 1") {

    val srcX = ForComprehension.ID[Int]("x");
    val srcY = ForComprehension.ID[Int]("y");

//    val src = List(1, 2)

    {
      val r1 = for {
        a <- srcX
        b = a * 2
      } yield {
        a + b
      }
      pprint.pprintln(r1)
    }

    {
      val r1 = for {
        a <- srcX
        c <- srcY
      } yield {
        a.value + c
      }
      pprint.pprintln(r1)
    }

//    val r2 = src
//      .map { a =>
//        val b = a
//        (a, b)
//      }
//      .map {
//        case (a, b) =>
//          a + b
//      }
//    pprint.pprintln(r2)
  }

  it("example 2") {
    // cascading with flatMap, doesn't work
    // first version probably shouldn't define flatMap

    val s1 = ForComprehension.ID[Int]("x");
    val s2 = ForComprehension.ID[Int]("y");

    val r1 = for (
      a <- s1;
      b <- s2
    ) yield a.value + b

    pprint.pprintln(r1)

  }

  it("example 3") {
    // cartesian arg, the only option that works

    val s1 = ForComprehension.ID[Int]("x");
    val s2 = ForComprehension.ID[Int]("y");

    val r1 = for (case (a, b) <- (s1, s2)) yield {
      a + b
    }

    pprint.pprintln(r1)
  }

  it("example 4") {
    // currying, doesn't work

    val s1 = ForComprehension.ID[Int]("x");
    val s2 = ForComprehension.ID[Int]("y");

    val r1 = for (a <- s1) yield {

      val t2 = for (b <- s2) yield {
        a + b
      }

      t2
    }

    pprint.pprintln(r1)
  }
}
