package org.example.backend

import javax.enterprise.context.Dependent
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application

@Dependent
@ApplicationPath("/api")
class HelloApplication : Application() {

}