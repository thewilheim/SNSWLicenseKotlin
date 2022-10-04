package routes

import com.mongodb.client.MongoDatabase
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import models.LearnerLicense
import org.litote.kmongo.*
import org.litote.kmongo.id.*
import LicenseClassDTO


fun Route.initLicenseRoute(db: MongoDatabase) {

    val licenseCollection = db.getCollection<LearnerLicense>("licenses")


    route("/license") {
        get() {
            val principal = call.principal<JWTPrincipal>()
            val email = principal!!.payload.getClaim("email").asString()
            val filter = "{userEmail:'$email'}"
            val userLicense = licenseCollection.findOne(filter)
            if(userLicense !== null) {
                val licenseDTO = LicenseClassDTO(_id = (userLicense._id as WrappedObjectId).id.toString() ,userEmail = userLicense.userEmail, issuedBy = userLicense.issuedBy, issuedDate = userLicense.issuedDate, practiceLogEntries = userLicense.practiceLogEntries, totalTime = userLicense.totalTime, totalNightTime = userLicense.totalNightTime, totalRemainingTime = userLicense.totalRemainingTime)
                call.respond(licenseDTO)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        get("/{email}") {
            val email = call.parameters["email"].toString()
            val filter = "{userEmail:'$email'}"
            val userLicense = licenseCollection.findOne(filter)
            if(userLicense !== null) {
                val licenseDTO = LicenseClassDTO(_id = (userLicense._id as WrappedObjectId).id.toString() ,userEmail = userLicense.userEmail, issuedBy = userLicense.issuedBy, issuedDate = userLicense.issuedDate, practiceLogEntries = userLicense.practiceLogEntries, totalTime = userLicense.totalTime, totalNightTime = userLicense.totalNightTime, totalRemainingTime = userLicense.totalRemainingTime)
                call.respond(licenseDTO)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }


}