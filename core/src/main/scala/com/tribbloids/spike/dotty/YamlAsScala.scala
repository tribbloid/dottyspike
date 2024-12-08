package com.tribbloids.spike.dotty

object YamlAsScala {

  object YAML:
    apiVersion:
      apps / v1
    kind:
      Deployment
    metadata:
      name:
        "my-app"
      namespace:
        "my-app"
      labels:
        app.kubernetes.io / name:
          "my-app"
      containers:
        envFrom:
          -configMapRef:
            name:
              "my-app-config-env-vars"

          -secretRef: // the empty line above this one is needed
            name:
              "my-app-secret-env-vars"

  // Some stubs to prove the syntax above.
  object Deployment

  def apiVersion(k: Unit): Unit = ???

  object apps {
    def /(u: Unit): Unit = ???
  }

  val v1: Unit = ???

  def kind(d: Deployment.type): Unit = ???

  def metadata(k: Unit): Unit = ???

  def name(s: String): Unit = ???

  def namespace(s: String): Unit = ???

  def labels(s: Unit): Unit = ???

  object app {
    object kubernetes {
      object io {
        def /(r: Unit): Unit = ???
      }
    }
  }

  def containers(r: Unit): Unit = ???

  def envFrom(a: Unit): Unit = ???

  class Item {
    def unary_- : Unit = ???
  }

  def configMapRef(r: Unit): Item = ???

  def secretRef(r: Unit): Item = ???
}
