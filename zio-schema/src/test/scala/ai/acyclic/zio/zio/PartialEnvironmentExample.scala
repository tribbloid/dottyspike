package ai.acyclic.zio.zio

import zio.{Console, Task, UIO, ZEnvironment, ZIO, ZLayer}

/**
 * Demonstrates partial environment provision in ZIO.
 * When only part of the required environments is provided, ZIO simplifies
 * the effect type by removing the provided dependencies from the requirement.
 */
class PartialEnvironmentExample {

  // Define service traits
  trait DatabaseService {
    def query(sql: String): Task[List[String]]
  }

  trait CacheService {
    def get(key: String): Task[Option[String]]
    def put(key: String, value: String): Task[Unit]
  }

  trait LoggerService {
    def log(message: String): Task[Unit]
  }

  // Type aliases for environment composition
  type DbEnv = DatabaseService
  type CacheEnv = CacheService
  type LogEnv = LoggerService
  type FullEnv = DbEnv & CacheEnv & LogEnv

  /**
   * An effect requiring all three environments
   */
  def fetchDataWithCaching(userId: String): ZIO[FullEnv, Throwable, String] =
    for {
      cache <- ZIO.service[CacheService]
      cached <- cache.get(s"user:$userId")
      result <- cached match {
        case Some(data) =>
          ZIO.serviceWithZIO[LoggerService](_.log(s"Cache hit for user $userId")) *>
            ZIO.succeed(data)
        case None =>
          for {
            db <- ZIO.service[DatabaseService]
            logger <- ZIO.service[LoggerService]
            _ <- logger.log(s"Cache miss for user $userId, querying database")
            data <- db.query(s"SELECT * FROM users WHERE id = '$userId'")
            result = data.headOption.getOrElse("")
            _ <- cache.put(s"user:$userId", result)
          } yield result
      }
    } yield result

  /**
   * PARTIAL EVALUATION EXAMPLE 1:
   * Provide only Logger and Cache, leaving Database requirement
   */
  def partialProvideExample(): Unit = {
    val loggerLayer: ZLayer[Any, Nothing, LoggerService] =
      ZLayer.succeed(new LoggerService {
        def log(message: String): Task[Unit] =
          Console.printLine(s"[LOG] $message").orDie
      })

    val cacheLayer: ZLayer[Any, Nothing, CacheService] =
      ZLayer.succeed(new CacheService {
        private var store = Map.empty[String, String]
        def get(key: String): Task[Option[String]] =
          ZIO.succeed(store.get(key))
        def put(key: String, value: String): Task[Unit] =
          ZIO.succeed { store = store + (key -> value) }
      })

    val fullEffect: ZIO[FullEnv, Throwable, String] =
      fetchDataWithCaching("123")

    val simplifiedEffect: ZIO[DbEnv, Throwable, String] =
      fullEffect.provideSome[DbEnv](loggerLayer, cacheLayer)

    println("Partial provide example:")
    println(s"  Original requires: DbEnv & CacheEnv & LogEnv")
    println(s"  After providing Logger + Cache, simplified to: DbEnv")
    println(s"  Type verified at compile time: simplifiedEffect requires only DbEnv")
  }

  /**
   * PARTIAL EVALUATION EXAMPLE 2:
   * Progressive layer building showing type-level simplification
   */
  def progressiveLayerBuilding(): Unit = {
    val dbImpl: DatabaseService = new DatabaseService {
      def query(sql: String): Task[List[String]] =
        ZIO.succeed(List("user1", "user2"))
    }

    val cacheImpl: CacheService = new CacheService {
      def get(key: String): Task[Option[String]] = ZIO.succeed(None)
      def put(key: String, value: String): Task[Unit] = ZIO.unit
    }

    val loggerImpl: LoggerService = new LoggerService {
      def log(message: String): Task[Unit] = ZIO.unit
    }

    val step1: ZIO[DbEnv & CacheEnv & LogEnv, Throwable, String] =
      fetchDataWithCaching("456")

    val step2: ZIO[DbEnv & CacheEnv, Throwable, String] =
      step1.provideSome[DbEnv & CacheEnv](ZLayer.succeed(loggerImpl))

    val step3: ZIO[DbEnv, Throwable, String] =
      step2.provideSome[DbEnv](ZLayer.succeed(cacheImpl))

    val step4: UIO[String] =
      step3.provideSome[Any](ZLayer.succeed(dbImpl)).orDie

    println("\nProgressive layer building:")
    println(s"  Step 1 (original): ZIO[DbEnv & CacheEnv & LogEnv, Throwable, String]")
    println(s"  Step 2 (after Log): ZIO[DbEnv & CacheEnv, Throwable, String]")
    println(s"  Step 3 (after Cache): ZIO[DbEnv, Throwable, String]")
    println(s"  Step 4 (after Db): UIO[String]")
    println(s"  Each step is a compile-time verified simplification!")
  }

  /**
   * PARTIAL EVALUATION EXAMPLE 3:
   * Using provideSomeEnvironment for runtime simplification
   */
  def provideSomeEnvironmentExample(): Unit = {
    val fullEffect: ZIO[DbEnv & CacheEnv & LogEnv, Throwable, String] =
      fetchDataWithCaching("789")

    val partialEnv = ZEnvironment.empty
      .add[CacheService](new CacheService {
        def get(key: String): Task[Option[String]] = ZIO.succeed(Some("cached"))
        def put(key: String, value: String): Task[Unit] = ZIO.unit
      })
      .add[LoggerService](new LoggerService {
        def log(message: String): Task[Unit] = Console.printLine(message).orDie
      })

    val simplified: ZIO[DbEnv, Throwable, String] =
      fullEffect.provideSomeEnvironment[DbEnv](_ ++ partialEnv)

    println("\nprovideSomeEnvironment example:")
    println(s"  Built partial environment with Cache + Logger")
    println(s"  Result type: ZIO[DbEnv, Throwable, String]")
    println(s"  Only DatabaseService remains to be provided")
  }
}
