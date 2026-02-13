package ai.acyclic.zio

import org.scalatest.funspec.AnyFunSpec
import _root_.zio._

class ZioAssociativitySpec extends AnyFunSpec {

  describe("ZIO Applicative Associativity") {

    it("should demonstrate zipRight (*>) associativity with different types") {

      val runtime = Runtime.default

      val u: ZIO[Any, Nothing, Int] = ZIO.succeed(1)
      val v: ZIO[Any, Nothing, String] = ZIO.succeed("2")
      val w: ZIO[Any, Nothing, Boolean] = ZIO.succeed(true)

      // Associativity Law for Applicative Functor (as ZipRight)
      // (u *> v) *> w === u *> (v *> w)
      // Both resolve to the result of `w` (true), and carry effects of u, v, w in order.
      // Their types are identical: ZIO[Any, Nothing, Boolean]

      val left: ZIO[Any, Nothing, Boolean] = (u *> v) *> w
      val right: ZIO[Any, Nothing, Boolean] = u *> (v *> w)

      val resultLeft = Unsafe.unsafe { implicit unsafe =>
        runtime.unsafe.run(left).getOrThrowFiberFailure()
      }
      val resultRight = Unsafe.unsafe { implicit unsafe =>
        runtime.unsafe.run(right).getOrThrowFiberFailure()
      }

      assert(resultLeft == resultRight)
      assert(resultLeft == true)
    }

    it("should demonstrate zipLeft (<*) associativity with different types") {

      val runtime = Runtime.default

      val u: ZIO[Any, Nothing, Int] = ZIO.succeed(1)
      val v: ZIO[Any, Nothing, String] = ZIO.succeed("2")
      val w: ZIO[Any, Nothing, Boolean] = ZIO.succeed(true)

      // Associativity Law for Applicative Functor (as ZipLeft)
      // (u <* v) <* w === u <* (v <* w)
      // Both resolve to the result of `u` (1), and carry effects of u, v, w in order.
      // Their types are identical: ZIO[Any, Nothing, Int]

      val left: ZIO[Any, Nothing, Int] = (u <* v) <* w
      val right: ZIO[Any, Nothing, Int] = u <* (v <* w)

      val resultLeft = Unsafe.unsafe { implicit unsafe =>
        runtime.unsafe.run(left).getOrThrowFiberFailure()
      }
      val resultRight = Unsafe.unsafe { implicit unsafe =>
        runtime.unsafe.run(right).getOrThrowFiberFailure()
      }

      assert(resultLeft == resultRight)
      assert(resultLeft == 1)
    }

    it("should demonstrate zip (<*>) is NOT associative in structure due to Zippable behavior") {

      val runtime = Runtime.default

      val u: ZIO[Any, Nothing, Int] = ZIO.succeed(1)
      val v: ZIO[Any, Nothing, String] = ZIO.succeed("2")
      val w: ZIO[Any, Nothing, Boolean] = ZIO.succeed(true)

      // Applicative Functor Law (Associativity) for `zip`
      // In ZIO 2, `zip` (<*>) uses `Zippable` which flattens left-associatively but not right-associatively.
      // (u <*> v) <*> w === (Int, String, Boolean)
      // u <*> (v <*> w) === (Int, (String, Boolean))

      val left: ZIO[Any, Nothing, (Int, String, Boolean)] = (u <*> v) <*> w
      val right: ZIO[Any, Nothing, (Int, (String, Boolean))] = u <*> (v <*> w)

      val resultLeft = Unsafe.unsafe { implicit unsafe =>
        runtime.unsafe.run(left).getOrThrowFiberFailure()
      }
      val resultRight = Unsafe.unsafe { implicit unsafe =>
        runtime.unsafe.run(right).getOrThrowFiberFailure()
      }

      // Values demonstrate structural difference
      assert(resultLeft == (1, "2", true))
      assert(resultRight == (1, ("2", true)))

      // They are structurally different (cannot even be compared directly easily without casting)
      // We assert they are different by their structure above.
    }
  }
}
