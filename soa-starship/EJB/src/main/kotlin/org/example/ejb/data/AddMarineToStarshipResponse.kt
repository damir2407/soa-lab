package org.example.ejb.data

import lombok.NoArgsConstructor
import java.io.Serializable

@NoArgsConstructor
data class AddMarineToStarshipResponse(
    val code: Int,
    val spaceMarineResponse: Long? = null,
    val errorResponse: String? = null,
    private val serialVersionUID: Long = -981571L
) : Serializable