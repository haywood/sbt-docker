import sbt.docker.Keys.buildOpts
import sbt.docker.Keys.context
import sbt.docker.Keys.Docker

import com.typesafe.sbt.packager.universal.Keys.stage
import com.typesafe.sbt.packager.universal.Keys.stagingDirectory
import org.apache.commons.io.FileUtils

sbt.docker.SbtDockerPlugin.dockerSettings

packagerSettings

buildOpts in Docker := Seq("--rm", "--no-cache")

context in Docker <<= (
  baseDirectory in Docker, stage in Universal, stagingDirectory in Universal,
  streams
) map { (baseDir, _, stagingDir, streams) =>
    streams.log.debug("copying Dockerfile into $stagingDir")
    def dockerfile(dir: File) = new File(dir, "Dockerfile")
    FileUtils.copyFile(dockerfile(baseDir), dockerfile(stagingDir))
    stagingDir
}
