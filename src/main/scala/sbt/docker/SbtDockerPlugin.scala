package sbt.docker

import sbt._
import sbt.Keys.name
import sbt.Keys.version
import sbt.Keys.streams

object SbtDockerPlugin extends Plugin {
  val Docker = config("docker") extend Compile

  val build = TaskKey[Unit]("build", "Build a docker image.")

  val login = TaskKey[Unit]("login", "Login into the docker index.")

  val push = TaskKey[Unit]("push", "Push the docker image.")

  val pull = TaskKey[Unit]("pull", "Pull the docker image.")

  val username = SettingKey[String]("username", "docker.io username")

  val email = SettingKey[String]("email", "docker.io email")

  val context = SettingKey[File]("context", "The context of the docker build.")

  val password = SettingKey[String]("password", "docker.io password")

  override lazy val settings = Seq(
    username in Docker := Option(sys.props("docker.username")).getOrElse(""),
    email in Docker := Option(sys.props("docker.email")).getOrElse(""),
    password in Docker := Option(sys.props("docker.password")).getOrElse(""),
    context in Docker <<= sbt.Keys.baseDirectory,
    build in Docker <<= (name in Docker, version in Docker, context in Docker, streams) map { (name, version, context, streams) =>
      s"docker build -q --rm -t $name:$version $context" ! streams.log
    },
    login in Docker <<= (username in Docker, password in Docker, email in Docker, streams) map { (u, p, e, streams) =>
      s"docker login -u $u -p $p -e $e" ! streams.log
    },
    push in Docker <<= (build in Docker, login in Docker, name in Docker, version in Docker, streams) map { (image, _, name, version, streams) =>
      s"docker push $name:$version" ! streams.log
    },
    pull in Docker <<= (name, version, streams) map { (name, version, streams) =>
      s"docker pull $name:$version" ! streams.log
    }
  )
}

// vim: set ts=4 sw=4:
