package actors

import akka.actor.Actor
import dao.PlayerDAO
import entites.VenueEntity
import messages.VenueSeller._

class VenueSeller extends Actor {

  def behaviour(venues: Vector[VenueEntity]): Receive = {
    case AddOrUpdate(newVenue) ⇒ {
      val newVenues = venues
      if(venues.exists(v => v.id == newVenue.id))
        newVenues.filter(v => v.id == newVenue.id)
      context.become(behaviour(newVenues :+ newVenue))
    }
    case Buy(venueId, player) ⇒ {
      val venue = venues.find(v => v.id == venueId) //TODO zabezpieczyc geta
      if (venue.isDefined && (player.money > venue.get.price)) {
        context.become(behaviour(venues :+ venue.get.copy(owner = Some(player.id))))
        sender() ! PurchaseResponse(s"${venue.get.name} was bought by ${player.id} for ${venue.get.price}")
      }
      else sender() ! PurchaseResponse(s"${player.id} can't afford ${venue.get.name}")
    }
    case GetVenues ⇒ sender() ! venues
    case Delete(venueId) ⇒
      context.become(behaviour(venues.filter(v => v.id == venueId)))
  }

  def receive = behaviour(Vector.empty[VenueEntity])
}