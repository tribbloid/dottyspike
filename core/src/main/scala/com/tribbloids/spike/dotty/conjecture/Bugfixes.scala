package com.tribbloids.spike.dotty.conjecture

import ai.acyclic.six.verification.Verify

object Bugfixes {

  import compiletime.testing.*

  object `ExtensionalEquality F[X] vs F[_ <: X] For Covariant F` {

    { // Primary
      trait F[+T]

      summon[F[Product] <:< F[? <: Product]]

//      summon[F[? <: Product] <:< F[Product]] // oops
//      summon[F[Product] =:= F[? <: Product]] // oops

      Verify.typeError( // Not widely used as string cannot be refactored regardless
        """
      summon[F[? <: Product] <:< F[Product]] // oops
      summon[F[Product] =:= F[? <: Product]] // oops
        """
      )

//      summon[F[? <: Product] <:< F[Product]] // oops
//      summon[F[Product] =:= F[? <: Product]] // oops
    }

    { // Dual
      trait F_* {
        type TT
      }

      trait TGen {

        type T_/\
        type T_\/ <: T_/\
        type Mat = F_* { type TT <: T_/\ }
      }

      object ProductGen extends TGen {
        override type T_/\ = Product
        override type T_\/ = Product
      }

      object LessThanProductGen extends TGen {
        override type T_/\ = Product
        override type T_\/ = Nothing

      }

      summon[ProductGen.Mat =:= LessThanProductGen.Mat]
    }

    { // Dual, non-purist, can use type constructor

      trait F_* {
        type TT
      }

      trait TGen[/\, \/ <: /\] {

        type Mat = F_* { type TT <: /\ }
      }

      object ProductGen extends TGen[Product, Product] {}

      object LessThanProductGen extends TGen[Product, Nothing] {}

      summon[ProductGen.Mat =:= LessThanProductGen.Mat]
    }
  }

  object `ExtensionalEquality F[X] vs F[_ >: X] For Contravariant F` {

    { // Primary
      trait F[-T]

      summon[F[Product] <:< F[? >: Product]]

      //      summon[F[_ >: Product] <:< F[Product]] // oops
      //      summon[F[Product] =:= F[_ >: Product]] // oops
    }

    { // Dual
      trait F_* {
        type TT
      }

      trait TGen {

        type T_/\
        type T_\/ <: T_/\
        type F = F_* { type TT >: T_\/ }
      }

      object ProductGen extends TGen {
        override type T_/\ = Product
        override type T_\/ = Product
      }

      object MoreThanProductGen extends TGen {
        override type T_/\ = Any
        override type T_\/ = Product

      }

      summon[ProductGen.F =:= MoreThanProductGen.F]
    }
  }

  object MatchTypeSimple {

    { // primary
      type Ext[S] = S match {
        case Float => Double
        case Int => Long
        case _ => S
      }

      summon[Ext[Int] =:= Long]
    }

    { // dual, type classes are implicit terms
      trait SGen {
        type S
        type Ext
      }
      type GenAux[_S] = SGen { type S = _S }

      trait Gen_Fallback1 {
        given [_S]: GenAux[_S] = new SGen {
          final override type S = _S
          final override type Ext = _S
        }
      }

      trait Gen_Fallback0 extends Gen_Fallback1 {

        implicit object IntGen extends SGen {
          final override type S = Int
          final override type Ext = Long
        }
      }

      object SGen extends Gen_Fallback0 {

        implicit object FloatGen extends SGen {
          final override type S = Float
          final override type Ext = Double
        }
      }

      val gen = summon[GenAux[Int]]
      summon[gen.Ext =:= Long]
    }

    { // dual, type classes are outer objects

      trait SGen {
        type S
        type Ext
      }

      object FloatGen extends SGen {
        override type S = Float
        type Ext = Double
      }
      object IntGen extends SGen {
        override type S = Int
        type Ext = Long
      }
      trait FallbackGen extends SGen {
        type Ext = S
      }

      summon[IntGen.Ext =:= Long]
    }
  }

  object MatchTypeCovariantArg {

    { // primary
      trait Vec[+T]
      trait VecD extends Vec[Double] {}

      type Ext[S <: Vec[?]] = S match {
        case Vec[t] => t
      }

