import Dependencies._

object ProjectDependencies {

  lazy val projectDependencies = Seq(
    akka,
    akkaHttp,
    akkaPersistence,
    hash,

    common
  ).reduce(_ ++ _)

}
