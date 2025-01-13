package com.tribbloids.spike.dotty

import scala.reflect.Typeable

object UnapplyTypeArg {

  def mapType(k: Typeable[List[?]], v: Typeable[List[?]]) = {

    (k, v) match {

      case (k: Typeable[List[a]], v: Typeable[List[b]]) =>
        given kk: Typeable[List[a]] = k
        given vv: Typeable[List[b]] = v
        summon[Typeable[Map[a, b]]]
    }
  }

}
