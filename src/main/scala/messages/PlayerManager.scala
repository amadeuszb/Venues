package messages

import actors.{PlayerManager, VenueSeller}
import akka.actor.Props
import entites.VenueEntity

object PlayerManager {
  def props() = Props(new PlayerManager())

  case class GetMoney(playerId: String)
  case class Money(price: Long)
  case class Pay(playerId: String, price: Int)

}