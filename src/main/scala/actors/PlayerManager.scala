package actors

import akka.actor.Actor
import entites.PlayerEntity
import messages.PlayerManager._

class PlayerManager extends Actor {

  def behaviour(players: Vector[PlayerEntity]): Receive = {
    case Pay(playerId, price) ⇒ {
      val player = players.find(p => p.id == playerId)
      val playersList = players.filter(p => p.id == playerId)
      if (player.isDefined) //todo isDefined
        context.become(behaviour(playersList :+ PlayerEntity(playerId, player.get.money - price)))
    }
    case GetMoney(playerId) ⇒ {
      val player = players.find(p => p.id == playerId) //TODO improve this code look
      if (player.isDefined) sender() ! Money(player.get.money)
      else sender() ! Money(0)
    }
  }

  def receive = behaviour(Vector[PlayerEntity](PlayerEntity("player2", 2000), PlayerEntity("player1", 500)))
}