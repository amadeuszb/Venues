package messages

import actors.VenueManager
import akka.actor.Props
import entites.{PlayerEntity, VenueEntity}



object VenueManager {
  def props() = Props(new VenueManager())

  case class AddOrUpdate(venue: VenueEntity)
  case class Buy(venueId: String, player: PlayerEntity)
  case class PurchaseResponse(response: String)
  case class VenuesResponse(venues: Vector[VenueEntity])
  case class Delete(venueId: String)
  case object GetVenues

}