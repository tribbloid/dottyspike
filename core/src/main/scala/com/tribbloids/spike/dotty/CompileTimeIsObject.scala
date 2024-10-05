package com.tribbloids.spike.dotty

import scala.quoted.*

object CompileTimeIsObject:

  inline def isObject[T]: Boolean = ${ isObjectImpl[T] }

  private def isObjectImpl[T: Type](
      using
      Quotes
  ): Expr[Boolean] =

    val q = summon[Quotes]

    import q.reflect.*

    val tpe = {
      TypeRepr.of[T]
    }
    val sym = tpe.typeSymbol

    Expr(sym.flags.is(Flags.Module) && !sym.flags.is(Flags.Trait))

  class MyClass

  // Usage examples
  object MyObject
