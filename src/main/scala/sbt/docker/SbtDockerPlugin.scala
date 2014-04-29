package sbt.docker

import Keys._
import sbt._
import sbt.Keys.name
import sbt.Keys.version
import sbt.Keys.streams
import sbt.Keys.baseDirectory

object SbtDockerPlugin extends Plugin {
  val dockerSettings = Seq(
    username in Docker := sys.props.getOrElse("docker.username", ""),
    email in Docker := sys.props.getOrElse("docker.email", ""),
    password in Docker := sys.props.getOrElse("docker.password", ""),
    context in Docker <<= baseDirectory.map(identity),
    registry in Docker <<= (username in Docker)(identity),
    runArgs := Nil,
    buildOpts in Docker := Seq("-q", "--rm"),
    containerArgs := Nil,
    tag in Docker <<= (registry in Docker, name in Docker, version in Docker) { (registry, name, version) =>
      val base = s"$name:$version"
      if (registry.nonEmpty) {
        s"$registry/$base"
      } else {
        base
      }
    },
    build in Docker <<= (tag in Docker, buildOpts in Docker, context in Docker, streams) map { (tag, buildOpts, context, streams) =>
      s"docker build ${buildOpts.mkString(" ")} -t $tag $context" ! streams.log
    },
    login in Docker <<= (username in Docker, password in Docker, email in Docker, streams) map { (u, p, e, streams) =>
      if (u.nonEmpty) {
        s"docker login -u $u -p $p -e $e" ! streams.log
      }
    },
    push in Docker <<= (build in Docker, login in Docker, registry in Docker, tag in Docker, streams) map { (_, _, registry, tag, streams) =>
      if (registry.isEmpty) {
        throw new IllegalStateException("must specify a registry to use push")
      }
      s"docker push $tag" ! streams.log
    },
    pull in Docker <<= (tag in Docker, registry in Docker, login in Docker, streams) map { (tag, registry, _, streams) =>
      if (registry.isEmpty) {
        throw new IllegalStateException("must specify a registry to use pull")
      }
      s"docker pull $tag" ! streams.log
    },
    runContainer in Docker <<= (tag in Docker, runArgs in Docker, containerArgs in Docker, streams) map { (tag, runArgs, containerArgs, streams) =>
      s"docker run " + runArgs.mkString(" ") + s" $tag " + containerArgs.mkString(" ") ! streams.log
    }
  )
}

// vim: set ts=4 sw=4:
