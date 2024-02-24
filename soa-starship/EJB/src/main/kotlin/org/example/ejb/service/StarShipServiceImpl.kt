package org.example.ejb.service

import org.example.ejb.data.AddMarineToStarshipResponse
import org.example.ejb.data.ChapterRequest
import org.example.ejb.data.CoordinatesRequest
import org.example.ejb.data.CreateStarShipResponse
import org.example.ejb.data.SpaceMarineResponse
import org.example.ejb.data.SpaceMarineUpdateRequest
import org.example.ejb.data.StarShipCreateRequest
import org.example.ejb.data.StarShipResponse
import org.example.ejb.data.UnloadMarinesFromStarshipResponse
import org.jboss.ejb3.annotation.Pool
import java.io.FileInputStream
import java.security.KeyStore
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet
import javax.ejb.Stateless
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.ws.rs.BadRequestException
import javax.ws.rs.client.Client
import javax.ws.rs.client.ClientBuilder
import javax.ws.rs.client.Entity
import javax.ws.rs.core.MediaType

@Stateless
@Pool("slsb-strict-max-pool")
open class StarShipServiceImpl : StarShipService {

    override fun createStarship(starShipCreateRequest: StarShipCreateRequest): CreateStarShipResponse {
        var connection: Connection? = null
        var preparedStatement: PreparedStatement? = null
        var resultSet: ResultSet? = null
        val query = "INSERT INTO starship (name, max_speed) VALUES (?, ?) RETURNING id"

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)
            preparedStatement = connection.prepareStatement(query)
            preparedStatement.setString(1, starShipCreateRequest.name)
            preparedStatement.setDouble(2, starShipCreateRequest.maxSpeed)

            resultSet = preparedStatement.executeQuery()
            if (resultSet.next()) {
                return CreateStarShipResponse(
                    200,
                    id = resultSet.getLong("id")
                )
            }
            return CreateStarShipResponse(
                400,
                errorResponse = "Не удалось создать звездный корабль!"
            )

        } finally {
            resultSet?.close()
            preparedStatement?.close()
            connection?.close()
        }

    }

    override fun addMarineToStarship(starShipId: Long, spaceMarineId: Long): UnloadMarinesFromStarshipResponse {
        getStarshipById(starShipId)
            ?: return UnloadMarinesFromStarshipResponse(
                code = 404,
                errorResponse = "Не удалось найти звездный корабль по такому id!"
            )

        return try {
            performUpdateMarine(spaceMarineId, starShipId)
            UnloadMarinesFromStarshipResponse(
                code = 200
            )
        } catch (ex: BadRequestException) {
            UnloadMarinesFromStarshipResponse(
                code = 400,
                errorResponse = "Воздушный корабль с id = $starShipId уже занят!"
            )
        }

    }

    override fun unloadMarinesFromStarShip(starShipId: Long): UnloadMarinesFromStarshipResponse {
        getStarshipById(starShipId)
            ?: return UnloadMarinesFromStarshipResponse(
                code = 404,
                errorResponse = "Не удалось найти звездный корабль по такому id!"

            )

        var connection: Connection? = null
        var preparedStatement: PreparedStatement? = null
        val query = "UPDATE starship SET spacemarine_id = NULL WHERE id = ?"

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)
            preparedStatement = connection.prepareStatement(query)
            preparedStatement.setLong(1, starShipId)

            preparedStatement.executeUpdate()
            return UnloadMarinesFromStarshipResponse(
                code = 204
            )

        } finally {
            preparedStatement?.close()
            connection?.close()
        }
    }

    override fun getAllStarShips(): List<StarShipResponse> {
        var connection: Connection? = null
        var preparedStatement: PreparedStatement? = null
        val query = "SELECT * FROM starship"
        var resultSet: ResultSet? = null
        val starShips = mutableListOf<StarShipResponse>()

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)
            preparedStatement = connection.prepareStatement(query)

            resultSet = preparedStatement.executeQuery()

            while (resultSet.next()) {
                val starship = StarShipResponse(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getDouble("max_speed"),
                    resultSet.getLong("spacemarine_id")
                )
                starShips.add(starship)
            }
            return starShips

        } finally {
            resultSet?.close()
            preparedStatement?.close()
            connection?.close()
        }
    }


    private fun getStarshipById(id: Long): StarShipResponse? {
        var connection: Connection? = null
        var preparedStatement: PreparedStatement? = null
        var resultSet: ResultSet? = null
        val query = "SELECT * FROM starship WHERE id = ?"

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)
            preparedStatement = connection.prepareStatement(query)
            preparedStatement.setLong(1, id)

            resultSet = preparedStatement.executeQuery()
            if (resultSet.next()) {
                val starship = StarShipResponse(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getDouble("max_speed"),
                    resultSet.getLong("spacemarine_id")
                )
                return starship
            }
            return null
        } finally {
            resultSet?.close()
            preparedStatement?.close()
            connection?.close()
        }
    }

    private fun createSSLContext(truststorePath: String, truststorePassword: String): SSLContext {
        val truststore = KeyStore.getInstance("PKCS12", "BC")

        FileInputStream(truststorePath).use { truststoreInput -> truststore.load(truststoreInput, truststorePassword.toCharArray()) }
        val tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        tmf.init(truststore)
        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, tmf.trustManagers, null)
        return sslContext
    }

    private fun createSSLClient(): Client {
        val sslContext = createSSLContext(
            "/keystore.jks",
            "password"
        )
        return ClientBuilder.newBuilder()
            .sslContext(sslContext)
            .build()
    }


    private fun performGetMarineById(id: Long): SpaceMarineResponse? {
        return createSSLClient()
            .target(MARINES_URL)
            .path("/{id}")
            .resolveTemplate("id", id)
            .request(MediaType.APPLICATION_JSON)
            .get(SpaceMarineResponse::class.java)
    }

    private fun performUpdateMarine(id: Long, starShipId: Long): Long {
        return createSSLClient()
            .target(MARINES_URL)
            .path("/{id}/starship-include/{starShipId}")
            .resolveTemplates(mapOf(
                "id" to id,
                "starShipId" to starShipId))
            .request(MediaType.APPLICATION_JSON)
            .put(Entity.json<Any>(Unit), Long::class.java)
    }

    private fun mapToSpaceMarineUpdateRequest(
        spaceMarine: SpaceMarineResponse,
        starShipId: Long
    ): SpaceMarineUpdateRequest {
        val coordinatesRequest = CoordinatesRequest(
            spaceMarine.coordinates.x,
            spaceMarine.coordinates.y
        )

        val chapterRequest = ChapterRequest(
            spaceMarine.chapter.name,
            spaceMarine.chapter.parentLegion,
            spaceMarine.chapter.marinesCount,
            spaceMarine.chapter.world
        )


        return SpaceMarineUpdateRequest(
            spaceMarine.name,
            coordinatesRequest,
            spaceMarine.health,
            spaceMarine.height,
            spaceMarine.category,
            spaceMarine.weaponType,
            chapterRequest,
            starShipId
        )
    }

    companion object {
        private val MARINES_URL = "http://haproxy:8200/api/v1/space-marines"
        private val DB_URL = "jdbc:postgresql://postgres:5432/soa"
        private val DB_USER = "postgres"
        private val DB_PASSWORD = "postgres"
    }
}