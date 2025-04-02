package com.tribbloids.spike.scala.spark.pkginit

import com.tribbloids.spike.scala.spark.pkginit.Foundation.initialisationCounter

package object pkg2 {

  {
    initialisationCounter.incrementAndGet()
  }

  case class Top1() {}
}
