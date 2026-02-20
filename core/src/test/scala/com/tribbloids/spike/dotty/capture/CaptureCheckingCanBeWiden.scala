package com.tribbloids.spike.dotty.capture

import scala.language.experimental.captureChecking

object CaptureCheckingCanBeWiden {

  trait T1 { type R }

  case class T2(x: T1, y: T1)

  def withFile[T](name: String)(x: (T1^, T1^) => T): T = ???

  {
    withFile("data.txt")((x, y) =>
      summon[x.type <:< T1]

      val z = T2(x, y)

      ()
    )
  }
}
