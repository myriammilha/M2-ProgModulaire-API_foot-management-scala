package org.openapitools.server.api

import org.apache.pekko.http.scaladsl.server.Directives._
import org.apache.pekko.http.scaladsl.server.Route
import org.apache.pekko.http.scaladsl.model.StatusCodes
import org.apache.pekko.http.scaladsl.marshalling.ToEntityMarshaller
import org.apache.pekko.http.scaladsl.unmarshalling.FromEntityUnmarshaller
import org.apache.pekko.http.scaladsl.unmarshalling.FromStringUnmarshaller
import org.openapitools.server.AkkaHttpHelper._
import org.openapitools.server.model.ModelMatch
import org.openapitools.server.model.Player
import org.openapitools.server.model.Team


class DefaultApi(
    defaultService: DefaultApiService,
    defaultMarshaller: DefaultApiMarshaller
) {

  
  import defaultMarshaller._

  lazy val route: Route =
    path("matches") { 
      post {  
            entity(as[ModelMatch]){ modelMatch =>
              defaultService.createMatch(modelMatch = modelMatch)
            }
      }
    } ~
    path("players") { 
      post {  
            entity(as[Player]){ player =>
              defaultService.createPlayer(player = player)
            }
      }
    } ~
    path("teams") { 
      post {  
            entity(as[Team]){ team =>
              defaultService.createTeam(team = team)
            }
      }
    } ~
    path("matches" / Segment) { (id) => 
      delete {  
            defaultService.deleteMatch(id = id)
      }
    } ~
    path("players" / Segment) { (id) => 
      delete {  
            defaultService.deletePlayer(id = id)
      }
    } ~
    path("teams" / Segment) { (id) => 
      delete {  
            defaultService.deleteTeam(id = id)
      }
    } ~
    path("matches") { 
      get {  
            defaultService.getMatches()
      }
    } ~
    path("players") { 
      get {  
            defaultService.getPlayers()
      }
    } ~
    path("teams") { 
      get {  
            defaultService.getTeams()
      }
    }
}


trait DefaultApiService {

  def createMatch201(responseModelMatch: ModelMatch)(implicit toEntityMarshallerModelMatch: ToEntityMarshaller[ModelMatch]): Route =
    complete((201, responseModelMatch))
  /**
   * Code: 201, Message: Match ajouté avec succès, DataType: ModelMatch
   */
  def createMatch(modelMatch: ModelMatch)
      (implicit toEntityMarshallerModelMatch: ToEntityMarshaller[ModelMatch]): Route

  def createPlayer201(responsePlayer: Player)(implicit toEntityMarshallerPlayer: ToEntityMarshaller[Player]): Route =
    complete((201, responsePlayer))
  /**
   * Code: 201, Message: Joueur ajouté avec succès, DataType: Player
   */
  def createPlayer(player: Player)
      (implicit toEntityMarshallerPlayer: ToEntityMarshaller[Player]): Route

  def createTeam201(responseTeam: Team)(implicit toEntityMarshallerTeam: ToEntityMarshaller[Team]): Route =
    complete((201, responseTeam))
  /**
   * Code: 201, Message: Équipe ajoutée avec succès, DataType: Team
   */
  def createTeam(team: Team)
      (implicit toEntityMarshallerTeam: ToEntityMarshaller[Team]): Route

  def deleteMatch200: Route =
    complete((200, "Match supprimé avec succès"))
  def deleteMatch404: Route =
    complete((404, "Match non trouvé"))
  /**
   * Code: 200, Message: Match supprimé avec succès
   * Code: 404, Message: Match non trouvé
   */
  def deleteMatch(id: String): Route

  def deletePlayer200: Route =
    complete((200, "Joueur supprimé avec succès"))
  def deletePlayer404: Route =
    complete((404, "Joueur non trouvé"))
  /**
   * Code: 200, Message: Joueur supprimé avec succès
   * Code: 404, Message: Joueur non trouvé
   */
  def deletePlayer(id: String): Route

  def deleteTeam200: Route =
    complete((200, "Équipe supprimée avec succès"))
  def deleteTeam404: Route =
    complete((404, "Équipe non trouvée"))
  /**
   * Code: 200, Message: Équipe supprimée avec succès
   * Code: 404, Message: Équipe non trouvée
   */
  def deleteTeam(id: String): Route

  def getMatches200(responseModelMatcharray: Seq[ModelMatch])(implicit toEntityMarshallerModelMatcharray: ToEntityMarshaller[Seq[ModelMatch]]): Route =
    complete((200, responseModelMatcharray))
  /**
   * Code: 200, Message: Liste des matchs, DataType: Seq[ModelMatch]
   */
  def getMatches()
      (implicit toEntityMarshallerModelMatcharray: ToEntityMarshaller[Seq[ModelMatch]]): Route

  def getPlayers200(responsePlayerarray: Seq[Player])(implicit toEntityMarshallerPlayerarray: ToEntityMarshaller[Seq[Player]]): Route =
    complete((200, responsePlayerarray))
  /**
   * Code: 200, Message: Liste des joueurs, DataType: Seq[Player]
   */
  def getPlayers()
      (implicit toEntityMarshallerPlayerarray: ToEntityMarshaller[Seq[Player]]): Route

  def getTeams200(responseTeamarray: Seq[Team])(implicit toEntityMarshallerTeamarray: ToEntityMarshaller[Seq[Team]]): Route =
    complete((200, responseTeamarray))
  /**
   * Code: 200, Message: Liste des équipes, DataType: Seq[Team]
   */
  def getTeams()
      (implicit toEntityMarshallerTeamarray: ToEntityMarshaller[Seq[Team]]): Route

}

trait DefaultApiMarshaller {
  implicit def fromEntityUnmarshallerPlayer: FromEntityUnmarshaller[Player]

  implicit def fromEntityUnmarshallerTeam: FromEntityUnmarshaller[Team]

  implicit def fromEntityUnmarshallerModelMatch: FromEntityUnmarshaller[ModelMatch]



  implicit def toEntityMarshallerPlayer: ToEntityMarshaller[Player]

  implicit def toEntityMarshallerModelMatcharray: ToEntityMarshaller[Seq[ModelMatch]]

  implicit def toEntityMarshallerTeam: ToEntityMarshaller[Team]

  implicit def toEntityMarshallerModelMatch: ToEntityMarshaller[ModelMatch]

  implicit def toEntityMarshallerTeamarray: ToEntityMarshaller[Seq[Team]]

  implicit def toEntityMarshallerPlayerarray: ToEntityMarshaller[Seq[Player]]

}

