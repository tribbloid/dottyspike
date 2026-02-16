package com.tribbloids.spike.dotty.kyo

import kyo.*
import org.scalatest.funspec.AnyFunSpec

class PartialEvaluationEnv extends AnyFunSpec {

  describe("random example") {

    trait Database {
      def query(sql: String): String < Any
    }

    trait Cache {
      def get(key: String): Option[String] < Any
    }

    trait Logger {
      def log(msg: String): Unit < Any
    }

    val db = new Database {
      def query(sql: String): String < Any = s"Result of: $sql"
    }

    val cache = new Cache {
      def get(key: String): Option[String] < Any =
        if (key == "user:1") Some("cached-user") else None
    }

    val logger = new Logger {
      def log(msg: String): Unit < Any = ()
    }

    val program: String < (Env[Database] & Env[Cache] & Env[Logger]) = {
      for {
        dbInstance <- Env.get[Database]
        cacheInstance <- Env.get[Cache]
        loggerInstance <- Env.get[Logger]
        _ <- loggerInstance.log("Starting query")
        cached <- cacheInstance.get("user:1")
        result <- cached match {
          case Some(value) =>
            loggerInstance.log("Cache hit").map(_ => value)
          case None =>
            for {
              _ <- loggerInstance.log("Cache miss, querying database")
              data <- dbInstance.query("SELECT * FROM users WHERE id = 1")
              _ <- loggerInstance.log("Database query complete")
            } yield data
        }
        _ <- loggerInstance.log(s"Final result: $result")
      } yield result
    }

    val withDatabaseProvided: String < (Env[Cache] & Env[Logger]) =
      Env.run(db)(program)

    val withDatabaseAndCacheProvided: String < Env[Logger] =
      Env.run(cache)(withDatabaseProvided)

    val withAllProvided: String < Any =
      Env.run(logger)(withDatabaseAndCacheProvided)

    val finalResult: String =
      withAllProvided.eval

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

  describe("using opaque types") {

    object Types {
      opaque type UserId = String
      opaque type Email = String
      opaque type Token = String

      object UserId {
        def apply(s: String): UserId = s
        extension (id: UserId) def value: String = id
      }

      object Email {
        def apply(s: String): Email = s
        extension (e: Email) def value: String = e
      }

      object Token {
        def apply(s: String): Token = s
        extension (t: Token) def value: String = t
      }
    }

    import Types.*

    val userIdEnv: UserId = UserId("user-123")
    val emailEnv: Email = Email("user@example.com")
    val tokenEnv: Token = Token("secret-token-456")

    val opaqueProgram: String < (Env[UserId] & Env[Email] & Env[Token]) = {
      for {
        userId <- Env.get[UserId]
        email <- Env.get[Email]
        token <- Env.get[Token]
      } yield s"User: ${userId.value}, Email: ${email.value}, Token: ${token.value}"
    }

    val withUserIdProvided: String < (Env[Email] & Env[Token]) =
      Env.run(userIdEnv)(opaqueProgram)

    val withUserIdAndEmailProvided: String < Env[Token] =
      Env.run(emailEnv)(withUserIdProvided)

    val withAllOpaqueProvided: String < Any =
      Env.run(tokenEnv)(withUserIdAndEmailProvided)

    val opaqueResult: String = withAllOpaqueProvided.eval

    it("should work with opaque types as environments") {
      val result: String = opaqueResult

      assert(result == "User: user-123, Email: user@example.com, Token: secret-token-456")
    }

    it("should show partial evaluation with opaque types") {
      val programWithAll3: String < (Env[UserId] & Env[Email] & Env[Token]) =
        opaqueProgram

      val programWith2: String < (Env[Email] & Env[Token]) =
        withUserIdProvided

      val programWith1: String < Env[Token] =
        withUserIdAndEmailProvided

      val programComplete: String < Any =
        withAllOpaqueProvided

      assert(programWithAll3.isInstanceOf[String < (Env[UserId] & Env[Email] & Env[Token])])
      assert(programWith2.isInstanceOf[String < (Env[Email] & Env[Token])])
      assert(programWith1.isInstanceOf[String < Env[Token]])
      assert(programComplete.isInstanceOf[String < Any])
    }

    it("should partially evaluate when only Email is provided") {
      val programWithAll3: String < (Env[UserId] & Env[Email] & Env[Token]) =
        opaqueProgram

      val withOnlyEmailProvided: String < (Env[UserId] & Env[Token]) =
        Env.run(emailEnv)(programWithAll3)

      assert(withOnlyEmailProvided.isInstanceOf[String < (Env[UserId] & Env[Token])])
    }
  }
}

object PartialEvaluationEnv {}
