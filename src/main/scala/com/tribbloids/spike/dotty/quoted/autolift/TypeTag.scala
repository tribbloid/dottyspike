package com.tribbloids.spike.dotty.quoted.autolift

import dotty.tools.dotc.ast.tpd
import dotty.tools.dotc.core.{Contexts, TastyInfo}
import dotty.tools.dotc.core.Contexts.{Context, ContextBase}
import dotty.tools.dotc.core.tasty.DottyUnpickler
import dotty.tools.dotc.core.tasty.TreeUnpickler.UnpickleMode
import dotty.tools.dotc.quoted.{PickledQuotes, TastyString}
import dotty.tools.io.NoAbstractFile

import scala.quoted.*
import scala.quoted.runtime.QuoteUnpickler
import scala.quoted.runtime.impl.{QuotesImpl, SpliceScope, TypeImpl}
import scala.quoted.staging.QuoteDriver
import scala.runtime.ScalaRunTime

case class TypeTag[T](
    tastyPickle: List[String]
) extends Serializable {

  import TypeTag.*

  def runtimeClass: Class[T] = ???

  def tree: tpd.Tree = {

    val bytes = TastyString.unpickle(tastyPickle)
    val unpickler = new DottyUnpickler(NoAbstractFile, bytes, UnpickleMode.TypeTree)

//    unpickler.enter(Set.empty)
//    val result: tpd.Tree = unpickler.tree

//    tree
    ???
  }

  def unbox(
      implicit
      ctx: Context
  ): Type[T] = {

    val tree = PickledQuotes.unpickleTypeTree(tastyPickle, PickledQuotes.TypeHole.V2(null))
    new TypeImpl(tree, SpliceScope.getCurrent).asInstanceOf[scala.quoted.Type[T]]
  }

  def unbox(quotes: Quotes): Type[T] = {

    quotes.asInstanceOf[QuoteUnpickler].unpickleTypeV2(tastyPickle, null)
  }
}

object TypeTag {

  def compile[T]()(
      using
      Type[T],
      Quotes
  ): Expr[TypeTag[T]] = {

    val tt = implicitly[Type[T]]
    val qq = implicitly[Quotes]

    val raw = (tt, qq) match {

      case (t: TypeImpl, q: QuotesImpl) =>
        given ctx: Context = q.ctx

        val tree = t.typeTree
        val pickle = PickledQuotes.pickleQuote(tree)

        val txt = tree.showIndented(2)

        println(txt)
        val raw = TypeTag[T](pickle)

        raw
      case _ =>
        ???
    }

    import SerialisingLowering.JVMNativeLowering.Implicits.given
    //      given toExpr: ToExpr[TypeTag[T]] = JVMNativeSerializingToExpr.Implicits.only[TypeTag[T]]

    Expr.apply[TypeTag[T]](raw)
  }

  inline given get[T]: TypeTag[T] = ${ compile[T]() }
}
