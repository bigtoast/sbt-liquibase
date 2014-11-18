
sbtPlugin := true

organization := "com.github.bigtoast"

name := "sbt-liquibase"

version := "0.5"

crossScalaVersions := Seq("2.9.2", "2.10.0")

libraryDependencies += "org.liquibase" % "liquibase-core" % "3.3.0"

publishMavenStyle := true

publishTo := Some(Resolver.file("bigtoast.github.com", file(Path.userHome + "/Projects/BigToast/bigtoast.github.com/repo")))
