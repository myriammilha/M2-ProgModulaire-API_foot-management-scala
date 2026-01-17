package org.openapitools.server.actors

import org.apache.pekko.actor.typed.{ActorRef, Behavior}
import org.apache.pekko.actor.typed.scaladsl.Behaviors
import org.openapitools.server.model._

object TeamActor {
  sealed trait Command
  case class CreateTeam(team: Team, replyTo: ActorRef[Response]) extends Command
  case class GetTeams(replyTo: ActorRef[Response]) extends Command
  case class DeleteTeam(id: String, replyTo: ActorRef[Response]) extends Command

  sealed trait Response
  case class Teams(teams: Vector[Team]) extends Response
  case class TeamCreated(team: Team) extends Response
  case class TeamDeleted(id: String) extends Response
  case class TeamNotFound(id: String) extends Response

  def apply(): Behavior[Command] = behavior(Vector.empty)

  private def behavior(teams: Vector[Team]): Behavior[Command] = Behaviors.receive { (context, message) =>
    message match {
      case CreateTeam(team, replyTo) =>
        replyTo ! TeamCreated(team)
        behavior(teams :+ team)
      case GetTeams(replyTo) =>
        replyTo ! Teams(teams)
        Behaviors.same
      case DeleteTeam(id, replyTo) =>
        teams.find(_.id == id) match {
          case Some(_) =>
            replyTo ! TeamDeleted(id)
            behavior(teams.filterNot(_.id == id))
          case None =>
            replyTo ! TeamNotFound(id)
            Behaviors.same
        }
    }
  }
}

object PlayerActor {
  sealed trait Command
  case class CreatePlayer(player: Player, replyTo: ActorRef[Response]) extends Command
  case class GetPlayers(replyTo: ActorRef[Response]) extends Command
  case class DeletePlayer(id: String, replyTo: ActorRef[Response]) extends Command

  sealed trait Response
  case class Players(players: Vector[Player]) extends Response
  case class PlayerCreated(player: Player) extends Response
  case class PlayerDeleted(id: String) extends Response
  case class PlayerNotFound(id: String) extends Response

  def apply(): Behavior[Command] = behavior(Vector.empty)

  private def behavior(players: Vector[Player]): Behavior[Command] = Behaviors.receive { (context, message) =>
    message match {
      case CreatePlayer(player, replyTo) =>
        replyTo ! PlayerCreated(player)
        behavior(players :+ player)
      case GetPlayers(replyTo) =>
        replyTo ! Players(players)
        Behaviors.same
      case DeletePlayer(id, replyTo) =>
        players.find(_.id == id) match {
          case Some(_) =>
            replyTo ! PlayerDeleted(id)
            behavior(players.filterNot(_.id == id))
          case None =>
            replyTo ! PlayerNotFound(id)
            Behaviors.same
        }
    }
  }
}

object MatchActor {
  sealed trait Command
  case class CreateMatch(matchData: Match, replyTo: ActorRef[Response]) extends Command
  case class GetMatches(replyTo: ActorRef[Response]) extends Command
  case class DeleteMatch(id: String, replyTo: ActorRef[Response]) extends Command

  sealed trait Response
  case class Matches(matches: Vector[Match]) extends Response
  case class MatchCreated(matchData: Match) extends Response
  case class MatchDeleted(id: String) extends Response
  case class MatchNotFound(id: String) extends Response

  def apply(): Behavior[Command] = behavior(Vector.empty)

  private def behavior(matches: Vector[Match]): Behavior[Command] = Behaviors.receive { (context, message) =>
    message match {
      case CreateMatch(matchData, replyTo) =>
        replyTo ! MatchCreated(matchData)
        behavior(matches :+ matchData)
      case GetMatches(replyTo) =>
        replyTo ! Matches(matches)
        Behaviors.same
      case DeleteMatch(id, replyTo) =>
        matches.find(_.id == id) match {
          case Some(_) =>
            replyTo ! MatchDeleted(id)
            behavior(matches.filterNot(_.id == id))
          case None =>
            replyTo ! MatchNotFound(id)
            Behaviors.same
        }
    }
  }
}
