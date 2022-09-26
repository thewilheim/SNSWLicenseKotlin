package routes

import com.mongodb.client.MongoDatabase
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import models.LogbookEntry
import models.User
import org.litote.kmongo.*
import org.mindrot.jbcrypt.BCrypt

fun Route.initLogbookRoute(db: MongoDatabase) {

    val logbookCollection = db.getCollection<LogbookEntry>("hours")


    route("/logbook"){
        post("/create"){
            val data = call.receive<LogbookEntry>()
            logbookCollection.insertOne(data)

            call.respond(HttpStatusCode.Created)
        }

        get{
            val principal = call.principal<JWTPrincipal>()
            val email = principal?.payload?.getClaim("email").toString().replace("\"","")
            val filter = "{userEmail:$email}"
            val entries = logbookCollection.find(filter).toList()
            if(entries != null) {
                call.respond(entries)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        delete("/{id}"){
            val id = call.parameters["id"].toString()
            val filter = "{_id:ObjectId('$id')}"
            val entity = logbookCollection.deleteOne(filter)
            if (entity != null) {
                call.respond(entity)
            } else {
                call.respond((HttpStatusCode.NotFound))
            }
        }

        put{
            val entity = call.receive<LogbookEntry>()
            val result = logbookCollection.updateOne(entity)
            if (result.modifiedCount.toInt() == 1){
                call.respond(HttpStatusCode.OK, entity)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }


}