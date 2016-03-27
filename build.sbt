
sbtPlugin := true

organization := "com.github.bigtoast"

name := "sbt-liquibase"

version := "0.6"

crossScalaVersions := Seq("2.11.0")

libraryDependencies += "org.liquibase" % "liquibase-core" % "2.0.5"

publishTo := Some(Resolver.file("bigtoast.github.com", file(Path.userHome + "/Projects/BigToast/bigtoast.github.com/repo")))
