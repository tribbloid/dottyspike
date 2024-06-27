package com.tribbloids.spike.dotty.quoted.autolift

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, ObjectInputStream, ObjectOutputStream}
import java.util.Base64
import scala.quoted.*

trait SerialisingLowering[T] extends ToExpr[T] {

  def apply(x: T)(
      using
      Quotes
  ): Expr[T] = {
    val stringsExpr: Expr[Seq[String]] = Varargs(serialize(x).map(Expr(_)))

    deserialize(stringsExpr)
  }

  protected def serialize(x: T): Seq[String]

  protected def deserialize(expr: Expr[Seq[String]])(
      using
      Quotes
  ): Expr[T]
}

object SerialisingLowering {

  object JVMNativeLowering {

    @transient lazy val encoder: Base64.Encoder = Base64.getEncoder
    @transient lazy val decoder: Base64.Decoder = Base64.getDecoder

    private inline val MAX_LITERAL_LENGTH = 32768

    protected def deserializeImpl[T](strings: Seq[String]): T = {
      val bytes = strings.map(decoder.decode).reduce(_ ++ _)
      val bIStream = new ByteArrayInputStream(bytes)
      val oIStream = new ObjectInputStream(bIStream)
      val v = oIStream.readObject()
      v.asInstanceOf[T]
    }

    def deserialize[T: Type](expr: Expr[Seq[String]])(
        using
        Quotes
    ): Expr[T] = {

      '{ deserializeImpl[T]($expr) }
    }

    object Implicits {

      given only[T <: Serializable: Type]: JVMNativeLowering[T] = JVMNativeLowering[T]()
    }
  }

  class JVMNativeLowering[T <: Serializable] extends SerialisingLowering[T] {

    import JVMNativeLowering.*

    override protected def serialize(x: T): Seq[String] = {
      val bOStream = new ByteArrayOutputStream()
      val oOStream = new ObjectOutputStream(bOStream)
      oOStream.writeObject(x)
      val serialized = encoder.encodeToString(bOStream.toByteArray)
      serialized.sliding(MAX_LITERAL_LENGTH, MAX_LITERAL_LENGTH).toSeq
    }

    override protected def deserialize(expr: Expr[Seq[String]])(
        using
        Quotes
    ): Expr[T] = {

      JVMNativeLowering.deserialize(expr)
    }
  }

}
