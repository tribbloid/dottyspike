package com.tribbloids.spike.dotty

import org.scalatest.funspec.AnyFunSpec

class TrackedErasureSpec extends AnyFunSpec {

  describe("Tracked parameters") {
    it("should preserve type information without Aux pattern") {
      TrackedErasureDemo.test()
    }
  }
}
