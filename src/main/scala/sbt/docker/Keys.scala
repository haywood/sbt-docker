package sbt.docker

import sbt._

object Keys {
/* Configs */

  val Docker = config("docker") extend Compile

/* Settings */

  val username = settingKey[String]("docker.io username")

  val email = settingKey[String]("docker.io email")

  val password = settingKey[String]("docker.io password")

  val tag = settingKey[String]("The full tag of the image. Constructed from the registry, name , and version. You should not need to override, but access to the value is provided for convenience.")

  val registry = settingKey[String]("The docker registry to use for tag, push, and pull. Defaults to username.")

  val runArgs = settingKey[Seq[String]]("The arguments to the docker run command.")

  val containerArgs = settingKey[Seq[String]]("The arguments to pass to the container when using docker run.")

  val buildOpts = settingKey[Seq[String]]("Options to pass to docker build. Does not include the tag option, which is always given.")

/* Tasks */

  val build = taskKey[Unit]("Build a docker image.")

  val login = taskKey[Unit]("Login into the docker index.")

  val push = taskKey[Unit]("Push the docker image.")

  val pull = taskKey[Unit]("Pull the docker image.")

  val context = taskKey[File]("The context of the docker build.")

  val runContainer = taskKey[Unit]("Run the docker image as a container.")
}

// vim: set ts=4 sw=4:
