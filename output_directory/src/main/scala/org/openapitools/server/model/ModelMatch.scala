package org.openapitools.server.model


/**
 * @param id Identifiant unique du match for example: ''null''
 * @param team1Id Identifiant de l'équipe 1 for example: ''null''
 * @param team2Id Identifiant de l'équipe 2 for example: ''null''
 * @param score Score final du match (e.g., 2-1) for example: ''null''
*/
final case class ModelMatch (
  id: String,
  team1Id: String,
  team2Id: String,
  score: Option[String] = None
)

