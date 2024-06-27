package com.tribbloids.spike.dotty.quoted.autolift

import com.tribbloids.spike.dotty.quoted.autolift.Fixture.testCompiler
import dotty.tools.dotc.core.Contexts.Context
import org.scalatest.funspec.AnyFunSpec

import scala.quoted.runtime.impl.{QuotesImpl, TypeImpl}
import scala.quoted.{Quotes, Type}

class TypeTagSpec extends AnyFunSpec {
  import TypeTagSpec.*

  describe("TypeTag") {

    val tag1 = TypeTag.get[Seq[T1[Int]]]

    it("runtime") {

//      val tp = tag1.unboxRaw
//      println(tp)
    }

    it("... in compiler") {

      testCompiler.run { q =>

        given Quotes = q
        given Context = q.asInstanceOf[QuotesImpl].ctx

        val t1: Type[Seq[T1[Int]]] = tag1.unbox(q)
        val str = t1.asInstanceOf[TypeImpl].typeTree.show

        println(str)

        '{}
      }
    }

//    it("compile-time") {
//
//      val t1 = TypeTag.get[Int].unbox
//      val t2 = summon[TypeTag[Int]].unbox
//
//      assert(t1 == t2)
//    }

//    it("pickler") {
//      import boopickle.Default.generatePickler
//
//      summon[Pickler[Type[Int]]]
//    }
  }
}

object TypeTagSpec {

  trait T1[T]
}
