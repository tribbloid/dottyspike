package com.tribbloids.spike.dotty

object TransitiveConversion {

  import scala.language.implicitConversions

  class A(val value: Int)

  class B(val a: A)

  class C(val b: B)

  trait Conv[A, B] extends Conversion[A, B]

  given Conv[A, B] with
    def apply(a: A): B = new B(a)

  given Conv[B, C] with
    def apply(b: B): C = new C(b)

//  given Conversion[A, C] with
//    def apply(a: A): C = summon[Conversion[B, C]](summon[Conversion[A, B]](a))

  object ConvUtils {
    given hypotheticalSyllogism[A, B, C](
        using
        ab: Conv[A, B],
        bc: Conv[B, C]
    ): Conv[A, C] =
      new Conv[A, C] {
        def apply(a: A): C = bc(ab(a))
      }
  }

  import ConvUtils.given

  @main def demo: Unit =
    val a = new A(42)
    val c: C = a // This works due to the transitive-like conversion
    println(c.b.a.value) // Outputs: 42
}
