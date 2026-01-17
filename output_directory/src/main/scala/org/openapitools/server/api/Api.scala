package org.openapitools.server.api

import org.apache.pekko.http.scaladsl.server.Directives._
import org.apache.pekko.http.scaladsl.server.Route
import org.apache.pekko.http.scaladsl.model.StatusCodes
import org.openapitools.server.model._
import org.openapitools.server.model.JsonFormats._
import spray.json._
import org.apache.pekko.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import org.apache.pekko.actor.typed.{ActorRef, ActorSystem}
import org.apache.pekko.util.Timeout
import scala.concurrent.duration._
import org.openapitools.server.actors._
import org.apache.pekko.actor.typed.scaladsl.AskPattern._

class TeamApi(teamActor: ActorRef[TeamActor.Command])(implicit system: ActorSystem[_]) {
  import system.executionContext
  implicit val timeout: Timeout = 3.seconds

  def routes: Route = pathPrefix("teams") {
    import TeamActor._
    concat(
      get {
        val futureTeams = teamActor.ask[Response](GetTeams.apply)
        onComplete(futureTeams) {
          case scala.util.Success(Teams(teams)) => complete(teams)
          case _ => complete(StatusCodes.InternalServerError)
        }
      },
      post {
        entity(as[Team]) { team =>
          val futureCreated = teamActor.ask[Response](replyTo => CreateTeam(team, replyTo))
          onComplete(futureCreated) {
            case scala.util.Success(TeamCreated(createdTeam)) => complete(StatusCodes.Created -> createdTeam)
            case _ => complete(StatusCodes.InternalServerError)
          }
        }
      }
    )
  }
}

class PlayerApi(playerActor: ActorRef[PlayerActor.Command])(implicit system: ActorSystem[_]) {
  import system.executionContext
  implicit val timeout: Timeout = 3.seconds

  def routes: Route = pathPrefix("players") {
    import PlayerActor._
    concat(
      get {
        val futurePlayers = playerActor.ask[Response](GetPlayers.apply)
        onComplete(futurePlayers) {
          case scala.util.Success(Players(players)) => complete(players)
          case _ => complete(StatusCodes.InternalServerError)
        }
      },
      post {
        entity(as[Player]) { player =>
          val futureCreated = playerActor.ask[Response](replyTo => CreatePlayer(player, replyTo))
          onComplete(futureCreated) {
            case scala.util.Success(PlayerCreated(createdPlayer)) => complete(StatusCodes.Created -> createdPlayer)
            case _ => complete(StatusCodes.InternalServerError)
          }
        }
      }
    )
  }
}

class MatchApi(matchActor: ActorRef[MatchActor.Command])(implicit system: ActorSystem[_]) {
  import system.executionContext
  implicit val timeout: Timeout = 3.seconds

  def routes: Route = pathPrefix("matches") {
    import MatchActor._
    concat(
      get {
        val futureMatches = matchActor.ask[Response](GetMatches.apply)
        onComplete(futureMatches) {
          case scala.util.Success(Matches(matches)) => complete(matches)
          case _ => complete(StatusCodes.InternalServerError)
        }
      },
      post {
        entity(as[Match]) { matchData =>
          val futureCreated = matchActor.ask[Response](replyTo => CreateMatch(matchData, replyTo))
          onComplete(futureCreated) {
            case scala.util.Success(MatchCreated(createdMatch)) => complete(StatusCodes.Created -> createdMatch)
            case _ => complete(StatusCodes.InternalServerError)
          }
        }
      }
    )
  }
}
