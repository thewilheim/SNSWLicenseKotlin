import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*

val RoleBasedAuthorization = createRouteScopedPlugin(name = "RoleBasedAuthorization", createConfiguration = ::PluginConfiguration) {
    pluginConfig.apply {
        on(AuthenticationChecked) { call ->
            val principal = call.principal<JWTPrincipal>()
            val userClaimRoles = principal?.payload?.claims?.get("roles")
            var authorized = false

            if(userClaimRoles != null){
                val userRoles =  userClaimRoles.toString()
                    .replace("[","").replace("]","").replace("\"","")
                    .split(",")

                for(r in pluginConfig.roles){
                    val inRole = userRoles.contains(r)
                    if(inRole){
                        authorized = true
                    }
                }
            }
            if(!authorized){
                call.respond( HttpStatusCode.Forbidden)
            }
        }
    }
}

class PluginConfiguration {
    var roles: List<String> = listOf()
}