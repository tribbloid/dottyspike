package com.tribbloids.spike.dotty

object ValueOfExample {
  // from Scala 2.13 ValueOf doc

  object FromScalaDoc {

    case class Residue[M <: Int](n: Int) extends AnyVal {

      def +(rhs: Residue[M])(
          implicit
          m: ValueOf[M]
      ): Residue[M] =
        Residue((this.n + rhs.n) % valueOf[M])

    }

    val fiveModTen: Residue[10] = Residue[10](5)

    val nineModTen: Residue[10] = Residue[10](9)

    fiveModTen + nineModTen // OK == Residue[10](4)

    val fourModEleven = Residue[11](4)

    //  fiveModTen + fourModEleven
    // compiler error: type mismatch;
    //   found   : Residue[11]
    //   required: Residue[10]
  }

  val v = summon[ValueOf[ValueOfExample.type]] // works

//  val v2 = summon[ValueOfExample.type]
  // doesn't work

  def main(args: Array[String]): Unit = {
    require(v.value == ValueOfExample)
  }

}
