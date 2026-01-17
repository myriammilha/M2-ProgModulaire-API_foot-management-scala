package fr.football.actors

import org.openapitools.server.model.{ModelMatch, Player, Team}
import org.apache.pekko.actor.typed.ActorRef

sealed trait Message
sealed trait Response extends Message

// Team Messages
case class CreateTeam(team: Team, replyTo: ActorRef[Response]) extends Message
case class GetTeams(replyTo: ActorRef[Response]) extends Message
case class DeleteTeam(teamId: String, replyTo: ActorRef[Response]) extends Message
case class CheckTeamExists(teamId: String, replyTo: ActorRef[Response]) extends Message

// Team Responses
case class TeamCreated(team: Team) extends Response
case class Teams(teams: Seq[Team]) extends Response
case class TeamDeleted(teamId: String) extends Response
case object TeamAlreadyExists extends Response
case object TeamNotFound extends Response
case class TeamExistsResponse(exists: Boolean) extends Response

// Player Messages
case class CreatePlayer(player: Player, replyTo: ActorRef[Response]) extends Message
case class GetPlayers(replyTo: ActorRef[Response]) extends Message
case class DeletePlayer(playerId: String, replyTo: ActorRef[Response]) extends Message

// Player Responses
case class PlayerCreated(player: Player) extends Response
case class Players(players: Seq[Player]) extends Response
case class PlayerDeleted(playerId: String) extends Response
case object PlayerAlreadyExists extends Response
case object PlayerNotFound extends Response
case object PlayerTeamNotFound extends Response
case object PlayerCreationFailed extends Response
case object PlayerCreationInProgress extends Response

// Match Messages
case class CreateMatch(match_ : ModelMatch, replyTo: ActorRef[Response]) extends Message
case class GetMatches(replyTo: ActorRef[Response]) extends Message
case class DeleteMatch(matchId: String, replyTo: ActorRef[Response]) extends Message

// Match Responses
case class MatchCreated(match_ : ModelMatch) extends Response
case class Matches(matches: Seq[ModelMatch]) extends Response
case class MatchDeleted(matchId: String) extends Response
case object MatchAlreadyExists extends Response
case object MatchNotFound extends Response
case object TeamsNotFound extends Response
case object MatchCreationFailed extends Response
case object MatchCreationInProgress extends Response
