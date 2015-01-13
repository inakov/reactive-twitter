import play.PlayScala

scalaVersion := "2.11.1"

name := """reactive-twitter"""

version := "1.0"

libraryDependencies ++= Seq(
  "com.mohiva" %% "play-silhouette" % "1.0",
  "org.webjars" %% "webjars-play" % "2.3.0",
  "org.webjars" % "bootstrap" % "3.1.1",
  "org.webjars" % "jquery" % "1.11.0",
  "net.codingwell" %% "scala-guice" % "4.0.0-beta4",
  "org.reactivemongo" %% "play2-reactivemongo" % "0.10.5.0.akka23",
  cache
)

lazy val root = (project in file(".")).enablePlugins(PlayScala)
