package com.tribbloids.spike.dotty

import org.scalatest.funspec.AnyFunSpec

class MatchMixin extends AnyFunSpec {

  it("sanity") {
    trait Mixin

    {
      val k: Option[Int] & Mixin = Some(1).asInstanceOf[Option[Int] & Mixin]

      k match {
        case Some(1) =>
      }
    }

    {
      val k: Either[Int, String] & Mixin = Left(1).asInstanceOf[Left[Int, String] & Mixin]

      k match {
        case Left(1) =>
      }
    }

  }
}
