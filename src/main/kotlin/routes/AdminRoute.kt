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
import io.ktor.server.request.*
import models.SearchUserDTO
import models.User


fun Route.initAdminRoute(db: MongoDatabase) {

    val accountCollection = db.getCollection<User>("accounts")
    val licenseCollection = db.getCollection<LearnerLicense>("licenses")


    route("/search") {

        post() {
            val data = call.receive<SearchUserDTO>()
            val firstNameFilter = "{firstName:/^${data.firstName}$/i}"
            val lastNameFilter = "{lastName: /^${data.lastName}\$/i}"
            val dateOfBirthFilter = "{dateOfBirth: /^${data.dateOfBirth}\$/i}"
            val filter = "{\$or:[$firstNameFilter, $lastNameFilter, $dateOfBirthFilter]}"
            val entity = accountCollection.findOne(filter)
            if (entity != null) {
                call.respond(entity)
            } else {
                call.respond((HttpStatusCode.NotFound))
            }
        }
    }


    route("/issue") {

        post("/learner") {
            val data = call.receive<LearnerLicense>()
            licenseCollection.insertOne(data)
            call.respond(HttpStatusCode.Created,data)
        }
    }

}