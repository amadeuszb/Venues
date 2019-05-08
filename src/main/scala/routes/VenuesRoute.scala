package routes

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives.{complete, get, pathPrefix, _}
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import messages.Manager
import messages.Manager.{BuyVenueResponse, GetVenuesResponse}
import services.VenuesApi

class VenuesRoute(system: ActorSystem, timeout: Timeout) extends VenuesApi {

  val venuesPath = "venues"
  val buyPath = "buy"

  val getVenueRoute: Route =
    pathEnd {
      get {
        pathEnd {
          onSuccess(getVenues()) {
            case GetVenuesResponse(value) => complete(value)
          }
        }
      }
    }

  val createAndDeleteVenueRoute: Route =
    path(Segment) {
      id => {
        pathEnd {
          delete {
            onSuccess(deleteVenue(id)) {
              case _ => complete(HttpResponse(StatusCodes.OK)) //TODO
            }
          } ~ put {
            entity(as[String]) { json => complete("")
              onSuccess(addVenues(id,json)) {
                case _ => complete(HttpResponse(StatusCodes.OK)) //TODO
              }
            }
          }
        }
      }
    }

  val buyVenueRoute: Route =
    path(Segment / buyPath) {
      id =>
        post {
          entity(as[String]) { json =>
            onSuccess(buyVenue(id,json)) {
              case BuyVenueResponse(value) => complete(value) //TODO
            }
          }
        }

    }

  val routes: Route =
    pathPrefix(venuesPath) {
      getVenueRoute ~
        createAndDeleteVenueRoute ~
        buyVenueRoute
    }

  override def createManager(): ActorRef = system.actorOf(Manager.props)

  override implicit def requestTimeout: Timeout = timeout
}

