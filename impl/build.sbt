name := "football-api-impl"
version := "1.0"
scalaVersion := "2.12.8"

lazy val root = (project in file("."))
  .dependsOn(generatedServer)
  .settings(
    libraryDependencies ++= Seq(
      "org.apache.pekko" %% "pekko-actor-typed" % "1.0.1",
      "org.apache.pekko" %% "pekko-stream" % "1.0.1",
      "org.apache.pekko" %% "pekko-http" % "1.0.0",
      "org.apache.pekko" %% "pekko-http-spray-json" % "1.0.0",
      "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5",
      "ch.qos.logback" % "logback-classic" % "1.4.7",
      "org.openapitools" %% "openapi-scala-akka-http-server" % "1.0.0"
    )
  )

lazy val generatedServer = ProjectRef(file("../output_directory"), "output_directory")
