package org.example.backend.rest

import org.example.backend.util.JndiUtils
import org.example.ejb.data.ErrorResponse
import org.example.ejb.data.StarShipCreateRequest
import org.example.ejb.service.StarShipService
import java.time.LocalDateTime
import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/v1/star-ships")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
open class StarShipController {

    @POST
    open fun createStarShip(starShipCreateRequest: StarShipCreateRequest): Response {
        if (starShipCreateRequest.name.isNullOrBlank()) {
            return Response.status(400).entity(
                ErrorResponse(
                    LocalDateTime.now().toString(),
                    "Не удалось создать звездный корабль! Поле name не должно быть пустым!"
                )
            ).build()
        }

        if (starShipCreateRequest.maxSpeed <= 0) {
            return Response.status(400).entity(
                ErrorResponse(
                    LocalDateTime.now().toString(),
                    "Не удалось создать звездный корабль! Поле maxSpeed должно быть положительным!"
                )
            ).build()
        }

        val result = getService().createStarship(starShipCreateRequest)
        return if (result.code == 200) {
            Response.status(200).entity(result.id).build()
        } else {
            Response.status(result.code).entity(result.errorResponse).build()
        }
    }

    @POST
    @Path("/{starShipId}/load/{spaceMarineId}")
    open fun loadSpaceMarine(@PathParam("starShipId") starShipId: Long,
                             @PathParam("spaceMarineId") spaceMarineId: Long
    ): Response {
        val result = getService().addMarineToStarship(starShipId, spaceMarineId)
        return if (result.code == 200) {
            Response.status(200).entity("Успех!").build()
        } else {
            Response.status(result.code).entity(result.errorResponse).build()
        }
    }

    @POST
    @Path("/{starShipId}/unload-all")
    open fun unloadAllFromStarShip(@PathParam("starShipId") starShipId: Long): Response {
        val result = getService().unloadMarinesFromStarShip(starShipId)
        return if (result.code == 204) {
            Response.status(204).build()
        } else {
            Response.status(result.code).entity(result.errorResponse).build()
        }
    }

    @GET
    open fun getAllStarShips(): Response {
        val starShips = getService().getAllStarShips()
        return Response.status(200).entity(starShips).build()
    }

    private fun getService(): StarShipService {
        return JndiUtils.getFromContext("ejb:/EJB-1.0-SNAPSHOT/StarShipServiceImpl!org.example.ejb.service.StarShipService")
    }
}
