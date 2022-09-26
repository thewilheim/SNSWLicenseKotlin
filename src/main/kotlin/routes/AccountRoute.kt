package routes

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.mongodb.client.MongoDatabase
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import models.User
import org.litote.kmongo.deleteOne
import org.litote.kmongo.find
import org.litote.kmongo.findOne
import org.litote.kmongo.getCollection
import org.mindrot.jbcrypt.BCrypt
import java.util.*

fun Route.initAccountRoute(db: MongoDatabase) {

    val accountCollection = db.getCollection<User>("accounts")


    route("/login"){
        post {
            val data = call.receive<User>()


            val filter = "{email:/^${data.email}$/i}"
            val user = accountCollection.findOne(filter)

            if(user == null){
                return@post call.respond(HttpStatusCode.BadRequest)
            }
            val valid = BCrypt.checkpw(data.password,user.password)
            if(!valid){
                return@post call.respond(HttpStatusCode.BadRequest)
            }
            val expiry = Date(System.currentTimeMillis() + 86400000)
            val token = JWT.create()
                .withAudience("http://localhost:8080")
                .withIssuer("http://localhost:8080")
                .withClaim("email",user?.email)
                .withClaim("roles",user?.roles)
                .withExpiresAt(expiry)
                .sign(Algorithm.HMAC256("secret"))

            return@post call.respond(token)
        }
    }

    route("/register") {
        post {
            val data = call.receive<User>()
            val hashed = BCrypt.hashpw(data.password, BCrypt.gensalt())
            val user = User(data.firstName,data.lastName,data.mobile,data.dateOfBirth,data.email, password = hashed,roles = data.roles)
            accountCollection.insertOne(user)
            call.respond(HttpStatusCode.Created)
        }
    }


    get {
        val principal = call.principal<JWTPrincipal>()
        val email = principal?.payload?.getClaim("email").toString().replace("\"","")
        val filter = "{email:$email}"
        val entity = accountCollection.findOne(filter)
        if(entity != null) {
            call.respond(entity)
        } else {
            call.respond(HttpStatusCode.NotFound)
        }
    }

    get("/{id}") {
        val id = call.parameters["id"].toString()
        val filter = "{_id:ObjectId('$id')}"
        val entity = accountCollection.findOne(filter)
        if (entity != null) {
            call.respond(entity)
        } else {
            call.respond((HttpStatusCode.NotFound))
        }
    }
}