package services

import akka.http.scaladsl.model.{StatusCode, StatusCodes}
import dao.VenuesDAO
import entites.{SimplePlayerEntity, SimpleVenueEntity, VenueEntity}
import io.circe.generic.auto._
import io.circe.parser.parse
import io.circe.syntax._
import io.circe.{Json, Printer}

object VenuesServices {

  private val printer = Printer.noSpaces.copy(dropNullValues = true)

  def addVenue(id: String, json: String): StatusCode = {
    val actualVenue = decodeVenue(json)
    if (actualVenue.isEmpty) {
      StatusCodes.BadRequest
    }
    else {
      actualVenue.foreach(venue => VenuesDAO.addOrUpdateVenue(VenueEntity(id, venue.name, venue.price, None)))
      StatusCodes.OK
    }

  }

  private def decodeVenue(entity: String): Option[SimpleVenueEntity] = {
    val res: Json = parse(entity).getOrElse(Json.Null)
    res.as[SimpleVenueEntity].toOption
  }

  def getVenues: String = {
    VenuesDAO.getAllVenues.values.asJson.pretty(printer)
  }

  def buyVenue(propertyId: String, json: String): (StatusCode, String) = {
    val simplePlayerEntity = decodePlayer(json)
    simplePlayerEntity match {
      case Some(simplePlayer) =>
        VenuesDAO.getVenueById(propertyId) match {
          case Some(venue) =>
            if (VenuesDAO.buyVenue(simplePlayer.playerId, propertyId))
              StatusCodes.OK -> s"${venue.name} was bought by ${simplePlayer.playerId} for ${venue.price}"
            else
              StatusCodes.OK -> s"${simplePlayer.playerId} can't afford ${venue.name}"
          case None => StatusCodes.BadRequest -> "Error venue doesn't exist"
        }
      case None =>
        StatusCodes.BadRequest -> "Error json cannot be correctly decoded"
    }
  }

  private def decodePlayer(entity: String): Option[SimplePlayerEntity] = {
    val res: Json = parse(entity).getOrElse(Json.Null)
    res.as[SimplePlayerEntity].toOption
  }

  def deleteVenue(venueId: String): Unit = {
    VenuesDAO.deleteVenueById(venueId)
  }


}
