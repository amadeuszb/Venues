package messages



object Manager  {
  case class CreatePlayer(playerId: String, money: Int)
  case class AddVenue(id: String, json: String)
  case class BuyVenue(propertyId: String, json: String)
}

