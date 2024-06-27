package com.tribbloids.spike.dotty

object CoerciveCovariance {

  trait Cov[+T]

//  def cast[A, B](v: Cov[A])(
//      implicit
//      ev: A <:< B
//  ) = {
//    v: Cov[B]
//  }

  type Ext[S <: Any] = S match {
    case Seq[t] => t
  }

  implicitly[Ext[Seq[Int]] =:= Int]

  trait XX[A, B]

  trait XX1[B <: Seq[_]] extends XX[Ext[B], B]

  trait SI extends Seq[Int]

  implicitly[Ext[SI] =:= Int]

  new XX1[SI] {}

//  new XX1[Array[Int]] {}
}
