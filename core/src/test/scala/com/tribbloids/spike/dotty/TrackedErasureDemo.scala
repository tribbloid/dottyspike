package com.tribbloids.spike.dotty

import scala.language.experimental.modularity

/** Demonstrates that tracked term/type arguments can be erased without warning
  * when using type annotations (without Aux pattern) in Scala 3.8.1+
  *
  * KEY POINT: The `tracked` modifier allows the compiler to:
  * 1. Erase the tracked parameter at runtime (no bytecode representation)
  * 2. Preserve type information at compile time via refinement types
  * 3. Eliminate the need for the Aux pattern to track dependent types
  *
  * Prior to tracked parameters, the Aux pattern was required to preserve type
  * information across class instantiations, making modular programming verbose.
  */
object TrackedErasureDemo {

  // ============ BEFORE: Traditional Aux Pattern ============
  // Required because without `tracked`, type information is lost after instantiation
  trait OrderingAux {
    type T
    def compare(t1: T, t2: T): Int
  }

  object OrderingAux {
    // The infamous Aux pattern - verbose and error-prone
    type Aux[_T] = OrderingAux { type T = _T }
  }

  class SetFunctorAux(val ord: OrderingAux) {
    type Set = List[ord.T]
    def empty: Set = Nil
    // Type information lost here - ord.T becomes an existential type
  }

  // ============ AFTER: Tracked Parameters ============
  trait OrderingTracked {
    type T
    def compare(t1: T, t2: T): Int
  }

  /** The `tracked` modifier on `ord` parameter:
    * 1. Erases `ord` at runtime - no field is generated in bytecode
    * 2. Preserves `ord.T` in the type via refinement: SetFunctorTracked { val ord: IntOrdering.type }
    * 3. No Aux pattern needed!
    */
  class SetFunctorTracked(tracked val ord: OrderingTracked) {
    type Set = List[ord.T]
    def empty: Set = Nil

    extension (s: Set)
      def add(x: ord.T): Set = x :: s
      def contains(x: ord.T): Boolean = s.exists(e => ord.compare(x, e) == 0)
  }

  // ============ Usage Examples ============
  object IntOrdering extends OrderingTracked {
    type T = Int
    def compare(t1: Int, t2: Int): Int = t1 - t2
  }

  object StringOrdering extends OrderingTracked {
    type T = String
    def compare(t1: String, t2: String): Int = t1.compareTo(t2)
  }

  /** Applied constructor type syntax (Scala 3.8+):
    * Type `SetFunctorTracked(IntOrdering)` is equivalent to:
    * `SetFunctorTracked { val ord: IntOrdering.type }`
    *
    * The tracked parameter `ord` is ERASED but the type `ord.T` is PRESERVED.
    */
  val IntSet: SetFunctorTracked(IntOrdering) = SetFunctorTracked(IntOrdering)
  val StringSet: SetFunctorTracked(StringOrdering) = SetFunctorTracked(StringOrdering)

  { // 2. type annotation should be guardrails, not unnsolicited wideninng (as compared to 3.)
    val IntSet: SetFunctorTracked = SetFunctorTracked(IntOrdering)
    val StringSet: SetFunctorTracked = SetFunctorTracked(StringOrdering)
  }

  { // 3.
    val IntSet = SetFunctorTracked(IntOrdering)
    val StringSet = SetFunctorTracked(StringOrdering)
  }

  def test(): Unit = {
    // Type annotation with applied constructor - NO WARNING about type erasure!
    // The compiler knows intSet has type List[Int] via the refinement
    val intSet: IntSet.Set = {
      import IntSet.*
      IntSet.empty.add(42).add(100)
    }
    assert(intSet.contains(42))
    assert(!intSet.contains(43))

    // Different ordering, different type - no interference
    val stringSet: StringSet.Set = {
      import StringSet.*
      StringSet.empty.add("hello")
    }
    assert(stringSet.contains("hello"))

    // Auto-inferred type also works - tracked inference is automatic
    val autoIntSet = SetFunctorTracked(IntOrdering)
    val autoIntSetValue: autoIntSet.Set = {
      import autoIntSet.*
      autoIntSet.empty.add(42)
    }
    assert(autoIntSetValue.contains(42))

    println("All tests passed!")
    println(s"IntSet type: ${IntSet.getClass.getSimpleName}")
    println(s"StringSet type: ${StringSet.getClass.getSimpleName}")
    println("Note: The tracked 'ord' parameter is erased at runtime (no field)")
  }
}
