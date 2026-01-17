package fr.football.service

import fr.football.actors._
import org.apache.pekko.actor.typed.{ActorRef, ActorSystem}
import org.apache.pekko.actor.typed.scaladsl.AskPattern._
import org.apache.pekko.util.Timeout
import org.openapitools.server.api.DefaultApiService
import org.openapitools.server.model.{ModelMatch, Player, Team}
import org.apache.pekko.http.scaladsl.server.Route
import org.apache.pekko.http.scaladsl.server.Directives._
import org.apache.pekko.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import org.apache.pekko.http.scaladsl.marshalling.ToEntityMarshaller
import org.apache.pekko.http.scaladsl.model.StatusCodes

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration._

class FootballApiServiceImpl(implicit val system: ActorSystem[_]) extends DefaultApiService with JsonSupport {
  implicit val timeout: Timeout = 5.seconds
  implicit val ec: ExecutionContext = system.executionContext

  private val teamActor = system.systemActorOf(TeamActor(), "teamActor")
  private val playerActor = system.systemActorOf(PlayerActor(), "playerActor")
  private val matchActor = system.systemActorOf(MatchActor(), "matchActor")

  override def createTeam(team: Team)(implicit toEntityMarshallerTeam: ToEntityMarshaller[Team]): Route = {
    onSuccess(teamActor.ask(ref => CreateTeam(team, ref))) {
      case TeamCreated(createdTeam) => complete(StatusCodes.Created, createdTeam)
      case TeamAlreadyExists => complete(StatusCodes.Conflict, "Team already exists")
      case _ => complete(StatusCodes.InternalServerError, "Failed to create team")
    }
  }

  override def getTeams()(implicit toEntityMarshallerTeamarray: ToEntityMarshaller[Seq[Team]]): Route = {
    onSuccess(teamActor.ask(ref => GetTeams(ref))) {
      case Teams(teams) => complete(StatusCodes.OK, teams)
      case _ => complete(StatusCodes.InternalServerError, "Failed to get teams")
    }
  }

  override def deleteTeam(teamId: String): Route = {
    onSuccess(teamActor.ask(ref => DeleteTeam(teamId, ref))) {
      case TeamDeleted(_) => complete(StatusCodes.OK, "Team deleted")
      case TeamNotFound => complete(StatusCodes.NotFound, "Team not found")
      case _ => complete(StatusCodes.InternalServerError, "Failed to delete team")
    }
  }

  override def createPlayer(player: Player)(implicit toEntityMarshallerPlayer: ToEntityMarshaller[Player]): Route = {
    onSuccess(playerActor.ask[Response](ref => CreatePlayer(player, ref))) {
      case PlayerCreated(createdPlayer) => complete(StatusCodes.Created, createdPlayer)
      case PlayerAlreadyExists => complete(StatusCodes.Conflict, "Player already exists")
      case PlayerTeamNotFound => complete(StatusCodes.NotFound, "Team not found")
      case PlayerCreationFailed => complete(StatusCodes.InternalServerError, "Failed to create player")
      case PlayerCreationInProgress => complete(StatusCodes.Accepted, "Player creation in progress")
      case _ => complete(StatusCodes.InternalServerError, "Unexpected error while creating player")
    }
  }

  override def getPlayers()(implicit toEntityMarshallerPlayerarray: ToEntityMarshaller[Seq[Player]]): Route = {
    onSuccess(playerActor.ask(ref => GetPlayers(ref))) {
      case Players(players) => complete(StatusCodes.OK, players)
      case _ => complete(StatusCodes.InternalServerError, "Failed to get players")
    }
  }

  override def deletePlayer(playerId: String): Route = {
    onSuccess(playerActor.ask(ref => DeletePlayer(playerId, ref))) {
      case PlayerDeleted(_) => complete(StatusCodes.OK, "Player deleted")
      case PlayerNotFound => complete(StatusCodes.NotFound, "Player not found")
      case _ => complete(StatusCodes.InternalServerError, "Failed to delete player")
    }
  }

  override def createMatch(modelMatch: ModelMatch)(implicit toEntityMarshallerModelMatch: ToEntityMarshaller[ModelMatch]): Route = {
    onSuccess(matchActor.ask[Response](ref => CreateMatch(modelMatch, ref))) {
      case MatchCreated(createdMatch) => complete(StatusCodes.Created, createdMatch)
      case MatchAlreadyExists => complete(StatusCodes.Conflict, "Match already exists")
      case TeamsNotFound => complete(StatusCodes.NotFound, "One or both teams not found")
      case MatchCreationFailed => complete(StatusCodes.InternalServerError, "Failed to create match")
      case MatchCreationInProgress => complete(StatusCodes.Accepted, "Match creation in progress")
      case _ => complete(StatusCodes.InternalServerError, "Unexpected error while creating match")
    }
  }

  override def getMatches()(implicit toEntityMarshallerModelMatcharray: ToEntityMarshaller[Seq[ModelMatch]]): Route = {
    onSuccess(matchActor.ask(ref => GetMatches(ref))) {
      case Matches(matches) => complete(StatusCodes.OK, matches)
      case _ => complete(StatusCodes.InternalServerError, "Failed to get matches")
    }
  }

  override def deleteMatch(matchId: String): Route = {
    onSuccess(matchActor.ask(ref => DeleteMatch(matchId, ref))) {
      case MatchDeleted(_) => complete(StatusCodes.OK, "Match deleted")
      case MatchNotFound => complete(StatusCodes.NotFound, "Match not found")
      case _ => complete(StatusCodes.InternalServerError, "Failed to delete match")
    }
  }
}
