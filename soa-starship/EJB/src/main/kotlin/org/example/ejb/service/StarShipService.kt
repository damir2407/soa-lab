package org.example.ejb.service

import org.example.ejb.data.CreateStarShipResponse
import org.example.ejb.data.StarShipCreateRequest
import org.example.ejb.data.StarShipResponse
import org.example.ejb.data.UnloadMarinesFromStarshipResponse
import javax.ejb.Remote


@Remote
interface StarShipService {

    fun createStarship(starShipCreateRequest: StarShipCreateRequest): CreateStarShipResponse

    fun addMarineToStarship(starShipId: Long, spaceMarineId: Long): UnloadMarinesFromStarshipResponse

    fun unloadMarinesFromStarShip(starShipId: Long): UnloadMarinesFromStarshipResponse

    fun getAllStarShips(): List<StarShipResponse>

}