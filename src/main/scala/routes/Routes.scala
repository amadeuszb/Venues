package routes

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout


class Routes(system: ActorSystem, timeout: Timeout) {

  val venuesRoute = new VenuesRoute(system,timeout)
  val routes: Route = {
    concat(
      venuesRoute.routes
    )
  }

}

