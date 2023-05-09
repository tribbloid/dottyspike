package com.tribbloids.spike.dotty.conjecture

object HigherKindV3 {

  trait _0

  trait _1 extends _0

  object Composition {

    { // Primary
      trait Vec[T]

      trait Matrix[T] extends Vec[Vec[T]]

      object VInt extends Vec[Int]
      object MInt extends Matrix[Int]
    }

    { // Dual
      trait Vec_* {
        type TT
      }

      trait TGen {
        type T_/\
        type T_\/ <: T_/\
        trait Vec extends Vec_* { type TT >: T_\/ <: T_/\ }

        object VecTGen extends TGen {
          override type T_/\ = TGen.this.Vec
          override type T_\/ = TGen.this.Vec
        }

        trait Matrix extends VecTGen.Vec
      }

      object IntGen extends TGen

      object VInt extends IntGen.Vec
      object MInt extends IntGen.Matrix
    }
  }

  object Covariance {

    { // Primary
      trait Cov[+T]
      type Cov_* = Cov[_]

      implicitly[Cov[_1] <:< Cov[_0]]

      trait G0 extends Cov[_0]
      trait G1 extends G0 with Cov[_1]

      object G0 extends G0
      object G1 extends G1
    }

    { // Dual
      trait Cov_* { type TT }

      trait TGen {
        type T_/\
        type T_\/ <: T_/\ // not used
        type Cov = Cov_* { type TT <: T_/\ }
      }

      object _0Gen extends TGen { type T_/\ = _0; trait CovImpl extends Cov_* { type TT <: T_/\ } }
      object _1Gen extends TGen { type T_/\ = _1; trait CovImpl extends Cov_* { type TT <: T_/\ } }
      // boilerplate can be avoided by allowing trait to extend duck types directly

      implicitly[_1Gen.Cov <:< _0Gen.Cov]

      trait G0 extends _0Gen.CovImpl
      trait G1 extends G0 with _1Gen.CovImpl

      object G0 extends G0
      object G1 extends G1
    }
  }

  object Contravariance {

    { // Primary
      trait Cov[-T]
      type Cov_* = Cov[_]

      implicitly[Cov[_0] <:< Cov[_1]]
    }

    { // Dual
      trait Cov_* {
        type TT
      }

      trait TGen {
        type T_/\ // not used
        type T_\/ <: T_/\

        type Cov = Cov_* { type TT >: T_\/ }
      }

      object _0Gen extends TGen {
        type T_/\ = Any
        type T_\/ = _0
      }

      object _1Gen extends TGen {
        type T_/\ = Any
        type T_\/ = _1
      }

      implicitly[_0Gen.Cov <:< _1Gen.Cov]
    }
  }

  object ExtensionalBound {

    { // Primary
      trait Mat[T]

      implicitly[Mat[Product] <:< Mat[_ <: Product]]

    }

    { // Dual
      trait Mat_* {
        type TT
      }

      trait TGen {
        type T_/\
        type T_\/ <: T_/\
        type Mat = Mat_* { type TT >: T_\/ <: T_/\ }
      }

      object ProductGen extends TGen {
        override type T_/\ = Product
        override type T_\/ = Product
      }

      object LessThanProductGen extends TGen {
        override type T_/\ = Product
        override type T_\/ = Nothing
      }

      implicitly[ProductGen.Mat <:< LessThanProductGen.Mat]
    }
  }

  object CovariantExtensionalEquality {

    { // Primary
      trait Mat[+T]

      implicitly[Mat[Product] <:< Mat[_ <: Product]]
//      implicitly[Mat[Product] =:= Mat[_ <: Product]] // oops
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

      implicitly[ProductGen.Mat =:= LessThanProductGen.Mat]
    }
  }

  object MatchCovariantHigherKindArg {
    // https://github.com/lampepfl/dotty/issues/17149

    trait Vec[+T]

    { // Primary1

      type Ext[S <: Vec[_]] = S match {
        case Vec[t] => t
      }

      implicitly[Int <:< Ext[Vec[Int]]]
//      implicitly[Int =:= Ext[Vec[Int]]] // broken
    }

    { // Primary2
      trait SGen {

        type S <: Vec[_]

        type Ext = S match {
          case Vec[t] => t
        }
      }

      object SeqIntGen extends SGen {

        type S = Vec[Int]
      }

      implicitly[Int <:< SeqIntGen.Ext]
//      implicitly[Int =:= SeqIntGen.Ext] // still broken
    }

    { // Dual
      trait Vec_* {
        type TT
      }
      object Vec_* {
        type Lt[T] = Vec_* { type TT <: T }
      }

      trait TGen {

        type T
        type Vec = Vec_*.Lt[T]
      }
      object IntGen extends TGen { type T = Int }

      trait SGen {

        type S <: Vec_*

        type Ext = S match {
          case Vec_*.Lt[t] => t
        }
      }

      object SIntGen extends SGen {

        type S = IntGen.Vec
      }

      implicitly[Int <:< SIntGen.Ext]
//      implicitly[Int =:= SIntGen.Ext] // also broken, but easier to fix
    }
  }

  // TODO: the following definition still use unbounded dual form, need fix or cleanup

  // https://www.slideshare.net/Odersky/implementing-higherkinded-types-in-dotty?from_search=0
  // Slide page 18
  object SimpleEncoding {

    object Primary {

      type Rep[T] = T
      type LL[T] = List[List[T]]
      type RMap[V, K] = Map[K, V]
    }

    object Dual {

      trait TGen {
        type _T

        type Rep = _T
        type LL = List[List[_T]]
      }

      trait VKGen {
        type _V
        type _K

        type RMap = Map[_K, _V]
      }
    }
  }

  // https://www.slideshare.net/Odersky/implementing-higherkinded-types-in-dotty?from_search=0
  // Slide page 26
  object TypeRefinementEncoding {

    type U
    type S <: U

    object Primary {

      trait F[T >: S <: U]
      type Rep[T] = T
    }

    object Dual {

      object TGen {
        type _T >: S <: U

        trait F

        type Rep = _T
      }
    }
  }

  object GADT {

    trait Nat
    trait _1 extends Nat

    object Primary {

      trait Succ[N <: Nat] extends Nat

      type _4 = Succ[Succ[Succ[_1]]]
    }

    object Dual {

      trait NGen {

        type _N <: Nat

        trait Succ extends Nat
      }

      object _1 extends NGen {
        type _N = _1
      }
      object _2 extends NGen {
        type _N = _1.Succ
      }
      object _3 extends NGen {
        type _N = _2.Succ
      }
      type _4 = _3.Succ
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
