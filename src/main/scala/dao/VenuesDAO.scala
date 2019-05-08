package dao

import entites.{PlayerEntity, VenueEntity}

import scala.collection.mutable

object VenuesDAO {

  private var venuesMap = new mutable.HashMap[String, VenueEntity]

  def getAllVenues: mutable.HashMap[String, VenueEntity] = {
    venuesMap
  }

  def buyVenue(playerEntity: PlayerEntity, venueId: String): Boolean = {
    val venue = getVenueById(venueId)
    if (venue.isDefined && (playerEntity.money > venue.get.price)) {
      addOrUpdateVenue(venue.get.copy(owner = Some(playerEntity.id)))
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
