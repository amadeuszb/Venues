package services

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import messages.Manager.{AddVenue, BuyVenue, BuyVenueResponse, DeleteVenue, GetVenuesResponse}
import messages.VenueManager.GetVenues

import scala.concurrent.Future

trait VenuesApi {

  def createManager(): ActorRef

  implicit def requestTimeout: Timeout

  lazy val mainActor: ActorRef = createManager()

  def getVenues(): Future[GetVenuesResponse] = mainActor.ask(GetVenues).mapTo[GetVenuesResponse]

  def addVenues(venueId: String, json: String): Future[Any] = mainActor.ask(AddVenue(venueId, json))

  def deleteVenue(venueId: String) = mainActor.ask(DeleteVenue(venueId))

  def buyVenue(venueId: String, json: String): Future[BuyVenueResponse] = mainActor.ask(BuyVenue(venueId: String, json: String)).mapTo[BuyVenueResponse]

}