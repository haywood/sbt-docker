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

  val context = SettingKey[File]("context", "The context of the docker build.")

  val password = SettingKey[String]("password", "docker.io password")
}

// vim: set ts=4 sw=4:
