package com.tribbloids.spike.dotty.kyo

import org.scalatest.funspec.AnyFunSpec
import kyo.*

class ApplicativeSyntaxSpec extends AnyFunSpec {

  describe("Applicative syntax in Kyo") {

    it("should support <*> or similar") {
      import AllowUnsafe.embrace.danger
      // Trying to verify if <*> is supported directly or via extension methods

      val v1: Int < IO = IO(1)
      val v2: Int < IO = IO(2)

      // Custom extension method to support <*>
      extension [A, S1](v1: A < S1) def <*>[B, S2](v2: B < S2): (A, B) < (S1 & S2) = Kyo.zip(v1, v2)

      val combined = (v1 <*> v2).map { case (a, b) => a + b }

      // Kyo typically uses zip for applicative composition
      val combinedZip = Kyo.zip(v1, v2).map { case (a, b) => a + b }

      assert(Abort.run(IO.Unsafe.run(combinedZip)).eval.getOrElse(0) == 3)
      assert(Abort.run(IO.Unsafe.run(combined)).eval.getOrElse(0) == 3)
    }
  }
}
