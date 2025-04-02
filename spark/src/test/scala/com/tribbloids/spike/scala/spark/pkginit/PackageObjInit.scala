package com.tribbloids.spike.scala.spark.pkginit

import ai.acyclic.six.spark.SparkEnvSpec

class PackageObjInit extends SparkEnvSpec {

  it("for object") {

    Foundation.initialisationCounter.set(0)

    val top = Obj1.Top1()
    assert(Foundation.initialisationCounter.intValue() == 1)
  }

  it("for package object inheriting a trait") {

    Foundation.initialisationCounter.set(0)

    val top = pkg1.Top1()
    assert(Foundation.initialisationCounter.intValue() == 1)
  }

  it("for self-contained package object") {

    Foundation.initialisationCounter.set(0)

    val top = pkg2.Top1()
    assert(Foundation.initialisationCounter.intValue() == 1)
  }

//  it("fn in package") {
//
//    val d ata = 1 to 10
//
//    val markers = spark.sparkContext
//      .parallelize(data)
//      .map { it =>
//
//        val top = new Top1
////        top._dummy1
//        dummy1
//      }
//      .collect()
//      .toSeq
//
//    assert(markers.distinct.size == 1)
//  }
}
