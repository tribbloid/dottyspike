package com.tribbloids.spike.meta.staging

import org.junit.Assert._
import org.junit.Test

import scala.quoted._
import scala.quoted.staging.{Toolbox, run, withQuoteContext}

class Test1 {

  // Needed to run or show quotes
  given Toolbox = Toolbox.make(getClass.getClassLoader)

  private def code(using QuoteContext) = '{ identity("foo") }

  @Test def t1(): Unit = {
    assertEquals("scala.Predef.identity[java.lang.String](\"foo\")", withQuoteContext(code.show))
    assertEquals("foo", run(code))
  }
}