      implicitly[Ext[Vec[Int]] =:= Int]
      implicitly[Ext[Vec[Int]] <:< Int]
      // broken in Scala 3.3: Cannot prove that e.Ext[Seq[Int]] =:= Int
      // works in Scala 3.4+

      implicitly[Ext[VecD] =:= Double]
      implicitly[Int <:< Ext[Vec[Int]]]
    }

    { // dual, type classes are implicit terms
      trait Vec[+T]
      trait VecD extends Vec[Double] {}

      trait SGen {
        type SS
        type Ext
      }
      type GenAux[S] = SGen { type SS = S }

      object SGen {

        //        class VecGen extends SGen {}

        implicit def forVec[T, S <: Vec[T]]: SGen { type Ext = T; type SS = S } = new SGen {
          type Ext = T
          type SS = S
        }
      }

      val gen = summon[GenAux[Vec[Int]]]
      summon[gen.Ext =:= Int] // success!

      val gen2 = summon[GenAux[VecD]]
      summon[gen2.Ext =:= Double] // success!
    }

    { // dual, type classes are outer objects
      // TODO: still lack VecD implementation

      trait Vec_* { type TT }
      type VecLt[T] = Vec_* { type TT <: T } // This is unnecessary if match type can extract bound directly

      trait TGen {
        type T_/\
        type Vec = Vec_* { type TT <: T_/\ }
      }

      object IntGen extends TGen {
        type T_/\ = Int
      }

      trait SGen {
        type S <: Vec_*
        type Ext
      }

      trait VecGen extends SGen {
        val tGen: TGen
        type S = tGen.Vec
        final type Ext = tGen.T_/\
      }

      object VecIntGen extends VecGen {
        val tGen: IntGen.type = IntGen
      }

      implicitly[VecIntGen.Ext =:= Int] // success!

      { // If you insist on using match type without higher kind
        trait SGen {
          type S <: Vec_*
          //          type Ext = S match {
          //            case VecLt[t] => t
          //            case _ => S
          //          }
          /*
          The match type contains an illegal case:
    case VecLt[t] => t
(this error can be ignored for now with `-source:3.3`)
           */
        }

        object SIntGen extends SGen {
          final type S = IntGen.Vec
        }

        //        implicitly[SIntGen.Ext =:= Int]
        //        implicitly[SIntGen.Ext <:< Int]
        // still broken in Scala 3.3, but easier to fix
      }
    }
  }

  object DependentPoly {

    object Problems {

      sealed trait Col[V] {

        trait Wrapper

        val wrapper: Wrapper = ???
      }

      val polyFn = [C <: Col[?]] => (x: C) => x.wrapper
    }

    object Dual {

      trait VGen {

        type V

        sealed trait Col {

          trait Wrapper

          val wrapper: Wrapper = ???
        }

        val polyFn: (x: Col) => x.Wrapper = (x: Col) => x.wrapper
      }
    }
  }

  object `Anonymous class in higher kinded value class #18099` {

    { // Primary
      class Foo[A]

      object Test {
        class Bar[F[_]](val underlying: Any) {
          def foo: Foo[F[Any]] = new Foo[F[Any]] {}
        }
      }
    }

    { // Dual
      trait AGen {

        class Foo

        trait FUnderscoreGen extends AGen {}
      }
      object AnyGen extends AGen

      //      trait F_Gen {
      //
      //        type F_ <: ((aGen: AGen) => aGen.FUnderscoreGen)
      //
      //        class Bar(constructor: F_)(val underlying: Any) {
      //          val fuGen: F_[AnyGen] = constructor(AnyGen)
      //
      //          def foo: F_[AnyGen] = new fuGen.Foo {}
      //        }
      //      }
    }
  }

  object `this.type <: Compat` {

    trait Law
    trait NodeK[+L <: Law] {
      type Value

      {
        summon[this.type <:< NodeK.Compat[L, Value]]
      }
    }

    object NodeK {
      type Compat[L <: Law, V] = NodeK[L] { type Value <: V }
    }
  }

  object SimpleBoundDeduction {

    { // Primary

      trait IUB

      type TT[I <: IUB] = I

      //    val v: IUB = ??? : TT[?]
      //    summon[TT[?] <:< IUB]
    }

    { // Dual

      trait IUB

      trait IGen {
        type I <: IUB
        type TT = I
      }

      summon[IGen#TT <:< IUB]
    }
  }

}
