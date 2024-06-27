

## Main Table

|                                                 | H-M (Flix) | DOT (Scala 3) | Scala 2 |
|-------------------------------------------------|------------|---------------|---------|
| Expressions                                     |            |               |         |
| `val value = 1`                                 | [x]        | [x]           | [x]     |
| `val term = (value: Int) + 2`                   | [x]        | [x]           | [x]     |
| `val lambda = { (x: Int) => x * term }`         | [x]        | [x]           | [x]     |
| `val application = lambda(term)`                | [x]        | [x]           | [x]     |
| `val selection = (1 -> "a")._2`                 | [x]        | [x]           | [x]     |
| `def context(): Unit = { val a1: Int = 1}`      | [x]        | [x]           | [x]     |
| Types                                           |            |               |         |
| `type MONO1 = Int `                             | [x]        | [x]           | [x]     |
| `type MONO2 = (String, Int)`                    | [x]        | [x]           | [x]     |
| `trait Fields { val _1: String; val _2: Int }`  |            | [x]           | [x]     |
| `type INTERSECTION = Product with Serializable` |            | [x]           | [x]     |
| `type POLY1 = [X] =>> (X => X) => X`            |            | [x]           |         |
| `type POLY2 = (x: (Boolean, Int)) => x._2.type` |            | [x]           |         |
| `type BOUND >: Tuple <: Product`                |            | [x]           | [x]     |
| `type UNION = Int \| String`                    | [x]        | [x]           |         |
| `type RECURSIVE = Tuple { val _1: String }`     |            | [x]           | [x]     |
| `type TOP = An`                                 |            | [x]           | [x]     |
| `type BOTTOM = Nothing`                         |            | [x]           | [x]     |
