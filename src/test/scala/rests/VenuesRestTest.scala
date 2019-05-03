package rests

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import io.circe.syntax._
import org.scalatest.{Matchers, WordSpec}

class VenuesRestTest extends WordSpec with Matchers with ScalatestRouteTest { //TODO

  val smallRoute: Route = VenuesRest.routes
  val sampleId = "687e8292-1afd-4cf7-87db-ec49a3ed93b1"


  "Venues Service" should {
    "return empty json list at begging" in {
      Get("/venues") ~> smallRoute ~> check {
        responseAs[String] shouldEqual "[]"
      }
    }
    "add correctly new venue" in {
      val jsonExpected = "{\"id\":\"" + sampleId + "\",\"name\":\"Rynek Główny\",\"price\":1000}"
      val json = "{\n  \"name\": \"Rynek Główny\",\n  \"price\": 1000\n}"
      Put(s"/venues/$sampleId").withEntity(json) ~> smallRoute
      Get("/venues") ~> smallRoute ~> check {
        responseAs[String].asJson shouldEqual s"[$jsonExpected]".asJson
      }
    }
    "delete venue" in {
      Delete(s"/venues/$sampleId") ~> smallRoute
      Get("/venues") ~> smallRoute ~> check {
        responseAs[String] shouldEqual "[]"
      }
    }

    "buy a venue when player can't afford it" in {
      val jsonVenue = "{\n  \"name\": \"Rynek Główny\",\n  \"price\": 1000\n}"
      Put(s"/venues/$sampleId").withEntity(jsonVenue) ~> smallRoute
      val jsonPlayer: String = "{\n  \"playerId\": \"player1\"\n}"
      Post(s"/venues/$sampleId/buy").withEntity(jsonPlayer) ~> smallRoute ~> check {
        responseAs[String] shouldEqual "player1 can't afford Rynek Główny"
      }

    }
    "buy a venue when player can afford it" in {
      val jsonPlayer: String = "{\n  \"playerId\": \"player2\"\n}"
      Post(s"/venues/$sampleId/buy").withEntity(jsonPlayer) ~> smallRoute ~> check {
        responseAs[String] shouldEqual "Rynek Główny was bought by player2 for 1000"
      }
    }
  }

}

