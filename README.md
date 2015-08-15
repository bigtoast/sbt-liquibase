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

<table>
        <tr>
                <td> <b>liquibaseUsername</b> </td>
                <td>Username for the database. This defaults to blank.</td>
        </tr>
        <tr><td></td><td>

            liquibaseUsername := "asdf"

        </td></tr>
        <tr>
                <td> <b>liquibasePassword</b> </td>
                <td>Password for the database. This defaults to blank.</td>
        </tr>
        <tr><td></td><td>

            liquibasePassword := "secretstuff"

        </td></tr>
        <tr>
                <td> <b>liqubaseDriver</b> </td>
                <td>Database driver classname. There is no default.</td>
        </tr>
        <tr><td></td><td>

            liquibaseDriver := "com.mysql.jdbc.Driver"

        </td></tr>
        <tr>
                <td> <b>liquibaseUrl</b> </td>
                <td>Database connection uri. There is no default.</td>
        </tr>
        <tr><td></td><td>

            liquibaseUrl := "jdbc:mysql://localhost:3306/mydb"

        </td></tr>
        <tr>
                <td> <b>liquibaseChangelogCatalog</b> </td>
                <td>Default catalog name for the changelog tables. This defaults to None.</td>
        </tr>
        <tr><td></td><td>

            liquibaseChangelogCatalog := Some("my_catalog")

        </td></tr>
        <tr>
                <td> <b>liquibaseChangelogSchemaName</b> </td>
                <td>Default schema name for the changelog tables. This defaults to None.</td>
        </tr>
        <tr><td></td><td>

            liquibaseChangelogSchemaName := Some("my_schema")

        </td></tr>
        <tr>
                <td> <b>liquibaseDefaultCatalog</b> </td>
                <td>Default catalog name for the db if it isn't defined in the uri. This defaults to None.</td>
        </tr>
        <tr><td></td><td>

            liquibaseDefaultCatalog := Some("my_catalog")

        </td></tr>
        <tr>
                <td> <b>liquibaseDefaultSchemaName</b> </td>
                <td>Default schema name for the db if it isn't defined in the uri. This defaults to None.</td>
        </tr>
        <tr><td></td><td>

            liquibaseDefaultSchemaName := Some("my_schema")

        </td></tr>
        <tr>
                <td> <b>liquibaseChangelog</b> </td>
                <td>Full path to your changelog file. This defaults 'src/main/migrations/changelog.xml'.</td>
        </tr>
        <tr><td></td><td>

            liquibaseChangelog := "other/path/dbchanges.xml"

        </td></tr>
</table>

## Tasks

<table>
        <tr>
                <td> <b>liquibase-update</b> </td>
                <td>Run the liquibase migration</td>
        </tr>
        <tr>
                <td><b>liquibase-status</b></td>
                <td>Print count of yet to be run changesets</td>
        </tr>
        <tr>
                <td><b>liquibase-clear-checksums</b></td>
                <td>Removes all saved checksums from database log. Useful for 'MD5Sum Check Failed' errors</td>
        </tr>
        <tr>
                <td><b>liquibase-list-locks</b></td>
                <td>Lists who currently has locks on the database changelog</td>
        </tr>
        <tr>
                <td><b>liquibase-release-locks</b></td>
                <td>Releases all locks on the database changelog.</td>
        </tr>
        <tr>
                <td><b>liquibase-validate-changelog</b></td>
                <td>Checks changelog for errors.</td>
        </tr>
        <tr>
                <td><b>liquibase-db-diff</b></td>
                <td>( this isn't implemented yet ) Generate changeSet(s) to make Test DB match Development</td>
        </tr>
        <tr>
                <td><b>liquibase-db-doc</b></td>
                <td>Generates Javadoc-like documentation based on current database and change log</td>
        </tr>
        <tr>
                <td><b>liquibase-generate-changelog</b></td>
                <td>Writes Change Log XML to copy the current state of the database to the file defined in the changelog setting</td>
        </tr>
        <tr>
                <td><b>liquibase-changelog-sync-sql</b></td>
                <td>Writes SQL to mark all changes as executed in the database to STDOUT</td>
        </tr>

        <tr>
                <td><b>liquibase-tag</b> {tag}</td>
                <td>Tags the current database state for future rollback with {tag}</td>
        </tr>
        <tr>
                <td><b>liquibase-rollback</b> {tag}</td>
                <td>Rolls back the database to the the state is was when the {tag} was applied.</td>
        </tr>
        <tr>
                <td><b>liquibase-rollback-sql</b> {tag}</td>
                <td>Writes SQL to roll back the database to that state it was in when the {tag} was applied to STDOUT</td>
        </tr>
        <tr>
                <td><b>liquibase-rollback-count</b> {int}</td>
                <td>Rolls back the last {int i} change sets applied to the database</td>
        </tr>
        <tr>
                <td><b>liquibase-rollback-sql-count</b> {int}</td>
                <td>Writes SQL to roll back the last {int i} change sets to STDOUT applied to the database</td>
        </tr>

        <tr>
                <td><b>liquibase-rollback-to-date</b> { yyyy-MM-dd HH:mm:ss }</td>
                <td>Rolls back the database to the the state it was at the given date/time. Date Format: yyyy-MM-dd HH:mm:ss</td>
        </tr>
        <tr>
                <td><b>liquibase-rollback-to-date-sql { yyyy-MM-dd HH:mm:ss }</b></td>
                <td>Writes SQL to roll back the database to that state it was in at the given date/time version to STDOUT</td>
        </tr>
        <tr>
                <td><b>liquibase-future-rollback-sql</b></td>
                <td>Writes SQL to roll back the database to the current state after the changes in the changelog have been applied.</td>
        </tr>
	<tr>
		<td><b>liquibase-drop-all</b></td>
		<td>Drop all tables</td>
	</tr>

</table>


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


