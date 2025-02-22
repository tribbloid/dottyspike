package com.tribbloids.spike.dotty

import scala.reflect.Typeable

object MatchErasedType {

  trait Supe {
    self: Singleton =>

    type T1
    lazy val default: T1

    lazy val tt: Typeable[T1] = summon

    def process(v: Any): T1 = {
      given Typeable[T1] = tt

      v match {
        case vv: (T1 & Int) => println(1); vv
//        case vv: T1 { def pre: Int } => println(2); vv
        case vv: T1 => println(3); vv
        case _ => println(4); default
      }
    }
  }

  object Impl1 extends Supe {
    override type T1 = Int
    override lazy val default: T1 = 1

    override lazy val tt: Typeable[T1] = summon
  }

  object Impl2 extends Supe {
    override type T1 = String
    override lazy val default: T1 = "a"

    override lazy val tt: Typeable[T1] = summon
  }

  def main(args: Array[String]): Unit = {

    val tt2 = summon[Typeable[List[Int]]]

    val r = List("a", "b") match {
      case vv: List[Int] => 1
      case _ => 2
    }

    Impl1.process(1.asInstanceOf[Any]) // 1
    Impl1.process("a".asInstanceOf[Any]) // 2
    Impl2.process("a".asInstanceOf[Any]) // 2
  }
}
