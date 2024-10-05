package com.tribbloids.spike.dotty.quoted.autolift

import org.scalatest.funspec.AnyFunSpec

import scala.quoted.Quotes

class FixtureSpec extends AnyFunSpec {

  it("compiler") {
    Fixture.testCompiler.run { q =>
      given Quotes = q

      '{
        Fixture
      }
    }
  }
}
