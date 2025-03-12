package ai.acyclic.zio.shapeless3

import org.scalatest.funspec.AnyFunSpec
import shapeless3.deriving.K0.Generic

// Example usage
object ProductTypeExample {

  // Define the traits and classes as provided
  trait Path

  object Path {

    given Generic[Path] = {
      ???
    }
  }

  case class User(name: String)

  case class Assignment(user: User, path: Path)

}

class ProductTypeExample extends AnyFunSpec {

  import ProductTypeExample.*

  it("der") {

    val gen = Generic[Assignment]
  }
}
