package routes

import com.mongodb.client.MongoDatabase
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import models.LogbookEntry
import org.litote.kmongo.getCollection

fun Route.initLogbookRoute(db: MongoDatabase) {

    val logbookCollection = db.getCollection<LogbookEntry>("hours")


    route("/logbook"){
        post("/create"){

        }

        get{
            call.respondText("Hello  World")
        }

        delete{

        }

        put{

        }
    }


}