package org.example.ejb.data

import lombok.NoArgsConstructor
import java.io.Serializable

@NoArgsConstructor
data class CreateStarShipResponse(
    val code: Int,
    val id: Long? = null,
    val errorResponse: String? = null,
    private val serialVersionUID: Long = -981578L
) : Serializable