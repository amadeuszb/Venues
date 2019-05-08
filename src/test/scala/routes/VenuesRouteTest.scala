package routes

import akka.http.scaladsl.model.StatusCode
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import io.circe.syntax._
import org.scalatest.{Matchers, WordSpec}

class VenuesRouteTest extends WordSpec with Matchers with ScalatestRouteTest {

  private val smallRoute: Route = new VenuesRoute().route
  private val sampleId = "687e8292-1afd-4cf7-87db-ec49a3ed93b1"
  private val jsonVenue = "{\"name\": \"Rynek Główny\",\"price\": 1000}"
  private val jsonWrongVenue = "{\"id\":\"" + sampleId + "\",\"namesd\":\"Rynek Główny\",\"pr1ice\":100110}"

  "Venues Service" should {
    val venuesPath = "/venues"

    "return empty json list at begging" in {
      Get(venuesPath) ~> smallRoute ~> check {
        responseAs[String] shouldEqual "[]"
      }
    }
    "add correctly new venue" in {
      val jsonExpected = "{\"id\":\"" + sampleId + "\",\"name\":\"Rynek Główny\",\"price\":1000}"
      Put(s"$venuesPath/$sampleId").withEntity(jsonVenue) ~> smallRoute
      Get(venuesPath) ~> smallRoute ~> check {
        responseAs[String].asJson shouldEqual s"[$jsonExpected]".asJson
      }
    }

    "delete venue" in {
      Delete(s"$venuesPath/$sampleId") ~> smallRoute
      Get(venuesPath) ~> smallRoute ~> check {
        responseAs[String] shouldEqual "[]"
      }
    }

    "buy a venue when player can't afford it" in {
      Put(s"$venuesPath/$sampleId").withEntity(jsonVenue) ~> smallRoute
      val jsonPlayer: String = "{\n  \"playerId\": \"player1\"\n}"
      Post(s"$venuesPath/$sampleId/buy").withEntity(jsonPlayer) ~> smallRoute ~> check {
        responseAs[String] shouldEqual "player1 can't afford Rynek Główny"
      }
    }
    "buy a venue when player can afford it" in {
      val jsonPlayer: String = "{\n  \"playerId\": \"player2\"\n}"
      Post(s"$venuesPath/$sampleId/buy").withEntity(jsonPlayer) ~> smallRoute ~> check {
        responseAs[String] shouldEqual "Rynek Główny was bought by player2 for 1000"
      }
      Get(venuesPath) ~> smallRoute ~> check {
        responseAs[String] shouldEqual "[{\"id\":\"687e8292-1afd-4cf7-87db-ec49a3ed93b1\",\"name\":\"Rynek Główny\",\"price\":1000,\"owner\":\"player2\"}]"
      }
    }
    "return status 400 when json is incorrect" in {
      Put(s"$venuesPath/$sampleId").withEntity(jsonWrongVenue) ~> smallRoute ~> check {
        response.status shouldEqual StatusCode.int2StatusCode(400)
      }
    }
    "return status 200 when json is correct" in {
      Put(s"$venuesPath/$sampleId").withEntity(jsonVenue) ~> smallRoute ~> check {
        response.status shouldEqual StatusCode.int2StatusCode(200)
      }
    }

  }

}

