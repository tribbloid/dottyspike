package com.tribbloids.spike.dotty.conjecture

object HigherKindV3 {

  trait _0

  trait _1 extends _0

  object Composition {

    { // Primary
      trait Vec[T]
      //     alternatively type Vec = [T] =>> Vec_*

      trait Matrix[T] extends Vec[Vec[T]]

      object VInt extends Vec[Int]
      object MInt extends Matrix[Int]
    }

    { // Dual
      trait Vec_* {
        type TT
      }

      trait TGen {

        type T

        trait Vec extends Vec_* { type TT = T }

        object VecTGen extends TGen { override type T = TGen.this.Vec }

        trait Matrix extends VecTGen.Vec
      }

      object IntGen extends TGen { type T = Int }

      object VInt extends IntGen.Vec
      object MInt extends IntGen.Matrix
    }
  }

  object Bounded {

    { // Primary
      trait Vec[T >: Nothing <: Any]

      trait NVec[T >: Nothing <: Number] extends Vec[T]
    }

    { // Dual
      trait Vec_*

      trait TGen {
        type T_/\ <: Any
        type T_\/ >: Nothing <: T_/\

        trait Vec extends Vec_* {
          type TT >: T_\/ <: T_/\
        }
      }

      trait NTGen extends TGen {
        override type T_/\ <: Number
      }
    }
  }

  object Covariance {

    { // Primary
      trait Cov[+T]
      type Cov_* = Cov[?]

      summon[Cov[_1] <:< Cov[_0]]

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

      summon[_1Gen.Cov <:< _0Gen.Cov]

      trait G0 extends _0Gen.CovImpl
      trait G1 extends G0 with _1Gen.CovImpl

      object G0 extends G0
      object G1 extends G1
    }
  }

  object Contravariance {

    { // Primary
      trait Cov[-T]
      type Cov_* = Cov[?]

      summon[Cov[_0] <:< Cov[_1]]
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

      summon[_0Gen.Cov <:< _1Gen.Cov]
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

  object Nat {

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
}
