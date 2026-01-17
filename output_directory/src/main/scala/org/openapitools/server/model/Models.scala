package org.openapitools.server.model

import spray.json._

/**
 * Modèles du domaine Football
 */
final case class Team(
  id: String,
  name: String,
  city: String
)

final case class Player(
  id: String,
  name: String,
  position: String,
  teamId: String
)

final case class Match(
  id: String,
  homeTeamId: String,
  awayTeamId: String,
  date: String,
  scoreHome: Int,
  scoreAway: Int
)

/**
 * JSON formats (Spray JSON)
 */
object JsonFormats extends DefaultJsonProtocol {
  implicit val teamFormat: RootJsonFormat[Team]     = jsonFormat3(Team)
  implicit val playerFormat: RootJsonFormat[Player] = jsonFormat4(Player)
  implicit val matchFormat: RootJsonFormat[Match]   = jsonFormat6(Match)
}
