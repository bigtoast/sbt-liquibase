
sbtPlugin := true

organization := "atd"

name := "sbt-liquibase"

version := "0.4"

libraryDependencies += "org.liquibase" % "liquibase-core" % "2.0.2"

publishTo := Some(Resolver.file("bigtoast.github.com", file(Path.userHome + "/Projects/BigToast/bigtoast.github.com/repo")))

