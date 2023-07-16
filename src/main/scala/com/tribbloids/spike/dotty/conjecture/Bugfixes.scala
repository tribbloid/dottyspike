package com.tribbloids.spike.dotty.conjecture

object Bugfixes {

  object `ExtensionalEquality F[X] vs F[_ <: X] For Covariant F` {

    { // Primary
      trait Mat[+T]

      summon[Mat[Product] <:< Mat[_ <: Product]]

      summon[Mat[_ <: Product] <:< Mat[Product]] // oops
      summon[Mat[Product] =:= Mat[_ <: Product]] // oops
    }

    { // Dual
      trait Mat_* {
        type TT
      }

      trait TGen {

        type T_/\
        type T_\/ <: T_/\
        type Mat = Mat_* { type TT <: T_/\ }
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

      type Ext[S <: Vec[_]] = S match {
        case Vec[t] => t
      }

      implicitly[Ext[Vec[Int]] =:= Int]
      implicitly[Ext[Vec[Int]] <:< Int]
      // broken in Scala 3.3: Cannot prove that e.Ext[Seq[Int]] =:= Int
      // works in Scala 3.4+

      implicitly[Int <:< Ext[Vec[Int]]]
    }

    { // dual, type classes are implicit terms
      trait Vec[+T]

      trait SGen {
        type SS
        type Ext
      }
      type GenAux[S] = SGen { type SS = S }

      object SGen {

        class VecGen extends SGen {
          final type SS = Vec[Ext]
        }

        implicit def forVec[T]: VecGen { type Ext = T } = new VecGen {
          type Ext = T
        }
      }

      val gen = summon[GenAux[Vec[Int]]]
      summon[gen.Ext =:= Int] // success!
    }

    { // dual, type classes are outer objects
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
          type Ext = S match {
            case VecLt[t] => t
            case _ => S
          }
        }

        object SIntGen extends SGen {
          final type S = Int
        }

        implicitly[SIntGen.Ext =:= Int]
        implicitly[SIntGen.Ext <:< Int]
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

        val polyFn = (x: Col) => x.wrapper
      }
    }
  }
}
