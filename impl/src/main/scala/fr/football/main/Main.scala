package fr.football.main

import org.apache.pekko.actor.typed.ActorSystem
import org.apache.pekko.actor.typed.scaladsl.Behaviors
import org.apache.pekko.http.scaladsl.Http
import org.apache.pekko.http.scaladsl.server.Route
import org.apache.pekko.http.scaladsl.server.Directives._

import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.duration.Duration
import scala.concurrent.Await

import org.openapitools.server.api._
import org.openapitools.server.actors._

object Main extends App {

  sealed trait RootCommand
  case object Stop extends RootCommand

  val rootBehavior = Behaviors.setup[RootCommand] { context =>

    // Création des acteurs
    val teamActor   = context.spawn(TeamActor(), "teamActor")
    val playerActor = context.spawn(PlayerActor(), "playerActor")
    val matchActor  = context.spawn(MatchActor(), "matchActor")

    implicit val system: ActorSystem[_] = context.system

    // APIs
    val teamApi   = new TeamApi(teamActor)
    val playerApi = new PlayerApi(playerActor)
    val matchApi  = new MatchApi(matchActor)

    // Routes exposées
    val routes: Route = concat(
      teamApi.routes,
      playerApi.routes,
      matchApi.routes
    )

    // Lancement du serveur
    val host = "0.0.0.0"
    val port = 8080

    Http()
      .newServerAt(host, port)
      .bind(Route.toFunction(routes))

    println(s"Server online at http://localhost:$port/")
    println("Server is running. Press Ctrl+C to stop...")

    Behaviors.receiveMessage {
      case Stop => Behaviors.stopped
    }
  }

  implicit val system: ActorSystem[RootCommand] =
    ActorSystem(rootBehavior, "FootballSystem")

  implicit val executionContext: ExecutionContextExecutor =
    system.executionContext

  // Le serveur tourne indéfiniment
  Await.ready(system.whenTerminated, Duration.Inf)
}
