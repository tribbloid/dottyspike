package com.tribbloids.spike.dotty

import com.tribbloids.spike.dotty.quoted.RecordTypedConfigStage0
import com.tribbloids.spike.dotty.quoted.RecordTypedConfigStage0.typesafeConfig

object RecordTypedConfig {
  import RecordTypedConfigStage0.*

  def main(args: Array[String]): Unit = {

    val config = typesafeConfig(
      "a" -> 1,
      "b" -> "2",
      "c" -> true
    )

    val k: Int = config.a
  }
}
