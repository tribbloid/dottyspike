package com.tribbloids.spike.dotty

object NonExtendableTypes {

  trait A
  trait B

  type C = A & B
//  trait D extends C // doesn't work
  trait D extends A with B

  type E = A { type Sub = Int }

//  trait F extends E // doesn't work
  trait F extends A { type Sub = Int }
}
