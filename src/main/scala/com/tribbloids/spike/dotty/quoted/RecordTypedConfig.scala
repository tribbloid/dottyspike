package com.tribbloids.spike.dotty.quoted

object RecordTypedConfig {
  import RecordTypedConfigStage0._

  def main(args: Array[String]): Unit = {

    val config = typesafeConfig(
      "a" -> 1,
      "b" -> "2",
      "c" -> true
    )

    val k: Int = config.a
  }
}
