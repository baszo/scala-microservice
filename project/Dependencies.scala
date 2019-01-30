import DependencyVersions._
import sbt._

object Dependencies {

  val globalExcludes = Seq(
    ExclusionRule("log4j"),
    ExclusionRule("log4j2"),
    ExclusionRule("commons-logging")
  )

  val akka = Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
    "com.typesafe.akka" %% "akka-stream" % akkaVersion
  )

  val akkaHttp = Seq(
    "com.typesafe.akka" %% "akka-http-core" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
    "de.heikoseeberger" %% "akka-http-json4s" % akkaJson4sVersion,
    "org.json4s" %% "json4s-native" % json4sVersion,
    "org.json4s" %% "json4s-jackson" % json4sVersion,
    "org.json4s" %% "json4s-core" % json4sVersion,
    "ch.megard" %% "akka-http-cors" % corsVersion,
    "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % "test"
  )

  val akkaPersistence = Seq(
    "com.typesafe.akka" %% "akka-persistence" % akkaVersion,
    "com.typesafe.akka" %% "akka-persistence-query" % akkaVersion,
    "com.typesafe.akka" %% "akka-persistence-cassandra" % cassandraVersion,
    "org.iq80.leveldb" % "leveldb" % leveldbVersion % "test",
    "org.fusesource.leveldbjni" % "leveldbjni-all" % leveldbjniVersion % "test",
    "com.github.dnvriend" %% "akka-persistence-inmemory" % akkaInMemVersion
  )

  val kafka = Seq(
    "com.typesafe.akka" %% "akka-stream-kafka" % akkaStreamKafkaVersion,
    "net.manub" %% "scalatest-embedded-kafka" % embeddedKafkaVersion % "test"
  )

  val crypto = Seq(
    "org.bouncycastle" % "bcpkix-jdk15on" % bouncycastleVersion
  )

  val jwt = Seq(
    "com.pauldijou" %% "jwt-json4s-native" % jwtJsonVersion,
    "com.pauldijou" %% "jwt-json-common" % "0.14.1" force
  )
  
  val jwtIgl = Seq(
    "io.igl" %% "jwt" %jwtiglVersion
  )

  val hash = Seq(
    "com.roundeights" %% "hasher" % hasherVersion
  )


  val mongo = Seq(
    "org.reactivemongo" %% "reactivemongo" % mongoVersion,
    "de.flapdoodle.embed" % "de.flapdoodle.embed.mongo" % embedMongoVersion % "test"
  )


  val email = Seq(
    "com.sun.mail" % "javax.mail" % mailVersion,
    "org.jvnet.mock-javamail" % "mock-javamail" % "1.9" % "test"
  )

  val bcrypt = Seq(
    "com.github.t3hnar" %% "scala-bcrypt" % bcryptVersion
  )

 
  val cassandraClient = Seq(
	"com.outworkers" % "phantom-dsl_2.12" % phantomVersion,
	"org.cassandraunit" % "cassandra-unit" % "3.3.0.2" % "test"
  )

  val awsS3 = Seq(
    "com.amazonaws" % "aws-java-sdk-s3" % awsVersion,
    "fi.solita.clamav" % "clamav-client" % clamAVVersion
  )

  val pdf = Seq(
    "com.lihaoyi" %% "scalatags" % scalaTagsVersion,
    "io.github.cloudify" %% "spdf" % spdfVersion
  )

  val devOps = Seq(
    "akka-http-lb" %% "akka-http-lb" % akkaHttpLbVersion
  )

  val test = Seq(
    "org.scalactic" %% "scalactic" % scalaTestVersion % "test",
    "org.scalatest" %% "scalatest" % scalaTestVersion % "test"
  )

  val logging = Seq(
    "ch.qos.logback" % "logback-classic" % logbackVersion,
    "ch.qos.logback" % "logback-core" % logbackVersion,
    "org.slf4j" % "jcl-over-slf4j" % slf4jVersion,
    "org.slf4j" % "log4j-over-slf4j" % slf4jVersion,
    "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingVersion,
    "com.typesafe" % "config" % configVersion
  )

  val common = Seq(devOps, test, logging).reduce(_++_)

  val projectDependencies = ProjectDependencies.projectDependencies

  val additionalResolvers = Seq(
    "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases/",
    "RoundEights" at "http://maven.spikemark.net/roundeights"
  )

}
