package sbt.docker

import Keys._
import sbt._
import sbt.Keys.name
import sbt.Keys.version
import sbt.Keys.streams
import sbt.Keys.baseDirectory

object SbtDockerPlugin extends Plugin {
  val dockerSettings = Seq(
    username in Docker := Option(sys.props("docker.username")).getOrElse(""),
    email in Docker := Option(sys.props("docker.email")).getOrElse(""),
    password in Docker := Option(sys.props("docker.password")).getOrElse(""),
    context in Docker <<= baseDirectory.map(identity),
    registry in Docker <<= (username in Docker)(identity),
    tag in Docker <<= (registry in Docker, name in Docker, version in Docker) { (registry, name, version) =>
      val base = s"$name:$version"
      if (registry.nonEmpty) {
        s"$registry/$base"
      } else {
        base
      }
    },
    build in Docker <<= (tag in Docker, context in Docker, streams) map { (tag, context, streams) =>
      s"docker build -q --rm -t $tag $context" ! streams.log
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
    }
  )
}

// vim: set ts=4 sw=4:
