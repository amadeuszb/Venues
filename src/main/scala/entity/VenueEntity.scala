package entity

import io.circe.generic.JsonCodec

@JsonCodec
case class SimpleVenueEntity(name: String, price: Long)

@JsonCodec
case class VenueEntity(id: String, name: String, price: Long, owner: Option[String])

