package services

import akka.actor.ActorRef
import akka.util.Timeout
import akka.actor.{ActorRef, ActorSystem}
import akka.util.Timeout
import akka.pattern.ask
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future}
import StatusCodes._
import entites.VenueEntity
import messages.VenueSeller.GetVenues

trait VenuesApi {

  def createManager(): ActorRef

  implicit def executionContext: ExecutionContext
  implicit def requestTimeout: Timeout

  lazy val game: ActorRef = createManager()

/*
  def createVenue(id: String, json: String): Future[VerVenues] = {
    val actualVenue = Decoders.decodeVenue(json)
    game.ask(CreateEvent(event, numberOfTickets))
      .mapTo[EventResponse]
  }
*/

  def getVenues(): Future[Vector[VenueEntity]] = game.ask(GetVenues).mapTo[Vector[VenueEntity]]


}