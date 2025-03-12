package ai.acyclic.zio.zio

import zio.schema.{DeriveSchema, Schema}

class AxiomPlugin {

  { // fail
    import java.nio.file.Path
    case class User(name: String)

    trait Assigment_Imp0 {
      implicit lazy val dummy: Schema[Path] = Schema.fail("dummy")
    }
    object Assignment extends Assigment_Imp0
    case class Assignment(user: User, path: Path)

//    val ss: Schema[Assignment] = DeriveSchema.gen[Assignment]
    /*
      Error: .. /DeriveSchemaSpike.scala:70:50
      Deriving schema for java.nio.file.Path is not supported
          val ss: Schema[Assignment] = DeriveSchema.gen[Assignment]

     */
  }

  { // fail

    trait Path
    object Path {}
    case class User(name: String)

    case class Assignment(user: User, path: Path)

    object Assignment {
      implicit lazy val dummy: Schema[Path] = Schema.fail("dummy")
    }

//    val ss: Schema[Assignment] = DeriveSchema.gen[Assignment]
  }

  { // success

    trait Path
    object Path {

      implicit lazy val dummy: Schema[Path] = Schema.fail("dummy")
    }
    case class User(name: String)

    object Assignment {}

    case class Assignment(user: User, path: Path)

    val ss: Schema[Assignment] = DeriveSchema.gen[Assignment]
  }
}
