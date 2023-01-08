Liquibase plugin for sbt 0.11 / 0.12
====================================

# Instructions for use:
### Step 1: Include the plugin in your build

Add the following to your `project/plugins.sbt`:

## sbt-0.11.2 / 0.12.1 

    resolvers += "bigtoast-github" at "http://bigtoast.github.com/repo/"

    addSbtPlugin("com.github.bigtoast" % "sbt-liquibase" % "0.5")

### Step 2: Add sbt-liquibase settings to your build

Add the following to your 'build.sbt' ( if you are using build.sbt )


    import com.github.bigtoast.sbtliquibase.LiquibasePlugin

    seq(LiquibasePlugin.liquibaseSettings: _*)
    
    liquibaseUsername := ""

    liquibasePassword := ""
                        
    liquibaseDriver   := "com.mysql.jdbc.Driver"
                        
    liquibaseUrl      := "jdbc:mysql://localhost:3306/test_db?createDatabaseIfNotExist=true"

Or if you are using a build object extending from Build:

    import sbt._
    import Keys._
    import com.github.bigtoast.sbtliquibase.LiquibasePlugin._

    class MyBuildThatHasntDrankTheNoSQLKoolAid extends Build {
         lazy val seniorProject = Project("hola", file("."), settings = Defaults.defaultSettings ++ liquibaseSettings ++ Seq (
              liquibaseUsername := "dbusername",
              liquibasePassword := "kittensareevil"
              /* lots more liquibase settings available */
         )
    }


## Settings

| Property name | Property description | Example |
| ------------- | -------------------- | --------|
| liquibaseUsername | Username for the database. This defaults to blank. |  liquibaseUsername := "asdf" |
| liquibasePassword | Password for the database. This defaults to blank. | liquibasePassword := "secretstuff" |
| liqubaseDriver | Database driver classname. There is no default. | liquibaseDriver := "com.mysql.jdbc.Driver" |
| liquibaseUrl | Database connection uri. There is no default. | liquibaseUrl := "jdbc:mysql://localhost:3306/mydb" |
| liquibaseDefaultSchemaName | Default schema name for the db if it isn't defined in the uri. This defaults to null. | liquibaseDefaultSchemaName := "dbname" |
| liquibaseChangelog | Full path to your changelog file. This defaults 'src/main/migrations/changelog.xml'. | liquibaseChangelog := "other/path/dbchanges.xml"


## Tasks

|Task name| Task Description |
|---------| ---------------- |
| liquibase-update | Run the liquibase migration |
| liquibase-status | Print count of yet to be run changesets |
| liquibase-clear-checksums | Removes all saved checksums from database log. Useful for 'MD5Sum Check Failed' errors |
| liquibase-list-locks | Lists who currently has locks on the database changelog |
| liquibase-release-locks | Releases all locks on the database changelog |
| liquibase-validate-changelog | Checks changelog for errors |
| liquibase-db-diff | ( this isn't implemented yet ) Generate changeSet(s) to make Test DB match Development |
| liquibase-db-doc | Generates Javadoc-like documentation based on current database and change log |
| liquibase-generate-changelog | Writes Change Log XML to copy the current state of the database to the file defined in the changelog setting |
| liquibase-changelog-sync-sql | Writes SQL to mark all changes as executed in the database to STDOUT |
| **liquibase-tag** {tag} | Tags the current database state for future rollback with {tag} |
| **liquibase-rollback**</b>** {tag} | Rolls back the database to the the state is was when the {tag} was applied. |
| **liquibase-rollback-sql** {tag} | Writes SQL to roll back the database to that state it was in when the {tag} was applied to STDOUT |
| **liquibase-rollback-count** {int} | Rolls back the last {int i} change sets applied to the database |
|**liquibase-rollback-sql-count** {int} | Writes SQL to roll back the last {int i} change sets to STDOUT applied to the database |
| **liquibase-rollback-to-date** { yyyy-MM-dd HH:mm:ss } | >Rolls back the database to the the state it was at the given date/time. Date Format: yyyy-MM-dd HH:mm:ss |
| **liquibase-rollback-to-date-sql** { yyyy-MM-dd HH:mm:ss } | Writes SQL to roll back the database to that state it was in at the given date/time version to STDOUT |
| liquibase-future-rollback-sql | Writes SQL to roll back the database to the current state after the changes in the changelog have been applied.</td> |
| liquibase-drop-all | Drop all tables |

Notes
------------------
I needed liquibase in an sbt project and after a little encouragement from a recent BASE (Bay Area Scala Enthusiasts) meeting I thought 
I would hack it out. We have been using it in production for a few months now and haven't had any real problems other than to make 
sure that the changelog path is relative ( which it is by default ). The reason for using a relative path is that liquibase stores the 
path to the changelog in the DATABASECHANGELOG table and the path is used to detect if a changelog has already been run. If you use absolute
paths and then run your migrations from somewhere else on your computer ( you moved your project for example ) the migration will fail because
liquibase will try to run the same migrations again.

If any bugs are found or features wanted please file an issue in the github project. I will do my best to accommodate.


Acknoledgements
---------------
I used the following plugins as reference

 * sbt-liquibase plugin for sbt 0.7
 * grails liquibase plugin
 * sbt-protobuf plugin for sbt 0.10


