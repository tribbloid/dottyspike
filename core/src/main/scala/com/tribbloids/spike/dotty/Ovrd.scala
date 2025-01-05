package com.tribbloids.spike.dotty

object Ovrd {

  trait Node[+V] {
    def value: V
  }

  trait Axiom extends (Node[Nothing] => Node[Any]) {

//    def apply(value: Node[Nothing]): Node[Any]
  }

  val f = summon[(Node[Nothing] => Node[Any])] // TODO: why does it work?

  trait Induction[V] extends Axiom {

    summon[(Node[V] => Node[V]) <:< (Node[Nothing] => Node[Any])] // works

//    override def apply(value: Node[V]): Node[V] // doesn't
  }
}
