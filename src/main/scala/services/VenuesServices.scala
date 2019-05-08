package services


import akka.actor.{ActorRef, ActorSystem}
import akka.util.Timeout
import akka.pattern.ask
import akka.http.scaladsl.model.StatusCodes


import scala.concurrent.ExecutionContext

object VenuesServices extends VenuesApi {

  override def createManager(): ActorRef = system.actorOf(Coachella.props)

  override implicit def executionContext: ExecutionContext = ???

  override implicit def requestTimeout: Timeout = ???
}
