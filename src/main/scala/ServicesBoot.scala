
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import scalikejdbc._

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn

object ServicesBoot {

  implicit lazy val s = AutoSession

  def main(args: Array[String]) {

    implicit val system: ActorSystem = ActorSystem("my-system")
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    implicit val executionContext: ExecutionContextExecutor = system.dispatcher

    val route = Routes.routes

    val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)

    StdIn.readLine()
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())
  }

}