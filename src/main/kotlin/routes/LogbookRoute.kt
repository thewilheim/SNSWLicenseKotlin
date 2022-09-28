package routes

import com.mongodb.client.MongoDatabase
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import models.LearnerLicense
import models.LogbookEntry
import models.User
import org.litote.kmongo.*
import org.mindrot.jbcrypt.BCrypt

fun Route.initLogbookRoute(db: MongoDatabase) {

    val logbookCollection = db.getCollection<LogbookEntry>("hours")
    val licenseCollection = db.getCollection<LearnerLicense>("licenses")

    route("/logbook"){
        post("/create"){
            val principal = call.principal<JWTPrincipal>()
            val email = principal!!.payload.getClaim("email").asString()
            val filter = "{userEmail:'$email'}"

            val userLicense = licenseCollection.findOne(filter)

            if(userLicense === null) {
                return@post call.respond(HttpStatusCode.NotFound)
            }

            val data = call.receive<LogbookEntry>()

            userLicense.practiceLogEntries.add(LogbookEntry(data.startTime,data.endTime,data.instructorLed))
            licenseCollection.updateOne(userLicense)

            call.respond(HttpStatusCode.Created, data)
        }

        get{
            val principal = call.principal<JWTPrincipal>()
            val email = principal?.payload?.getClaim("email").toString().replace("\"","")
            val filter = "{userEmail:'$email'}"

            val userLicense = licenseCollection.findOne(filter)
            if(userLicense === null) {
                return@get call.respond(HttpStatusCode.NotFound)
            }

            val entries = userLicense.practiceLogEntries

            call.respond(entries)
        }

        delete("/{id}"){
            val principal = call.principal<JWTPrincipal>()
            val email = principal?.payload?.getClaim("email").toString().replace("\"","")
            val userFilter = "{userEmail:'$email'}"
            val userLicense = licenseCollection.findOne(userFilter)

            if(userLicense === null) {
                return@delete call.respond(HttpStatusCode.NotFound)
            }

            val id = call.parameters["id"].toString()
            val filter = "{practiceLogEntries:[_id:ObjectId('$id')]}"
            val entity = logbookCollection.deleteOne(filter)

            if (entity != null) {
                call.respond(entity)
            } else {
                call.respond((HttpStatusCode.NotFound))
            }
        }

    }


}