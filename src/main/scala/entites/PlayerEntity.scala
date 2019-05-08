package entites

import io.circe.generic.JsonCodec

@JsonCodec
case class SimplePlayerEntity(playerId: String)

@JsonCodec
case class PlayerEntity(id: String,
                        money: Long
                       )

