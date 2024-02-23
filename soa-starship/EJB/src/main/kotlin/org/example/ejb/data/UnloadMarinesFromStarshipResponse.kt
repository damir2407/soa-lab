package org.example.ejb.data

import lombok.NoArgsConstructor
import java.io.Serializable

@NoArgsConstructor
data class UnloadMarinesFromStarshipResponse(
    val code: Int,
    val errorResponse: String? = null,
    private val serialVersionUID: Long = -981548L
) : Serializable