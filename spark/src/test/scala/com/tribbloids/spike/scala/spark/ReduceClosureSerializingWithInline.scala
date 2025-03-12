package com.tribbloids.spike.scala.spark

import ai.acyclic.six.spark.SparkUnitTest
import ai.acyclic.six.spark.serialization.AssertSerializable

class ReduceClosureSerializingWithInline extends SparkUnitTest {

  val k = "1"

  it("will fail") {
    sc.parallelize(1 to 2)
      .map { v =>
        k + v
      }
      .collect()
  }

  {
    val k = "1"

    it("will succeed") {
      sc.parallelize(1 to 2)
        .map { v =>
          k + v
        }
        .collect()
    }
  }

  import ReduceClosureSerializingWithInline.*

  it("example") {

    val b = new Breaking {}

    val fn = { () =>
      val v = b.v4
      println(v.getClass)
    }

    AssertSerializable(fn).weakly()
  }

  describe("in Spark") {

    val b = new Breaking {}

    it("1") {
      spark.sparkContext
        .parallelize(1 to 10)
        .map { _ =>
          val v = b.v1
          v.getClass
        }
        .count()
    }

    it("2") {
      spark.sparkContext
        .parallelize(1 to 10)
        .map { _ =>
          val v = b.v2
          v.getClass
        }
        .count()
    }

    it("3") {
      spark.sparkContext
        .parallelize(1 to 10)
        .map { _ =>
          val v = b.v3
          v.getClass
        }
        .count()
    }

    it("4") {
      spark.sparkContext
        .parallelize(1 to 10)
        .map { _ =>
          val v = b.v4
          v.getClass
        }
        .count()
    }

    it("5") {
      spark.sparkContext
        .parallelize(1 to 10)
        .map { _ =>
          val v = b.v5
          v.getClass
        }
        .count()
    }
  }

}

object ReduceClosureSerializingWithInline {

  trait T1 extends Serializable

  trait Breaking {

    val v1 = new T1 {}
    lazy val v2 = new T1 {}

    def v3 = new T1 {}
    inline def v4 = new T1 {}

    final val v5 = new T1 {}
  }
}
