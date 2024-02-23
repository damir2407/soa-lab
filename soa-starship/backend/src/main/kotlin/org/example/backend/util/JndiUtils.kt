package org.example.backend.util

import javax.naming.InitialContext

@Suppress("UNCHECKED_CAST")
class JndiUtils {

    companion object {
        fun <T> getFromContext(path: String): T {
            try {
                return InitialContext().lookup(path) as T
            } catch (ex: Exception) {
                throw RuntimeException("Failed to retrieve item from context. Path = $path")
            }
        }
    }
}