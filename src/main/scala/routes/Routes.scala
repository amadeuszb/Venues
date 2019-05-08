package routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

import scala.concurrent.ExecutionContext


class Routes()(implicit system: ExecutionContext) extends VenuesRoute{

  val routes: Route = {
    concat(
      route
    )
  }

}

