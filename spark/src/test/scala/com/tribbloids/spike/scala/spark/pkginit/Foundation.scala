package com.tribbloids.spike.scala.spark.pkginit

import java.util.concurrent.atomic.AtomicInteger

trait Foundation { // will

  import Foundation.*

//  val dummy1: String = compileStacktrace()
//
//  lazy val dummy2 = compileStacktrace()

  {
    initialisationCounter.incrementAndGet()
  }

  case class Top1() {

//    def _dummy1: String = dummy1
  }

}

object Foundation {

  lazy val initialisationCounter = new AtomicInteger()

  def compileStacktrace(): String = {

    // get stacktrace here
    val tt = Thread.currentThread()

    val stackTrace = tt.getStackTrace

    val body = stackTrace.mkString("\n")

    val result = tt.getId.toString + ":" + tt.getName + "\n" + body

    result
  }
}
