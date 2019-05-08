
import java.time.Duration

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.util.Timeout
import routes.Routes
import scalikejdbc._

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn

object ServicesBoot extends App {

  implicit lazy val session: AutoSession.type = AutoSession

  override def main(args: Array[String]) {

    implicit val system: ActorSystem = ActorSystem("my-system")
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    implicit val executionContext: ExecutionContextExecutor = system.dispatcher

    implicit val timeOut = Timeout.create(Duration.ofSeconds(10))

    val mainRoutes = new Routes(system, timeOut)
    val route = mainRoutes.routes
    val port = 8080
    val interface = "localhost"
    val bindingFuture = Http().bindAndHandle(route, interface, port)

    StdIn.readLine()
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())
  }

}