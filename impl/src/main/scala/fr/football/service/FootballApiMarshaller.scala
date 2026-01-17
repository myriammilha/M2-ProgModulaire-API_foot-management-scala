package fr.football.service

import org.apache.pekko.http.scaladsl.marshalling.ToEntityMarshaller
import org.apache.pekko.http.scaladsl.unmarshalling.FromEntityUnmarshaller
import org.openapitools.server.api.DefaultApiMarshaller
import org.openapitools.server.model.{ModelMatch, Player, Team}
import spray.json._
import org.apache.pekko.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import DefaultJsonProtocol._

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val teamFormat: RootJsonFormat[Team] = jsonFormat3(Team)
  implicit val playerFormat: RootJsonFormat[Player] = jsonFormat4(Player)
  implicit val matchFormat: RootJsonFormat[ModelMatch] = jsonFormat4(ModelMatch) // à ajuster si besoin
}

class FootballApiMarshaller extends DefaultApiMarshaller with JsonSupport {
  override implicit def fromEntityUnmarshallerTeam: FromEntityUnmarshaller[Team] =
    sprayJsonUnmarshaller[Team]

  override implicit def toEntityMarshallerTeam: ToEntityMarshaller[Team] =
    sprayJsonMarshaller[Team]

  override implicit def fromEntityUnmarshallerPlayer: FromEntityUnmarshaller[Player] =
    sprayJsonUnmarshaller[Player]

  override implicit def toEntityMarshallerPlayer: ToEntityMarshaller[Player] =
    sprayJsonMarshaller[Player]

  override implicit def fromEntityUnmarshallerModelMatch: FromEntityUnmarshaller[ModelMatch] =
    sprayJsonUnmarshaller[ModelMatch]

  override implicit def toEntityMarshallerModelMatch: ToEntityMarshaller[ModelMatch] =
    sprayJsonMarshaller[ModelMatch]

  override implicit def toEntityMarshallerTeamarray: ToEntityMarshaller[Seq[Team]] =
    sprayJsonMarshaller[Seq[Team]]

  override implicit def toEntityMarshallerPlayerarray: ToEntityMarshaller[Seq[Player]] =
    sprayJsonMarshaller[Seq[Player]]

  override implicit def toEntityMarshallerModelMatcharray: ToEntityMarshaller[Seq[ModelMatch]] =
    sprayJsonMarshaller[Seq[ModelMatch]]
}
