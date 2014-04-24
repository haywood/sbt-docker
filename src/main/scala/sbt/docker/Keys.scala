package sbt.docker

import sbt._

object Keys {
  val Docker = config("docker") extend Compile

  val build = TaskKey[Unit]("build", "Build a docker image.")

  val login = TaskKey[Unit]("login", "Login into the docker index.")

  val push = TaskKey[Unit]("push", "Push the docker image.")

  val pull = TaskKey[Unit]("pull", "Pull the docker image.")

  val username = SettingKey[String]("username", "docker.io username")

  val email = SettingKey[String]("email", "docker.io email")

  val context = TaskKey[File]("context", "The context of the docker build.")

  val password = SettingKey[String]("password", "docker.io password")

  val tag = SettingKey[String]("tag", "The full tag of the image. Constructed from the registry, name , and version. You should not need to override, but access to the value is provided for convenience.")

  val registry = SettingKey[String]("registry", "The docker registry to use for tag, push, and pull. Defaults to username.")
}

// vim: set ts=4 sw=4:
