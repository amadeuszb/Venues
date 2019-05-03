package services

import dao.VenuesDAO
import entity.{SimplePlayerEntity, SimpleVenueEntity, VenueEntity}
import io.circe.generic.auto._
import io.circe.parser.{decode, parse}

object VenuesServices {

  def getVenues: String = {
    VenuesDAO.getAllVenues
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
    val simplePlayer = decodePlayer(json).get //TODO tutej try cathe
    if (VenuesDAO.buyVenue(simplePlayer.playerId, propertyId))
      s"${simplePlayer.playerId} can buy $propertyId"
    else
      s"${simplePlayer.playerId} cannot buy $propertyId"
  }

}
