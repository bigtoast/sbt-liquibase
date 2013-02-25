
resolvers += Classpaths.typesafeResolver

libraryDependencies <+= sbtVersion(sv => sv match {
  case "0.11.3" => "org.scala-sbt" % "scripted-sbt_2.9.1" % sv
  case _ => "org.scala-sbt" % "scripted-sbt" % sv
})

libraryDependencies <+= sbtVersion(sv => sv match {
  case "0.11.3" => "org.scala-sbt" % "scripted-plugin_2.9.1" % sv
  case _ => "org.scala-sbt" % "scripted-plugin" % sv
})


addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "2.0.0")
