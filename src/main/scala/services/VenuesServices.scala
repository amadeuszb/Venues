package services

import dao.VenuesDAO
import entity.{SimplePlayerEntity, SimpleVenueEntity, VenueEntity}
import io.circe.Printer
import io.circe.generic.auto._
import io.circe.parser.{decode, parse}
import io.circe.syntax._

object VenuesServices {

  private val printer = Printer.noSpaces.copy(dropNullValues = true)

  def getVenues: String = {
    VenuesDAO.getAllVenues.values.asJson.pretty(printer)
  }

  def decodeVenue(entity: String): Option[SimpleVenueEntity] = { //TODO improve Decoding and move to services
    val simpleUserProduct = parse(entity) match {
      case Right(json) => decode[Option[SimpleVenueEntity]](json.toString())
      case Left(_) => None
    }
    simpleUserProduct match {
      case Right(user) => user.asInstanceOf[Some[SimpleVenueEntity]]
      case Left(_) => None
    }
  }

  def decodePlayer(entity: String): Option[SimplePlayerEntity] = { //TODO improve Decoding and move to services
    val simpleUserProduct = parse(entity) match {
      case Right(json) => decode[Option[SimplePlayerEntity]](json.toString())
      case Left(_) => None
    }
    simpleUserProduct match {
      case Right(user) => user.asInstanceOf[Some[SimplePlayerEntity]]
      case Left(_) => None
    }
  }

  def addVenue(id: String, json: String): Unit = {
    val actualVenue = decodeVenue(json)
    actualVenue.foreach(venue => VenuesDAO.addOrUpdateVenue(VenueEntity(id, venue.name, venue.price, None)))
  }

  def deleteVenue(venueId: String): Unit = {
    VenuesDAO.deleteVenueById(venueId)
  }

  def buyVenue(propertyId: String, json: String): String = {
    val simplePlayerEntity = decodePlayer(json)
    if (simplePlayerEntity.isDefined) {
      val simplePlayer = simplePlayerEntity.get
      val returnedInformation = VenuesDAO.getVenueById(propertyId) match {
        case Some(venue) => {
          if (VenuesDAO.buyVenue(simplePlayer.playerId, propertyId))
            s"${venue.name} was bought by ${simplePlayer.playerId} for ${venue.price}"
          else
            s"${simplePlayer.playerId} can't afford ${venue.name}"
        }
        case None => "Error property doesn't exist"
      }
      returnedInformation
    }
    else
      "Error json cannot be correctly decoded"
  }

}
