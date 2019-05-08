package actors

import akka.actor._
import akka.util.Timeout
import entites.{PlayerEntity, SimplePlayerEntity, VenueEntity}
import messages.Manager.{AddVenue, BuyVenue, BuyVenueResponse, GetVenuesResponse}
import messages.PlayerManager.Money
import messages.VenueManager
import messages.VenueManager._
import akka.http.scaladsl.model.{StatusCode, StatusCodes}
import entites.{SimplePlayerEntity, SimpleVenueEntity, VenueEntity}
import io.circe.generic.auto._
import io.circe.parser.parse
import io.circe.syntax._
import io.circe.{Json, Printer}

import scala.concurrent.ExecutionContext

class Manager(implicit timeout: Timeout) extends Actor {

  import messages.PlayerManager

  val venueName = "venue"
  val playerName = "player"
  private val printer = Printer.noSpaces.copy(dropNullValues = true)

  def createVenuesSeller(name: String): ActorRef = {
    context.actorOf(VenueManager.props(), name)
  }

  def createPlayersManager(name: String): ActorRef = {
    context.actorOf(PlayerManager.props(), name)
  }

  implicit val ex: ExecutionContext = context.dispatcher

  val venueSeller: ActorRef = createVenuesSeller(venueName)
  val playerManager: ActorRef = createPlayersManager(playerName)

  def receive = normalBehaviour

  def normalBehaviour: Receive = {
    case AddVenue(id, json) =>
      val simpleVenue = Decoders.decodeVenue(json).get //TODO remove get
      val fullVenue = VenueEntity(id, simpleVenue.name, simpleVenue.price, None)
      sender() ! (venueSeller ! AddOrUpdate(fullVenue))
    case BuyVenue(venueId: String, json: String) => {
      val simplePlayer = Decoders.decodePlayer(json).get //TODO remove get
      context.become(waitingForMoney(sender, venueId, simplePlayer))
    }
    case GetVenues => {
      venueSeller ! GetVenues
      context.become(waitingForVenues(sender))
    }
  }

  def waitingForMoney(originalSender: ActorRef, venueId: String, simplePlayer: SimplePlayerEntity): Receive = {
    case Money(price) =>
      val fullPlayer = PlayerEntity(simplePlayer.playerId, price)
      venueSeller ! Buy(venueId, fullPlayer)
      context.become(waitingForPurchase(originalSender, venueId, simplePlayer))
  }

  def waitingForPurchase(originalSender: ActorRef, venueId: String, simplePlayerEntity: SimplePlayerEntity): Receive = {
    case PurchaseResponse(response) => originalSender ! BuyVenueResponse(response)
  }

  def waitingForVenues(originalSender: ActorRef): Receive = {
    case VenuesResponse(venues) => originalSender ! GetVenuesResponse(venues.asJson.pretty(printer))
  }
}