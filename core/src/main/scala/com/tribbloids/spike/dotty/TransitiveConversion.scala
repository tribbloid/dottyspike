package com.tribbloids.spike.dotty

object TransitiveConversion {

  import scala.language.implicitConversions

  object NewStyle {

    class A(val value: Int)

    class B(val a: A)

    class C(val b: B)

    trait Conv[A, B] extends Conversion[A, B]

    given Conv[A, B] = { (a: A) => new B(a) }

    given Conv[B, C] = { (b: B) => new C(b) }

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

  object AlmostNewStyle {

    class A(val value: Int)

    class B(val a: A)

    class C(val b: B)

    trait Conv[A, B] extends Conversion[A, B]

    given ab: Conv[A, B] = { (a: A) => new B(a) }

    given bc: Conv[B, C] = { (b: B) => new C(b) }

    object ConvUtils {
      given hypotheticalSyllogism[A, B, C](
          using
          ab: Conv[A, B],
          bc: Conv[B, C]
      ): Conv[A, C] = {

        new Conv[A, C] {
          def apply(a: A): C = bc(ab(a))
        }
      }
    }
    import ConvUtils.given

    def demo(): Unit = {
      val a = new A(42)
      val c: C = a
      println(c.b.a.value) // Outputs: 42
    }
  }

  object OldStyle {

    class A(val value: Int)

    class B(val a: A)

    class C(val b: B)

    trait Conv[A, B] extends Conversion[A, B]

    given ab: Conv[A, B] = { (a: A) => new B(a) }

    given bc: Conv[B, C] = { (b: B) => new C(b) }

    object ConvUtils {
      implicit def hypotheticalSyllogism[A, B, C](
          using
          ab: Conv[A, B],
          bc: Conv[B, C]
      ): Conv[A, C] = {

        new Conv[A, C] {
          def apply(a: A): C = bc(ab(a))
        }
      }
    }

//    def demo(): Unit = {
//      val a = new A(42)
//      val c: C = a // breaks
//      println(c.b.a.value) // Outputs: 42
//    }
  }

}
