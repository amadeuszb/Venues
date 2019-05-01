package dao

import entity.VenueEntity
import io.circe.Printer
import io.circe.generic.auto._
import io.circe.syntax._

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

object VenuesDAO { //TODO VAR to VAL

  private var venues: ListBuffer[VenueEntity] = ListBuffer.empty
  private val printer = Printer.noSpaces.copy(dropNullValues = true)
  private var venuesMap = mutable.Map[String, String]

  def getAllVenues: String = {
    venues.asJson.pretty(printer)
  }

  def deleteVenueById(id: String): Unit = { //TODO better deleting
    venues = venues.filter(element => element.id != id)
  }

  def addOrUpdateVenue(venue: VenueEntity): Unit = {
    deleteVenueById(venue.id)
    venues += venue
  }

  def buyVenue(playerId: String, venueId: String): Boolean = { //TODO move part of actions to PlayerDAO
    val player = PlayerDAO.getById(playerId).get
    val venue = venues.find(v => v.id == venueId).get //TODO
    if (player.money > venue.price) {
      addOrUpdateVenue(venue.copy(owner = Some(player.id)))
      true
    }
    else false
  }


}
