package com.tribbloids.spike.meta.staging

// Import Expr and some extension methods
import scala.quoted._
import scala.quoted.staging.{run, withQuoteContext, Toolbox}

object Main {

  // Needed to run or show quotes
  given Toolbox = Toolbox.make(getClass.getClassLoader)

  def main(args: Array[String]): Unit = {
    val self = stagedPower(1)
    
    val square = stagedPower(2)
    println("3^2 = " + square(3))
    println("4.1^2 = " + square(4.1))
    println()
    val cube = stagedPower(3)
    println("2.4^3 = " + cube(2.4))
    println()

    val toTheFourth = stagedPower(4)
    println("3^4 = " + toTheFourth(3))
    println()

    val toThe5 = stagedPower(5)
    println("3^5 = " + toThe5(3))
    println()
  }

  def stagedPower(n: Int): Double => Double = {
    // Code representing the labmda where the recursion is unrolled based on the value of n
    def code(using QuoteContext): Expr[Double => Double] = '{ (x: Double) => ${ powerCode(n, 'x) } }

    println(s"staged power for n=" + n + ":")

    val _code = withQuoteContext{
      val _code: Expr[Double => Double] = code
      println(_code.show)
      _code
    }
    
    // Evaluate the contents of the co/*  */de and return it's value
    run(_code)
  }

  def powerCode(n: Int, x: Expr[Double])(using ctx: QuoteContext): Expr[Double] = {
    val result = if (n == 0) Expr(1.0) // Expr() lifts 1.0 to '{1.0}
    else if (n == 1) x // optimization to not generate x * 1
    else if (n < 0) throw new Exception("Negative powers not implemented. Left as a small exercise. Dont be shy, try it out.")
    else if (n == 2) {
      '{ $x * $x }
    } // optimization to not generate { val y = x; y * y }
    else if (n % 2 == 1) {
      '{ $x * ${ powerCode(n - 1, x) } }
    }
    else {
      '{ val y = $x * $x; ${ powerCode(n / 2, 'y) } }
    }

    result
  }
}
