package dao

import entity.VenueEntity

import scala.collection.mutable

object VenuesDAO {

  private var venuesMap = new mutable.HashMap[String, VenueEntity]

  def getAllVenues: mutable.HashMap[String, VenueEntity] = {
    venuesMap
  }

  def buyVenue(playerId: String, venueId: String): Boolean = { //TODO move part of actions to PlayerDAO
    val playersMoney = PlayerDAO.getMoneyByPlayerId(playerId)
    val venue = getVenueById(venueId)
    if (venue.isDefined && (playersMoney > venue.get.price)) {
      addOrUpdateVenue(venue.get.copy(owner = Some(playerId)))
      true
    }
    else false
  }

  def addOrUpdateVenue(venue: VenueEntity): Unit = {
    deleteVenueById(venue.id)
    venuesMap += ((venue.id, venue))
  }

  def deleteVenueById(id: String): Unit = {
    venuesMap.remove(id)
  }

  def getVenueById(id: String): Option[VenueEntity] = {
    venuesMap.get(id)
  }


}
