package org.openapitools.server

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
    val teamActor = context.spawn(TeamActor(), "teamActor")
    val playerActor = context.spawn(PlayerActor(), "playerActor")
    val matchActor = context.spawn(MatchActor(), "matchActor")

    implicit val system: ActorSystem[_] = context.system

    val teamApi = new TeamApi(teamActor)
    val playerApi = new PlayerApi(playerActor)
    val matchApi = new MatchApi(matchActor)

    val routes: Route = concat(
      teamApi.routes,
      playerApi.routes,
      matchApi.routes
    )

    val serverHost = "localhost"
    val serverPort = 8080

    Http()
      .newServerAt(serverHost, serverPort)
      .bind(Route.toFunction(routes))

    println(s"Server online at http://$serverHost:$serverPort/")

    Behaviors.receiveMessage {
      case Stop => Behaviors.stopped
    }
  }

  implicit val system: ActorSystem[RootCommand] =
    ActorSystem(rootBehavior, "footballManagementSystem")

  implicit val executionContext: ExecutionContextExecutor =
    system.executionContext

  // Attendre indéfiniment
  Await.ready(system.whenTerminated, Duration.Inf)
}
