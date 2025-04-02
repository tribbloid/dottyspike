package com.tribbloids.spike.scala.spark.pkginit.pkg2

//package com.tribbloids.spike.scala.spark.pkg2
//
//import ai.acyclic.six.spark.SparkEnvSpec
//
//class TopLevelValInit extends SparkEnvSpec {
//
//  it("fn in top-level declaration") {
//
//    val data = 1 to 10
//
//    val markers = spark.sparkContext
//      .parallelize(data)
//      .map { it =>
//        val top = new Top2
//        top._dummy2
//      }
//      .collect()
//      .toSeq
//
//    assert(markers.distinct.size == 1)
//  }
//}
