package actors

import entites.{SimplePlayerEntity, SimpleVenueEntity}
import io.circe.Json
import io.circe.parser.parse

object Decoders {
  def decodeVenue(entity: String): Option[SimpleVenueEntity] = {
    val res: Json = parse(entity).getOrElse(Json.Null)
    res.as[SimpleVenueEntity].toOption
  }
  def decodePlayer(entity: String): Option[SimplePlayerEntity] = {
    val res: Json = parse(entity).getOrElse(Json.Null)
    res.as[SimplePlayerEntity].toOption
  }
}
