package com.tribbloids.spike.dotty

import scala.compiletime.ops.int.+
import scala.compiletime.{constValue, error}

object DependentEquality {

  trait AA {

    lazy val ii: Int
  }

  object Impl {
    // from Claude 3.5

    // Type-level function to compare singleton types
    type =!=[A, B] <: Boolean = A match
      case B => false
      case _ => true

    // Usage
    @main def test: Unit =
      // These lines will compile
      println(proveSingletonEquality[Foo.type, Foo.type]) // true
      println(proveSingletonEquality[Bar.type, Bar.type]) // true
      //  println(proveSingletonEquality[Bar.type, Foo.type]) // true
//      println(proveSingletonEquality[KK, KK]) // true

    // Compile-time singleton equality check
    inline def proveSingletonEquality[A <: Singleton, B <: Singleton](
        using
        Proof[A =!= B]
    ): Boolean =
      inline if constValue[A =!= B] then false
      else
        summon[Proof[A =!= B]] // This will fail to compile if A and B are not the same type
        true

    // Proof of equality
    class Proof[A]

    class KK

    // Define two singleton objects
    object Foo

    object Bar

    object Proof:
      given [A]: Proof[A =!= A] = new Proof[false]

    // This line will not compile
    // println(proveSingletonEquality[Foo.type, Bar.type])
  }

  object Assuming {

    trait Foo {

      type S1
    }

    object Obj1 {

      val v: Foo = ???
      val v1: v.type = v
      val v2: v1.type = v1

      summon[v.S1 =:= v.S1]
//      summon[Eq[v.S1, v.S1]]

      // all the following failed
      summon[v.S1 =:= v1.S1]
      summon[v1.S1 =:= v2.S1]

      def fn1(v: Foo): Unit = {
        val v1: v.type = v
        val v2: v1.type = v1

        summon[v1.S1 =:= v2.S1]
      }
    }
  }

  object Ops {

    summon[1 + 1 =:= 2]
  }
}
