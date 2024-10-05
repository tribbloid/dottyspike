package com.tribbloids.spike.dotty.quoted.autolift.spike

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, ObjectInputStream, ObjectOutputStream}
import java.util.Base64
import scala.quoted.*

private object AutoLiftExample {

  trait SerializingAutoLift[T] extends ToExpr[T] {

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

  object JVMNativeSerializingToExpr {

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

      given only[T <: Serializable: Type]: JVMNativeSerializingToExpr[T] = JVMNativeSerializingToExpr[T]()
    }
  }

  class JVMNativeSerializingToExpr[T <: Serializable] extends SerializingAutoLift[T] {

    import JVMNativeSerializingToExpr.*

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

      JVMNativeSerializingToExpr.deserialize(expr)
    }
  }

  trait TypeTag[T] extends Serializable {
    def unbox: Type[T]
  }

  object TypeTag {

    def getImpl[T]()(
        using
        Type[T],
        Quotes
    ): Expr[TypeTag[T]] = {

      val tt = Type.of[T]

      val v = new TypeTag[T] {
        def unbox: Type[T] = tt
      }

      import JVMNativeSerializingToExpr.Implicits.given
//      given toExpr: ToExpr[TypeTag[T]] = JVMNativeSerializingToExpr.Implicits.only[TypeTag[T]]

      Expr.apply[TypeTag[T]](v)
    }

    inline given get[T]: TypeTag[T] = ${ getImpl[T]() }
  }
}
