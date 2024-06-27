package com.tribbloids.spike.dotty

object InlineError {

  trait AlwaysError

  object AlwaysError {

    implicit inline def get(): AlwaysError = {
      compiletime.error("error message!")
    }
  }

  trait AlwaysError2

  object AlwaysError2 {

    transparent inline given get: AlwaysError2 = {
      compiletime.error("error message 2!")
    }
  }

  trait Indirect1

  object Indirect1 {

    given get(
        using
        ev: AlwaysError
    ): Indirect1 = ???
  }

  trait Indirect2

  object Indirect2 {

    given get(
        using
        ev: Indirect1
    ): Indirect2 = ???
  }

  object Test0 {

//        AlwaysError.get() // error message!

//        summon[Indirect1] //
//        summon[Indirect2] //
  }

  trait Test1 {

    given get2(
        using
        ev: AlwaysError2
    ): Indirect1 = ???

//    summon[Indirect1] //error message 2!
  }

  trait Test2_Imp0 {

    given get0(
        using
        ev: AlwaysError2
    ): Indirect1 = ???
  }

  object Test2 extends Test2_Imp0 {

    given get2(
        using
        ev: AlwaysError2
    ): Indirect1 = ???

//    summon[Indirect1] //error message 2!
  }
}
