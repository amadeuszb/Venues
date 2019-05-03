package dao

import scala.collection.mutable


object PlayerDAO {

  var playersMap = new mutable.HashMap[String, Long]()
  playersMap.put("player2", 2000)
  playersMap.put("player1", 500)

  def getMoneyByPlayerId(playerId: String): Long = { //TODO Exception
    playersMap.getOrElse(playerId, -1)
  }

  def payForVenue(playerId: String, price: Long): Unit = { //add exception
    val actualPlayer = playersMap.get(playerId)
    actualPlayer match {
      case Some(player) =>
        playersMap.remove(playerId)
        playersMap.put(playerId, actualPlayer.get - price)
    }


  }
}
