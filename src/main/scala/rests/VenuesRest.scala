package rests

import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives.{complete, get, pathPrefix, _}
import akka.http.scaladsl.server.Route
import services.VenuesServices

object VenuesRest {
  val routes: Route =
    pathPrefix("venues") {
      pathEnd {
        get {
          pathEnd {
            complete(HttpEntity(ContentTypes.`application/json`, VenuesServices.getVenues))
          }
        }
      } ~ path(Segment) {
        id => {
          pathEnd {
            delete {
              VenuesServices.deleteVenue(id)
              complete("") //todo
            } ~ put {
              entity(as[String]) { json =>
                VenuesServices.addVenue(id, json)
                complete("") //todo
              }
            }
          }
        }
      } ~ path(Segment / "buy") {
        id =>
        post {
          entity(as[String]) { json =>
            complete(VenuesServices.buyVenue(id, json))
          }
        }

      }
    }

}

