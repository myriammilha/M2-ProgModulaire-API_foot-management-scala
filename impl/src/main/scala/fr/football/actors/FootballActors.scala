package fr.football.actors

import org.apache.pekko.actor.typed.{ActorRef, Behavior}
import org.apache.pekko.actor.typed.scaladsl.Behaviors
import org.openapitools.server.model.{ModelMatch, Player, Team}

object TeamActor {
  private var teams = Map[String, Team]()

  def apply(): Behavior[Message] = Behaviors.receive { (context, message) =>
    message match {
      case CreateTeam(team, replyTo) =>
        if (teams.exists(_._2.name == team.name)) {
          replyTo ! TeamAlreadyExists
        } else {
          teams += (team.id -> team)
          replyTo ! TeamCreated(team)
        }
        Behaviors.same

      case GetTeams(replyTo) =>
        replyTo ! Teams(teams.values.toSeq)
        Behaviors.same

      case DeleteTeam(teamId, replyTo) =>
        if (teams.contains(teamId)) {
          teams -= teamId
          replyTo ! TeamDeleted(teamId)
        } else {
          replyTo ! TeamNotFound
        }
        Behaviors.same

      case CheckTeamExists(teamId, replyTo) =>
        replyTo ! TeamExistsResponse(teams.contains(teamId))
        Behaviors.same

      case _ => Behaviors.same
    }
  }
}

object PlayerActor {
  private var players = Map[String, Player]()

  def apply(): Behavior[Message] = Behaviors.receive { (context, message) =>
    message match {
      case CreatePlayer(player, replyTo) =>
        if (players.values.exists(p => p.name == player.name)) {
          replyTo ! PlayerAlreadyExists
        } else {
          val adapter = context.messageAdapter[Response] { response =>
            response match {
              case TeamExistsResponse(exists) =>
                if (exists) {
                  players += (player.id -> player)
                  replyTo ! PlayerCreated(player)
                } else {
                  replyTo ! PlayerTeamNotFound
                }
                PlayerCreationInProgress
              case _ =>
                replyTo ! PlayerCreationFailed
                PlayerCreationInProgress
            }
          }
          val teamActor = context.spawn(TeamActor(), s"teamActor-${java.util.UUID.randomUUID()}")
          teamActor ! CheckTeamExists(player.teamId, adapter)
        }
        Behaviors.same

      case GetPlayers(replyTo) =>
        replyTo ! Players(players.values.toSeq)
        Behaviors.same

      case DeletePlayer(playerId, replyTo) =>
        if (players.contains(playerId)) {
          players -= playerId
          replyTo ! PlayerDeleted(playerId)
        } else {
          replyTo ! PlayerNotFound
        }
        Behaviors.same

      case _ => Behaviors.same
    }
  }
}

object MatchActor {
  private var matches = Map[String, ModelMatch]()

  def apply(): Behavior[Message] = Behaviors.receive { (context, message) =>
    message match {
      case CreateMatch(matchData, replyTo) =>
        if (matches.values.exists(m => 
          m.team1Id == matchData.team1Id && 
          m.team2Id == matchData.team2Id)) {
          replyTo ! MatchAlreadyExists
        } else {
          val adapter = context.messageAdapter[Response] { response =>
            response match {
              case TeamExistsResponse(team1Exists) =>
                if (team1Exists) {
                  val innerAdapter = context.messageAdapter[Response] { innerResponse =>
                    innerResponse match {
                      case TeamExistsResponse(team2Exists) =>
                        if (team2Exists) {
                          matches += (matchData.id -> matchData)
                          replyTo ! MatchCreated(matchData)
                        } else {
                          replyTo ! TeamsNotFound
                        }
                        MatchCreationInProgress
                      case _ =>
                        replyTo ! MatchCreationFailed
                        MatchCreationInProgress
                    }
                  }
                  val teamActor = context.spawn(TeamActor(), s"teamActor-${java.util.UUID.randomUUID()}")
                  teamActor ! CheckTeamExists(matchData.team2Id, innerAdapter)
                  MatchCreationInProgress
                } else {
                  replyTo ! TeamsNotFound
                  MatchCreationInProgress
                }
              case _ =>
                replyTo ! MatchCreationFailed
                MatchCreationInProgress
            }
          }
          val teamActor = context.spawn(TeamActor(), s"teamActor-${java.util.UUID.randomUUID()}")
          teamActor ! CheckTeamExists(matchData.team1Id, adapter)
        }
        Behaviors.same

      case GetMatches(replyTo) =>
        replyTo ! Matches(matches.values.toSeq)
        Behaviors.same

      case DeleteMatch(matchId, replyTo) =>
        if (matches.contains(matchId)) {
          matches -= matchId
          replyTo ! MatchDeleted(matchId)
        } else {
          replyTo ! MatchNotFound
        }
        Behaviors.same

      case _ => Behaviors.same
    }
  }
}
