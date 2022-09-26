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
    val _id: Id<LogbookEntry> = newId(),
)