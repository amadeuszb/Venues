package messages

import java.time.Duration

import actors.Manager
import akka.actor.Props
import akka.util.Timeout


object Manager  {
  implicit val timeout = Timeout.create(Duration.ofDays(1));
  def props() = Props(new Manager())
  case class CreatePlayer(playerId: String, money: Int)
  case class AddVenue(id: String, json: String)
  case class BuyVenue(propertyId: String, json: String)
  case class DeleteVenue(venueId: String)
  case object DeleteVenueResponse //tODO
  case class BuyVenueResponse(response: String)
  case class GetVenuesResponse(response: String)

}

