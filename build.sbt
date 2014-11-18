
sbtPlugin := true

organization := "ch.becompany"

name := "sbt-liquibase"

version := "0.6"

crossScalaVersions := Seq("2.9.2", "2.10.0")

libraryDependencies += "org.liquibase" % "liquibase-core" % "3.3.0"

publishMavenStyle := true

credentials += Credentials(Path.userHome / ".sbt" / ".credentials")

publishTo := {
  val nexus = "http://nexus.becompany.ch/nexus/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots") 
  else
    Some("releases"  at nexus + "content/repositories/releases")
}