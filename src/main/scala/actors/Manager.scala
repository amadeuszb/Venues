package actors

import akka.actor._
import akka.util.Timeout
import entites.{PlayerEntity, SimplePlayerEntity, VenueEntity}
import messages.Manager.{AddVenue, BuyVenue}
import messages.PlayerManager.{GetMoney, Money}
import messages.VenueSeller
import messages.VenueSeller._

class Manager(implicit timeout: Timeout) extends Actor {

  import messages.PlayerManager


  def createVenuesSeller(name: String): ActorRef = {
    context.actorOf(VenueSeller.props(), name)
  }

  def createPlayersManager(name: String): ActorRef = {
    context.actorOf(PlayerManager.props(), name)
  }

  val venueSeller = createVenuesSeller("name")
  val playerManager = createVenuesSeller("nameManager")
  def receive = normalBehaviour
  def normalBehaviour:Receive = {
    case AddVenue(id, json) => {
      val simpleVenue = Decoders.decodeVenue(json).get //TODO remove get
      val fullVenue = VenueEntity(id, simpleVenue.name, simpleVenue.price, None)
      venueSeller ! AddOrUpdate(fullVenue)
    }
    case BuyVenue(venueId: String, json: String) => {
      val simplePlayer = Decoders.decodePlayer(json).get//TODO remove get
      playerManager ! GetMoney(simplePlayer.playerId)
      context.become(waitingForMoney(venueId, simplePlayer))

    }
    case GetVenues => {
      venueSeller ! GetVenues
      //context.child("nameManager"
      context.become(waitingForVenues(sender))
    }
    /*
    case CreatePlayer(name, tickets) ⇒
      def create(): Unit = {
        //        creates the ticket seller
        val eventTickets = createVenuesSeller(name)
        //        builds a list of numbered tickets
        val newTickets = (1 to tickets).map { ticketId ⇒
          PlayerManager.Ticket(ticketId)
        }.toVector
        //        sends the tickets to the TicketSeller
        eventTickets ! PlayerManager.Add(newTickets)
        //        creates an event and responds with EventCreated
        sender() ! EventCreated(Event(name, tickets))
      }
      //      If event exists it responds with EventExists
      context.child(name).fold(create())(_ ⇒ sender() ! EventExists)


    case GetTickets(event, tickets) ⇒
      //      sends an empty Tickets message if the ticket seller couldn't be found
      def notFound(): Unit = sender() ! PlayerManager.Tickets(event)

      //      buys from the found TicketSeller
      def buy(child: ActorRef): Unit = {
        child.forward(PlayerManager.Buy(tickets))
      }
      //      executes notFound or buys with the found TicketSeller
      context.child(event).fold(notFound())(buy)


    case GetEvent(event) =>
      def notFound() = sender() ! None

      def getEvent(child: ActorRef) = child forward PlayerManager.GetEvent

      context.child(event).fold(notFound())(getEvent)


    case GetEvents ⇒
      def getEvents = {
        context.children.map { child ⇒
          //          asks all TicketSellers about the events they are selling for
          self.ask(GetEvent(child.path.name)).mapTo[Option[Event]]
        }
      }

      def convertToEvents(f: Future[Iterable[Option[Event]]]): Future[Events] = {
        f.map(_.flatten).map(l ⇒ Events(l.toVector))
      }

      pipe(convertToEvents(Future.sequence(getEvents))) to sender()


    case CancelEvent(event) ⇒
      def notFound(): Unit = sender() ! None

      //      ActorRef carries the message that should be sent to an Actor
      //      Here we'll forward the message to the TicketSeller actor that an event was canceled
      def cancelEvent(child: ActorRef): Unit = child forward PlayerManager.Cancel

      context.child(event).fold(notFound())(cancelEvent)
  }*/
  }
  def waitingForMoney(venueId: String, simplePlayer: SimplePlayerEntity): Receive = {
    case Money(price)=>
      val fullPlayer= PlayerEntity(simplePlayer.playerId, price)
      venueSeller ! Buy(venueId,fullPlayer)
      context.become(waitingForPurchase(venueId, simplePlayer))
  }

  def waitingForPurchase(venueId: String , simplePlayerEntity: SimplePlayerEntity): Receive = {
    case PurchaseResponse(response) =>
  }
  def waitingForVenues(originalSender: ActorRef): Receive ={
    case VenuesResponse(venues) => originalSender ! venues
  }
}