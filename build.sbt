
sbtPlugin := true

organization := "com.github.bigtoast"

name := "sbt-liquibase"

version := "0.6"

scalaVersion := "2.10.4"

crossScalaVersions := Seq("2.11.0")

libraryDependencies += "org.liquibase" % "liquibase-core" % "3.4.2"

publishMavenStyle := true

publishTo := Some("Artifactory" at "http://artifactory.unbound.se/artifactory/plugins-release-local")
