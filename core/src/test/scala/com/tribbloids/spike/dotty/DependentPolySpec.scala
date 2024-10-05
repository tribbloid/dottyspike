package com.tribbloids.spike.dotty

import ai.acyclic.six.typetag.TypeTag
import izumi.reflect.Tag
import izumi.reflect.macrortti.LightTypeTag
import org.scalatest.funspec.AnyFunSpec

class DependentPolySpec extends AnyFunSpec {

  import DependentPoly.*

  it("function") {

    {
      val t1 = summon[Tag[xRaw.type]]
      println(t1)
    }

    {
      val t1 = summon[TypeTag[xRaw.type]]
      println(t1)
    }
  }
}
