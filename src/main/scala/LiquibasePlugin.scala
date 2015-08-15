
package com.github.bigtoast.sbtliquibase

import sbt._
import classpath._
import Process._
import Keys._
import java.io.{File, PrintStream}
import java.text.SimpleDateFormat
import liquibase.integration.commandline.CommandLineUtils
import liquibase.resource.FileSystemResourceAccessor
import liquibase.database.Database
import liquibase.Liquibase
import liquibase.CatalogAndSchema
import liquibase.diff.output.DiffOutputControl

object LiquibasePlugin extends Plugin {

  val dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

  val liquibaseUpdate            = TaskKey[Unit]("liquibase-update", "Run a liquibase migration")
  val liquibaseStatus            = TaskKey[Unit]("liquibase-status", "Print count of unrun change sets")
  val liquibaseClearChecksums    = TaskKey[Unit]("liquibase-clear-checksums", "Removes all saved checksums from database log. Useful for 'MD5Sum Check Failed' errors")
  val liquibaseListLocks         = TaskKey[Unit]("liquibase-list-locks", "Lists who currently has locks on the database changelog")
  val liquibaseReleaseLocks      = TaskKey[Unit]("liquibase-release-locks", "Releases all locks on the database changelog")
  val liquibaseValidateChangelog = TaskKey[Unit]("liquibase-validate-changelog", "Checks changelog for errors")
  val liquibaseTag               = InputKey[Unit]("liquibase-tag", "Tags the current database state for future rollback")
  val liquibaseDbDiff            = TaskKey[Unit]("liquibase-db-diff", "( this isn't implemented yet ) Generate changeSet(s) to make Test DB match Development")
  val liquibaseDbDoc             = TaskKey[Unit]("liquibase-db-doc", "Generates Javadoc-like documentation based on current database and change log")
  val liquibaseGenerateChangelog = TaskKey[Unit]("liquibase-generate-changelog", "Writes Change Log XML to copy the current state of the database to standard out")
  val liquibaseChangelogSyncSql  = TaskKey[Unit]("liquibase-changelog-sync-sql", "Writes SQL to mark all changes as executed in the database to STDOUT")
  val liquibaseDropAll           = TaskKey[Unit]("liquibase-drop-all", "Drop all database objects owned by user")

  val liquibaseRollback          = InputKey[Unit]("liquibase-rollback", "<tag> Rolls back the database to the the state is was when the tag was applied")
  val liquibaseRollbackSql       = InputKey[Unit]("liquibase-rollback-sql", "<tag> Writes SQL to roll back the database to that state it was in when the tag was applied to STDOUT")
  val liquibaseRollbackCount     = InputKey[Unit]("liquibase-rollback-count", "<num>Rolls back the last <num> change sets applied to the database")
  val liquibaseRollbackCountSql  = InputKey[Unit]("liquibase-rollback-count-sql", "<num> Writes SQL to roll back the last <num> change sets to STDOUT applied to the database")
  val liquibaseRollbackToDate    = InputKey[Unit]("liquibase-rollback-to-date", "<date> Rolls back the database to the the state is was at the given date/time. Date Format: yyyy-MM-dd HH:mm:ss")
  val liquibaseRollbackToDateSql = InputKey[Unit]("liquibase-rollback-to-date-sql", "<date> Writes SQL to roll back the database to that state it was in at the given date/time version to STDOUT")
  val liquibaseFutureRollbackSql = InputKey[Unit]("liquibase-future-rollback-sql", " Writes SQL to roll back the database to the current state after the changes in the changelog have been applied")

  val liquibaseChangelog = SettingKey[String]("liquibase-changelog", "This is your liquibase changelog file to run.")
  val liquibaseUrl       = SettingKey[String]("liquibase-url", "The url for liquibase")
  val liquibaseUsername  = SettingKey[String]("liquibase-username", "username yo.")
  val liquibasePassword  = SettingKey[String]("liquibase-password", "password")
  val liquibaseDriver    = SettingKey[String]("liquibase-driver", "driver")
  val liquibaseDefaultCatalog = SettingKey[Option[String]]("liquibase-default-catalog","default catalog")
  val liquibaseDefaultSchemaName = SettingKey[Option[String]]("liquibase-default-schema-name","default schema name")
  val liquibaseChangelogCatalog = SettingKey[Option[String]]("liquibase-changelog-catalog", "changelog catalog")
  val liquibaseChangelogSchemaName = SettingKey[Option[String]]("liquibase-changelog-schema-name", "changelog schema name")
  val liquibaseContext = SettingKey[String]("liquibase-context","changeSet contexts to execute")

  lazy val liquibaseDatabase = TaskKey[Database]("liquibase-database", "the database")
  lazy val liquibase = TaskKey[Liquibase]("liquibase", "liquibase object")

