package com.tribbloids.spike.dotty


object JoinTypeResolving {

  object C1 {

    trait A {

      def get: (Any, Any)

      protected def get2: (Any, Any) = get
    }

    trait A1 extends A {

      def get: (Product, Any)

      override protected def get2: (Product, Any) = get
    }

    trait A2 extends A {
      def get: (Any, Int)

      override protected def get2: (Any, Int) = get
    }
//
//    object B extends A1 with A2 {
//      override def get: (Product, Int) = ???
//    }
  }

  object C2 {

    trait A {

      type K <: (Any, Any)

      def get: K

      protected def get2: K = get
    }

    trait A1 extends A {

      type K <: (Product, Any)
    }

    trait A2 extends A {
      type K <: (Any, Int)
    }

    object B extends A1 with A2 {

      type K <: (Product, Int)
      override def get: K = {
//        ??? : (Product, Int) doesn't work
        ???
      }
    }
  }

}
