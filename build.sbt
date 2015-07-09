name := """monarch-mme"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  ws,
  "org.specs2" %% "specs2" % "2.3.11" % "test"
)

// TODO tmp fix http://stackoverflow.com/questions/28104968/scala-ide-4-0-0-thinks-theres-errors-in-an-out-of-the-box-play-framework-2-3-7
EclipseKeys.createSrc := EclipseCreateSrc.All
