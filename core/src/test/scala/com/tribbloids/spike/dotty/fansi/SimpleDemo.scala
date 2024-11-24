package com.tribbloids.spike.dotty.fansi

import org.scalatest.funspec.AnyFunSpec

class SimpleDemo extends AnyFunSpec {

  it("ab") {

    val colored: fansi.Str = fansi.Color.Red("Hello World Ansi!")
    // Or fansi.Str("Hello World Ansi!").overlay(fansi.Color.Red)

    val length = colored.length // Fast and returns the non-colored length of string

    val blueWorld = colored.overlay(fansi.Color.Blue, 6, 11)

    val underlinedWorld = colored.overlay(fansi.Underlined.On, 6, 11)

    val underlinedBlue = blueWorld.overlay(fansi.Underlined.On, 4, 13)
  }
}
