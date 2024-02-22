package com.tribbloids.spike.dotty.quoted.autolift

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, ObjectInputStream, ObjectOutputStream}
import java.util.Base64
import scala.quoted.{Expr, Quotes, ToExpr, Type, Varargs}

object Example {

  object SerializableExpr {

    private inline val MAX_LITERAL_LENGTH = 32768

    lazy val encoder: Base64.Encoder = Base64.getEncoder
    lazy val decoder: Base64.Decoder = Base64.getDecoder

    trait serializableExpr[T <: Serializable: Type] extends ToExpr[T] {
      def apply(x: T)(
          using
          Quotes
      ): Expr[T] =
        val stringsExpr = Varargs(serialize(x).map(Expr(_)))
        '{ deserialize[T]($stringsExpr*) }
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
}
