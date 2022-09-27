package models

import ObjectIdAsStringSerializer
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import org.litote.kmongo.newId

@Serializable
data class LogbookEntry(
    val startTime: String,
    val endTime: String,
    val instructorLed: Boolean,
    @Serializable(with = ObjectIdAsStringSerializer::class)
    val _id: Id<LogbookEntry> = newId(),
    val userEmail: String,
    var totalHours: Int = 0
)