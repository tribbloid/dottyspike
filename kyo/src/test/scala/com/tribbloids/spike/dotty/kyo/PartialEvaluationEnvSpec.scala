package com.tribbloids.spike.dotty.kyo

import org.scalatest.funspec.AnyFunSpec
import kyo.*

class PartialEvaluationEnvSpec extends AnyFunSpec {

  describe("Partial Evaluation with Environments") {

    it("should partially evaluate program by providing environments one at a time") {
      import PartialEvaluationEnv.*

      val result: String = finalResult

      assert(result == "cached-user")
    }

    it("should show type simplification through partial evaluation") {
      import PartialEvaluationEnv.*

      val programWithAll3: String < (Env[Database] & Env[Cache] & Env[Logger]) =
        program

      val programWith2: String < (Env[Cache] & Env[Logger]) =
        withDatabaseProvided

      val programWith1: String < Env[Logger] =
        withDatabaseAndCacheProvided

      val programComplete: String < Any =
        withAllProvided

      assert(programWithAll3.isInstanceOf[String < (Env[Database] & Env[Cache] & Env[Logger])])
      assert(programWith2.isInstanceOf[String < (Env[Cache] & Env[Logger])])
      assert(programWith1.isInstanceOf[String < Env[Logger]])
      assert(programComplete.isInstanceOf[String < Any])
    }
  }
}
