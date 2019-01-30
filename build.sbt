
name := "bare-service"

version := "0.1"

enablePlugins(SbtNativePackager)
enablePlugins(JavaAppPackaging)
enablePlugins(DockerPlugin)

dockerBaseImage := "openjdk:8"
daemonUser := "root"
dockerRepository := Some("docker-repository-address")
dockerExposedPorts := Seq(8080)

(javaOptions in Universal) ++= CommonSettings.javaOptions

PB.targets in Compile := Seq(
  scalapb.gen(flatPackage=true) -> (sourceManaged in Compile).value
)

lazy val `order-service` = project.in(file("."))
  .settings(CommonSettings.commonSettings: _*)
  .settings(resolvers ++= Dependencies.additionalResolvers)
  .settings(excludeDependencies ++= Dependencies.globalExcludes)
  .settings(libraryDependencies ++= Dependencies.projectDependencies)
