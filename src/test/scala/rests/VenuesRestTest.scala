package rests

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import io.circe.syntax._
import org.scalatest.{Matchers, WordSpec}

class VenuesRestTest extends WordSpec with Matchers with ScalatestRouteTest { //TODO

  val smallRoute: Route = VenuesRest.routes
  val sampleId = "687e8292-1afd-4cf7-87db-ec49a3ed93b1"
  "Venues Service" should {
    "return correct json with venues" in {
      Get("/venues") ~> smallRoute ~> check {
        responseAs[String] shouldEqual "[]"
      }
    }
    "add correctly new venue" in {
      val json = "{\n  \"name\": \"Rynek Główny\",\n  \"price\": 1000\n}"
      val jsonExpected = "{\"id\":\"" + sampleId + "\",\"name\":\"Rynek Główny\",\"price\":1000}"
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


  }

}

