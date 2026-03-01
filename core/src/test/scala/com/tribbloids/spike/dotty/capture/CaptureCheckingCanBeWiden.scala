package com.tribbloids.spike.dotty.capture

import scala.language.experimental.captureChecking

object CaptureCheckingCanBeWiden {

  trait T1 { type R }

  def withFile[T](name: String)(x: (T1^, T1^) -> T): T = ???

  {
    withFile("data.txt")((c1, c2) =>
      val w = 3

      val x2: T1^{c1} = c1 // no cast
      val x2_ : T1^ = c1 // ditto, but shorter
      val x3: T1^{c1, c2} = c1 // widening
//      val x1: T1 = c1 //  unsafe downcast, breaks compilation
//      val x4: T1^{c2} = c1 // not a supertype or subtype, breaks compilation
      ()
    )
  }
}
