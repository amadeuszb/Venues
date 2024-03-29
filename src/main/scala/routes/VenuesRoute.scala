package routes

import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives.{complete, get, pathPrefix, _}
import akka.http.scaladsl.server.Route
import services.VenuesServices

object VenuesRoute {

  val venuesPath = "venues"
  val buyPath = "buy"

  val getVenueRoute: Route =
    pathEnd {
      get {
        pathEnd {
          complete(HttpEntity(ContentTypes.`application/json`, VenuesServices.getVenues))
        }
      }
    }

  val createAndDeleteVenueRoute: Route =
    path(Segment) {
      id => {
        pathEnd {
          delete {
            VenuesServices.deleteVenue(id)
            complete(HttpResponse(StatusCodes.OK))
          } ~ put {
            entity(as[String]) { json =>
              complete(VenuesServices.addVenue(id, json))
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
            complete(VenuesServices.buyVenue(id, json))
          }
        }

    }

  val routes: Route =
    pathPrefix(venuesPath) {
      getVenueRoute ~
        createAndDeleteVenueRoute ~
        buyVenueRoute
    }

}