  lazy val liquibaseSettings :Seq[Setting[_]] = Seq[Setting[_]](
    liquibaseDefaultCatalog := None,
    liquibaseDefaultSchemaName := None,
    liquibaseChangelogCatalog := None,
    liquibaseChangelogSchemaName := None,
    liquibaseChangelog := "src/main/migrations/changelog.xml",
    liquibaseContext := "",
    //changelog <<= baseDirectory( _ / "src" / "main" / "migrations" /  "changelog.xml" absolutePath ),


  liquibaseDatabase <<= (
      liquibaseUrl,
      liquibaseUsername,
      liquibasePassword,
      liquibaseDriver,
      liquibaseDefaultCatalog,
      liquibaseDefaultSchemaName,
      liquibaseChangelogCatalog,
      liquibaseChangelogSchemaName,
      fullClasspath in Runtime
    ) map { (
        url,
        uname,
        pass,
        driver,
        liquibaseDefaultCatalog,
        liquibaseDefaultSchemaName,
        liquibaseChangelogCatalog,
        liquibaseChangelogSchemaName,
        cpath) =>
      CommandLineUtils.createDatabaseObject(
          ClasspathUtilities.toLoader(cpath.map(_.data)),
          url,
          uname,
          pass,
          driver,
          liquibaseDefaultCatalog.getOrElse(null),
          liquibaseDefaultSchemaName.getOrElse(null),
          false, // outputDefaultCatalog
          true, // outputDefaultSchema
          null, // databaseClass
          null, // driverPropertiesFile
          null, // propertyProviderClass
          liquibaseChangelogCatalog.getOrElse(null),
          liquibaseChangelogSchemaName.getOrElse(null)
      )
  },

  liquibase <<= ( liquibaseChangelog, liquibaseDatabase ) map {
    ( cLog :String, dBase :Database ) =>
      new Liquibase( cLog, new FileSystemResourceAccessor, dBase )
  },

    liquibaseUpdate <<= (liquibase, liquibaseContext) map {
      (liquibase:Liquibase, context:String) =>
        liquibase.update(context)
    },

    liquibaseStatus <<= liquibase map { _.reportStatus(true, null.asInstanceOf[String], new LoggerWriter( ConsoleLogger() ) ) },
    liquibaseClearChecksums <<= liquibase map { _.clearCheckSums() },
    liquibaseListLocks <<= (streams, liquibase) map { (out, lbase) => lbase.reportLocks( new PrintStream(out.binary()) )  },
    liquibaseReleaseLocks <<= (streams, liquibase) map { (out, lbase) => lbase.forceReleaseLocks() },
    liquibaseValidateChangelog <<= (streams, liquibase) map { (out, lbase) => lbase.validate() },
    liquibaseDbDoc <<= ( streams, liquibase, target ) map { ( out, lbase, tdir ) =>
      lbase.generateDocumentation( tdir / "liquibase-doc" absolutePath )
      out.log("Documentation generated in %s".format( tdir / "liquibase-doc" absolutePath )) },

    liquibaseRollback <<= inputTask { (argTask) =>
      ( streams, liquibase, argTask ) map { ( out, lbase, args :Seq[String] ) =>
        lbase.rollback( args.head , null.asInstanceOf[String] )
        out.log("Rolled back to tag %s".format(args.head))
      }
    },

    liquibaseRollbackCount <<= inputTask { (argTask) =>
      ( streams, liquibase, argTask ) map { ( out, lbase, args :Seq[String] ) =>
        lbase.rollback( args.head.toInt , null )
        out.log("Rolled back to count %s".format(args.head))
      }
    },

    liquibaseRollbackSql <<= inputTask { (argTask) =>
      ( streams, liquibase, argTask ) map { ( out, lbase, args :Seq[String] ) =>
        lbase.rollback( args.head , null.asInstanceOf[String], out.text() )
      }
    },

    liquibaseRollbackCountSql <<= inputTask { (argTask) =>
      ( streams, liquibase, argTask ) map { ( out, lbase, args :Seq[String] ) =>
        lbase.rollback( args.head.toInt , null.asInstanceOf[String], out.text() )
      }
    },

    liquibaseRollbackToDate <<= inputTask { (argTask) =>
      ( streams, liquibase, argTask ) map { ( out, lbase, args :Seq[String] ) =>
        lbase.rollback( dateFormat.parse( args.mkString(" ") ) , null )
      }
    },

    liquibaseRollbackToDateSql <<= inputTask { (argTask) =>
      ( streams, liquibase, argTask ) map { ( out, lbase, args :Seq[String] ) =>
        lbase.rollback( dateFormat.parse(args.mkString(" ")) , null, out.text() )
      }
    },

    liquibaseFutureRollbackSql <<= inputTask { (argTask) =>
      ( streams, liquibase, argTask ) map { ( out, lbase, args :Seq[String] ) =>
        lbase.futureRollbackSQL( null, out.text() )
      }
    },

    liquibaseTag <<= inputTask { (argTask) =>
      (streams, liquibase, argTask) map { (out, lbase, args :Seq[String] ) =>
        lbase.tag(args.head)
        out.log("Tagged db with %s for future rollback if needed".format(args.head))
      }
    },

    liquibaseGenerateChangelog <<= (
        streams,
        liquibase,
        liquibaseChangelog,
        liquibaseDefaultCatalog,
        liquibaseDefaultSchemaName,
        liquibaseChangelogCatalog,
        liquibaseChangelogSchemaName,
        baseDirectory) map { (
            out,
            lbase,
            clog,
            defaultCatalog,
            defaultSchemaName,
            liquibaseChangelogCatalog,
            liquibaseChangelogSchemaName,
            bdir) =>
      CommandLineUtils.doGenerateChangeLog(
          clog,
          lbase.getDatabase(),
          defaultCatalog.getOrElse(null),
          defaultSchemaName.getOrElse(null),
          null, // snapshotTypes
          null, // author
          null, // context
          bdir / "src" / "main" / "migrations" absolutePath,
          new DiffOutputControl())
    },

    liquibaseChangelogSyncSql <<= (streams, liquibase ) map { ( out, lbase) =>
      lbase.changeLogSync(null, out.text())
    },

    liquibaseDropAll <<= liquibase map { _.dropAll() }
  )


}
