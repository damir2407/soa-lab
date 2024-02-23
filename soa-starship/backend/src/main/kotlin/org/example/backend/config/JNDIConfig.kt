package org.example.backend.config

import jakarta.ws.rs.NotFoundException
import org.example.ejb.service.StarShipService
import java.util.Properties
import javax.naming.Context
import javax.naming.InitialContext
import javax.naming.NamingException

object JNDIConfig {

    fun starShipService(): StarShipService {
        val jndiProps = Properties()
        jndiProps[Context.INITIAL_CONTEXT_FACTORY] = "org.wildfly.naming.client.WildFlyInitialContextFactory"
        jndiProps[Context.PROVIDER_URL] = "remote+http://localhost:8080"
        try {
            val context: Context = InitialContext(jndiProps)
            return (context.lookup("ejb:/EJB-1.0-SNAPSHOT/StarShipServiceImpl!org.example.ejb.service.StarShipService") as StarShipService).also {
                println("I found my ejb!!")
            }
        } catch (e: NamingException) {
            println("Unable to find bean")
            e.printStackTrace()
            throw NotFoundException()
        }
    }
}