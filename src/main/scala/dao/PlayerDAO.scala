package dao

import entity.PlayerEntity

import scala.collection.mutable.ListBuffer

object PlayerDAO {
  var players: ListBuffer[PlayerEntity] = ListBuffer(PlayerEntity("player1", 500), PlayerEntity("player2", 2000))

  def getById(playerId: String): Option[PlayerEntity] ={
    players.find(p => p.id == playerId)
  }

  def payForVenue(playerId: String, price:Long): Unit ={ //add exception
    val actualPlayer = getById(playerId)
    actualPlayer match{
      case Some(player) => {
        val newPlayers = players.filter(player => player.id != playerId)+=player.copy(money = player.money-price)
        players = newPlayers
      }
    }


  }
}
