package routes

import com.mongodb.client.MongoDatabase
import io.ktor.server.routing.*
import models.User
import org.litote.kmongo.getCollection

fun Route.initAccountRoute(db: MongoDatabase) {

    val accountCollection = db.getCollection<User>("accounts")


    route("/login"){
        post {  }
    }

    route("/register") {
        post {  }
    }


}