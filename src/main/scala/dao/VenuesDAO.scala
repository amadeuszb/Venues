package dao

import entity.VenueEntity
import io.circe.Printer
import io.circe.syntax._

import scala.collection.mutable

object VenuesDAO {

  private val printer = Printer.noSpaces.copy(dropNullValues = true)
  private var venuesMap = new mutable.HashMap[String, VenueEntity]

  def getAllVenues: String = {
    venuesMap.values.asJson.pretty(printer)
  }

  def deleteVenueById(id: String): Unit = {
    venuesMap.remove(id)
  }

  def addOrUpdateVenue(venue: VenueEntity): Unit = {
    deleteVenueById(venue.id)
    venuesMap += ((venue.id, venue))
  }

  def buyVenue(playerId: String, venueId: String): Boolean = { //TODO move part of actions to PlayerDAO
    val playersMoney = PlayerDAO.getMoneyByPlayerId(playerId)
    val venue = venuesMap(venueId) //TODO Change to get and check if object exist
    if (playersMoney > venue.price) {
      addOrUpdateVenue(venue.copy(owner = Some(playerId)))
      true
    }
    else false
  }


}
