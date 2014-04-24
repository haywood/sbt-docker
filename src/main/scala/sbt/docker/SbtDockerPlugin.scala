package sbt.docker

import sbt._
import sbt.Keys.streams

object SbtDockerPlugin extends Plugin {
  val Docker = config("docker")

  val build = TaskKey[Unit]("build", "Build a docker image.")

  val login = TaskKey[Unit]("login", "Login into the docker index.")

  val push = TaskKey[Unit]("push", "Push the docker image.")

  val repo = SettingKey[String]("repo", "The docker repo to push to.")

  val tag = SettingKey[String]("tag", "The tag to give the image.")

  val username = SettingKey[String]("username", "docker.io username")

  val email = SettingKey[String]("email", "docker.io email")

  val password = SettingKey[String]("password", "docker.io password")

  override lazy val settings = Seq(
    username in Docker := sys.props("docker.username"),
    email in Docker := sys.props("docker.email"),
    password in Docker := sys.props("docker.password"),
    build in Docker <<= (repo in Docker, tag in Docker, streams) map { (repo, tag, streams) =>
      val regex = """Successfully built (\w+)""".r
      s"docker build -t $repo:$tag ." ! streams.log
    },
    login in Docker <<= (username in Docker, password in Docker, email in Docker, streams) map { (u, p, e, streams) =>
      s"docker login -u $u -p $p -e $e" ! streams.log
    },
    push in Docker <<= (build in Docker, login in Docker, repo in Docker, tag in Docker, streams) map { (image, _, repo, tag, streams) =>
      s"docker push $repo:$tag" ! streams.log
    },
    tag in Docker <<= sbt.Keys.version
  )
}

// vim: set ts=4 sw=4:
