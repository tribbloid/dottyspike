package com.tribbloids.spike.dotty

object PolyFunctionSubTyping {

  trait T1

  trait T2 extends T1

  trait T3 extends T2

  trait Universe[T] {

    def apply: T => Seq[T]
  }

  trait Cov[+T] {

    def apply: Any => Seq[T]
  }

  trait Contra[-T] {

    def apply(v: T): Any
  }

  trait MidBound {

    def apply[T <: T2]: T => Seq[T]
  }

  trait LeastBound {

    def apply[T <: Nothing]: T => Any
  }

  trait MostBound {

    def apply[T]: T => Any
  }

  object HasBetterBound {

    trait X extends MidBound {

      override def apply[T <: T1]: T => Seq[T]
    }

    trait Y extends LeastBound {

      override def apply[T <: T1]: T => Seq[T]
    }

    trait Z extends LeastBound {

      type RR[T] = T match {
        case Option[i] => Seq[i]
      }

      override def apply[T]: T => RR[T] = ???
    }

    trait RRBound {

      type RR[T]

      def apply[T]: T => RR[T]
    }

    trait Z2 extends RRBound {

      type RR[T] = T match {
        case Option[i] => Seq[i]
      }

      override def apply[T]: T => RR[T] = ???

//      override def apply[T]: T => RR[T] = T match {
//        case Option[i] =>
//      }
    }

    trait AlsoZ2 {

      def apply[i]: Option[i] => Seq[i]
    }

    object Z extends Z

//    Z.apply(3) won't work
  }

//  trait HasWorserBound extends HasBound {
//
//    override def apply[T <: T3]: T => Seq[T] // doesn't work
//  }
}
