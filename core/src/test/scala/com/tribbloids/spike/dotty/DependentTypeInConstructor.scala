package com.tribbloids.spike.dotty

import ai.acyclic.six.testing.Assert
import com.tribbloids.spike.dotty.DependentTypeInConstructor.{B, C}
import org.tribbloid.scaffold.BaseSpec

object DependentTypeInConstructor {

  trait A
  trait B extends A
  trait C extends B

  case class TakeA(v: A)(
      using
      ev: v.type <:< B
  ) {

    type Ctg = v.type match {
      case C => true
      case _ => false
    }

//    println(valueOf[Ctg])
  }

  def takeA(v: A)(
      using
      ev: v.type <:< B
  ): Unit = {

    type Ctg = v.type match {
      case C => true
      case _ => false
    }

//    println(valueOf[Ctg])
  }

  Assert.typeError("takeA(new A)")

  takeA(new B {})

  takeA(new C {})

}

class DependentTypeInConstructor extends BaseSpec {

  import DependentTypeInConstructor.*

  it("test") {

    Assert.typeError("TakeA(new A)")

    TakeA(new B {})

    TakeA(new C {})
  }

  it("test2") {

    Assert.typeError("takeA(new A)")

    takeA(new B {})

    takeA(new C {})
  }

}