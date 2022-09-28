package models

import ObjectIdAsStringSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import DateSerializer
import org.litote.kmongo.Id
import org.litote.kmongo.newId
import java.util.Date

@Serializable
data class LogbookEntry(
    @Serializable(with = DateSerializer::class) @Contextual
    val startTime: Date,
    @Serializable(with = DateSerializer::class) @Contextual
    val endTime: Date,
    val instructorLed: Boolean,
    val totalMilliseconds: Long = endTime.time - startTime.time,
    @Serializable(with = ObjectIdAsStringSerializer::class)
    val _id: Id<LogbookEntry> = newId(),

)


@Serializable
data class LearnerLicense (
    @Serializable(with = ObjectIdAsStringSerializer::class)
    val _id: Id<LearnerLicense> = newId(),
    val userEmail:String,
    val issuedBy: String,
    @Serializable(with = DateSerializer::class) @Contextual
    val issuedDate: Date,
    val practiceLogEntries: MutableList<LogbookEntry> = mutableListOf()
        ) {
    val totalTime:Long
    get() {
        var total: Long = 0
        for(c in practiceLogEntries){
            total += c.totalMilliseconds
        }
        return total
    }
}