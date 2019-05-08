
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import routes.Routes
import scalikejdbc._

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn

object ServicesBoot {

  implicit lazy val session: AutoSession.type = AutoSession

  def main(args: Array[String]) {

    implicit val system: ActorSystem = ActorSystem("my-system")
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    implicit val executionContext: ExecutionContextExecutor = system.dispatcher

    val route = Routes.routes
    val port = 8080
    val interface = "localhost"
    val bindingFuture = Http().bindAndHandle(route, interface, port)

    StdIn.readLine()
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())
  }

}