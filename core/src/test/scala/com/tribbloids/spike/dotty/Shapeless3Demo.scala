package com.tribbloids.spike.dotty

import shapeless3.deriving.{K0, Labelling}

object Shapeless3Demo {

  // Sealed trait with case classes
  sealed trait Shape

  case class Circle(radius: Double) extends Shape

  case class Rectangle(width: Double, height: Double) extends Shape

  // Enum
  enum Color:
    case Red, Green, Blue

  // Define the type class
  trait CaseName[A] {
    def name(a: A): String
  }

  trait CaseName_Imp0 {

    // For any case type in non-enum sum types
    given f3[A](
        using
        labelling: Labelling[A]
    ): CaseName[A] with {
      def name(a: A): String = labelling.label
    }
  }

  object CaseName extends CaseName_Imp0 {
    // For enum sum types
    given f1[A <: Enum[A]]: CaseName[A] with {
      def name(a: A): String = a.name
    }

    // For non-enum sum types
    given f2[A](
        using
        cInst: K0.CoproductInstances[CaseName, A]
    ): CaseName[A] with {
      def name(a: A): String = cInst.fold(a)([a] => (cn: CaseName[a], a: a) => cn.name(a))
    }

  }

  // Demonstrate usage
  val shape: Shape = Circle(1.0)
  val shapeCaseName = summon[CaseName[Shape]].name(shape)
  println(shapeCaseName) // Prints "Circle"

  val color: Color = Color.Green
  val colorCaseName = summon[CaseName[Color]].name(color)
  println(colorCaseName) // Prints "Green"
}
