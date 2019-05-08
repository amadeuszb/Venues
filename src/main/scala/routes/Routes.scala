package routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route


object Routes {

  val routes: Route = {
    concat(
      VenuesRoute.routes
    )
  }

}

