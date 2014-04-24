sbt-docker
==========

A simple sbt plugin that makes it easy to publish docker images from an
sbt project with minimal configuration. It depends on the docker CLI.

Usage
=====

    [svc-iris-hub] $ docker:push - build, tag, and push your image.
    [svc-iris-hub] $ docker:build - build and tag your image.
    [svc-iris-hub] $ docker:pull - pull the image matching the current value of tag.
    [svc-iris-hub] $ docker:tag - the value of the tag used on the image (e.g. <registry>/<name>:<version>).
    [svc-iris-hub] $ docker:login - login to the configured docker registry using the supplied credntials. No-op if no credentials are given.
