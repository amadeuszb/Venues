package dao

import scala.collection.mutable


object PlayerDAO {

  private val playersMap = new mutable.HashMap[String, Long]()
  playersMap.put("player2", 2000)
  playersMap.put("player1", 500)

  def getMoneyByPlayerId(playerId: String): Long = {
    playersMap.getOrElse(playerId, 0)
  }

  def payForVenue(playerId: String, price: Long): Unit = {
    val actualPlayer = playersMap.get(playerId)
    actualPlayer match {
      case Some(_) =>
        playersMap.remove(playerId)
        playersMap.put(playerId, actualPlayer.get - price)
    }
  }

}
