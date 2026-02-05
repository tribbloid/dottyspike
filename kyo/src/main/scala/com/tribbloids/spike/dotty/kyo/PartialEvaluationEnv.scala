package com.tribbloids.spike.dotty.kyo

import kyo.*

object PartialEvaluationEnv {

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

  val program
      : String < (Env[Database] & Env[Cache] & Env[Logger]) = {
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

  val withDatabaseProvided
      : String < (Env[Cache] & Env[Logger]) =
    Env.run(db)(program)

  val withDatabaseAndCacheProvided: String < Env[Logger] =
    Env.run(cache)(withDatabaseProvided)

  val withAllProvided: String < Any =
    Env.run(logger)(withDatabaseAndCacheProvided)

  val finalResult: String =
    withAllProvided.eval
}
