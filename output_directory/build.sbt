name := "openapi-scala-akka-http-server"
version := "1.0.0"
organization := "org.openapitools"

scalaVersion := "2.12.18"

// Versions centralisées
val pekkoVersion      = "1.0.2"
val pekkoHttpVersion  = "1.0.1"
val sprayJsonVersion  = "1.3.6"

libraryDependencies ++= Seq(
  // Pekko core
  "org.apache.pekko" %% "pekko-actor-typed" % pekkoVersion,
  "org.apache.pekko" %% "pekko-stream"      % pekkoVersion,

  // Pekko HTTP
  "org.apache.pekko" %% "pekko-http"            % pekkoHttpVersion,
  "org.apache.pekko" %% "pekko-http-spray-json" % pekkoHttpVersion,

  // JSON
  "io.spray" %% "spray-json" % sprayJsonVersion
)
