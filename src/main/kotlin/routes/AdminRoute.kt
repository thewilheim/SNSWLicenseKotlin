package routes

import com.mongodb.client.MongoDatabase
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import models.LearnerLicense
import org.litote.kmongo.*
import RoleBasedAuthorization
import io.ktor.server.request.*
import models.SearchUserDTO
import models.User


fun Route.initAdminRoute(db: MongoDatabase) {

    val accountCollection = db.getCollection<User>("accounts")
    val licenseCollection = db.getCollection<LearnerLicense>("licenses")


    route("/search") {
        install(RoleBasedAuthorization){roles= listOf("CSR")}
        post() {
            val data = call.receive<SearchUserDTO>()
            val firstNameFilter = "{firstName:/^${data.firstName}$/i}"
            val lastNameFilter = "{lastName: /^${data.lastName}\$/i}"
            val dateOfBirthFilter = "{dateOfBirth: /^${data.dateOfBirth}\$/i}"
            val mobileFilter = "{mobile: /^${data.mobile}\$/i}"
            val emailFilter = "{email: /^${data.email}\$/i}"

            val filter = "{\$or:[$firstNameFilter, $lastNameFilter, $dateOfBirthFilter, $mobileFilter, $emailFilter]}"
            val entity = accountCollection.find(filter).toList()
            if (entity != null && entity.isNotEmpty()) {
                call.respond(entity)
            } else {
                call.respond((HttpStatusCode.NotFound))
            }
        }
    }


    route("/issue") {
        install(RoleBasedAuthorization){roles= listOf("CSR")}
        post("/learner") {
            val data = call.receive<LearnerLicense>()
            licenseCollection.insertOne(data)
            call.respond(HttpStatusCode.Created,data)
        }
    }

}