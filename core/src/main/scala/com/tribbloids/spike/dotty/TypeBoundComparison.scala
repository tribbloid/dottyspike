package com.tribbloids.spike.dotty

object TypeBoundComparison {

  //  trait A1 {
  //
  //    type T >: Int <: AnyVal
  //  }
  //
  //  trait A2 {
  //
  //    type T >: Int <: AnyVal
  //  }
  //
  //  implicitly[A2#T <:< A1#T]

  trait W {
    type TT
  }
  object W extends W {
    type TT <: Int
  }

  trait AA {

    type F1[+T] = W { type TT <: T }

    type F2[+T] = Seq[T]

    type F3[+T] <: T
  }
  object AA extends AA

  trait AA1 extends AA

  trait AA2 extends AA

  implicitly[AA1#F1[Int] =:= AA2#F1[Int]]
  implicitly[AA1#F2[Int] =:= AA2#F2[Int]]

//  implicitly[AA.F3[Int] =:= W.TT]

//  implicitly[AA1#F3[Int] =:= AA2#F3[Int]]

  implicitly[AA1#F1[Int]#TT =:= AA2#F1[Int]#TT]

}
