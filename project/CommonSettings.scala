import sbt.Keys._


object CommonSettings {
  val scalaVer = "2.12.4"

  val javaOptions = Seq(
    "-J-Xss1024k",
    "-J-Xms256M",
    "-J-Xmx512M",
    "-J-XX:MaxMetaspaceSize=128M",
    "-J-server",

    "-J-XX:+UseConcMarkSweepGC",
    "-J-XX:+UseCMSInitiatingOccupancyOnly",
    "-J-XX:+CMSParallelRemarkEnabled",
    "-J-XX:CMSInitiatingOccupancyFraction=70",
    "-J-XX:+ScavengeBeforeFullGC",
    "-J-XX:+CMSScavengeBeforeRemark",

    "-J-XX:+PrintGCDetails",
    "-J-Xloggc:./gc.log",
    "-J-XX:+UseGCLogFileRotation",
    "-J-XX:NumberOfGCLogFiles=10",
    "-J-XX:GCLogFileSize=100M",

    "-J-XX:+HeapDumpOnOutOfMemoryError",
    "-J-XX:HeapDumpPath=./gc-dump-`date`.hprof"
  )


  val commonSettings = Seq(
    scalaVersion := scalaVer,
    scalacOptions ++= Seq(
      "-encoding", "UTF-8"
    ),
    fork := true
  )
}