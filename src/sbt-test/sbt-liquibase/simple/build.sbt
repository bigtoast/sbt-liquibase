import com.github.bigtoast.sbtliquibase.LiquibasePlugin

seq(LiquibasePlugin.liquibaseSettings: _*)


libraryDependencies ++= Seq(
  "com.h2database" % "h2" % "1.3.170"
)

liquibaseUsername := ""

liquibasePassword := ""

liquibaseDriver   := "org.h2.Driver"

liquibaseUrl      := "jdbc:h2:target/db/test;AUTO_SERVER=TRUE"

liquibaseChangelog := "src/main/resources/migrations/changelog.xml"


