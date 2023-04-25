package com.tribbloids.spike.dotty.hypothesis

import com.tribbloids.spike.dotty.hypothesis.ThisTypeIsSpecial.Case2.{S1, S2}

// https://stackoverflow.com/questions/76104573/in-scala-3-with-dot-calculus-is-this-type-a-path-dependent-type-what-makes-i
object ThisTypeIsSpecial {

  object Case1 {

    trait Sub {
      type EE
    }

    trait S1 extends Sub { type EE = Product }
    trait S2 extends Sub { type EE = Tuple }

    trait A1 extends S1

    trait A2 extends A1 with S2
  }

  object Case2 {

    trait Supe {

      type E

      trait Sub {

        type EE = Supe.this.E
      }
    }

    object S1 extends Supe {
      type E = Product
    }
    object S2 extends Supe {
      type E = Tuple
    }

    trait A1 extends S1.Sub

    trait A2 extends A1 with S2.Sub
  }
}
