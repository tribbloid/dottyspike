package com.tribbloids.spike.dotty.quoted.autolift

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, ObjectInputStream, ObjectOutputStream}
import java.util.Base64
import scala.quoted.*

sealed trait SerializableExpr[T]:
  def apply(x: T)(
      using
      Quotes
  ): Expr[T]

sealed trait DeSerializableExpr[T]:
  def unapply(x: Expr[T])(
      using
      Quotes
  ): Option[T]

object SerializableExpr {

  private inline val MAX_LITERAL_LENGTH = 32768

  lazy val encoder: Base64.Encoder = Base64.getEncoder
  lazy val decoder: Base64.Decoder = Base64.getDecoder

  def apply[T: SerializableExpr](x: T)(
      using
      Quotes
  ): Expr[T] =
    summon[SerializableExpr[T]].apply(x)

  def unapply[T: DeSerializableExpr](x: Expr[T])(
      using
      Quotes
  ): Option[T] =
    summon[DeSerializableExpr[T]].unapply(x)

  given serializableExpr[T <: Serializable: Type]: SerializableExpr[T] with {
    def apply(x: T)(
        using
        Quotes
    ): Expr[T] =
      val stringsExpr = Varargs(serialize(x).map(Expr(_)))
      '{ deserialize[T]($stringsExpr*) }
  }

  given deSerializableExpr[T <: Serializable: Type]: DeSerializableExpr[T] with {
    def unapply(x: Expr[T])(
        using
        Quotes
    ): Option[T] =
      x match
        case '{ deserialize[T](${ Varargs(stringExprs) }*) } =>
          Exprs.unapply(stringExprs).map(strings => deserialize(strings*))
        case _ => None
  }

  private def serialize(x: Serializable): Seq[String] = {
    val bOStream = new ByteArrayOutputStream()
    val oOStream = new ObjectOutputStream(bOStream)
    oOStream.writeObject(x)
    val serialized = encoder.encodeToString(bOStream.toByteArray)
    serialized.sliding(MAX_LITERAL_LENGTH, MAX_LITERAL_LENGTH).toSeq
  }

  private def deserialize[T <: Serializable](strings: String*) = {
    val bytes = strings.map(decoder.decode).reduce(_ ++ _)
    val bIStream = new ByteArrayInputStream(bytes)
    val oIStream = new ObjectInputStream(bIStream)
    val v = oIStream.readObject()
    v.asInstanceOf[T]
  }
}

object App {
  import SerializableExpr.given

  def example(
      using
      Quotes
  ) = {
    val serializedExpr = SerializableExpr("abc")
    serializedExpr match
      case SerializableExpr(value) => println(value)
      case _ =>
  }
}
