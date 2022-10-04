package models

import ObjectIdAsStringSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import DateSerializer
import io.ktor.server.util.*
import org.litote.kmongo.Id
import org.litote.kmongo.newId
import java.time.Duration
import java.time.LocalDateTime
import java.util.Calendar
import java.util.Date
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

@Serializable
data class LogbookEntry(
    @Serializable(with = DateSerializer::class) @Contextual
    val startTime: Date,
    @Serializable(with = DateSerializer::class) @Contextual
    val endTime: Date,
    val instructorLed: Boolean,
    @Serializable(with = ObjectIdAsStringSerializer::class)
    val _id: Id<LogbookEntry> = newId(),

) {
    val totalMilliseconds: Long = _getMilliseconds()
    private fun _getMilliseconds():Long {

        var total: Long = 0

        if(instructorLed && endTime.time - startTime.time < 7200000L && endTime.time - startTime.time >= 3600000L) {
            total = (endTime.time - startTime.time) +  7200000L
        } else {
            total = endTime.time - startTime.time
        }
        return total
    }
}


@Serializable
data class LearnerLicense (
    @Serializable(with = ObjectIdAsStringSerializer::class)
    val _id: Id<LearnerLicense> = newId(),
    val userEmail:String,
    val issuedBy: String,
    @Serializable(with = DateSerializer::class) @Contextual
    val issuedDate: Date,
    val practiceLogEntries: MutableList<LogbookEntry> = mutableListOf(),
        ) {

    val totalTime: Long
    get() {
       var total: Long = 0
       for (c in practiceLogEntries) {
           total  += c.totalMilliseconds
       }
        return total
    }

    val totalNightTime:Long
    get() {
        var total: Long = 0

        for(c in practiceLogEntries) {
            val cal = Calendar.getInstance()
            cal.time = c.startTime
            val hour = cal.get(Calendar.HOUR_OF_DAY)
            if(hour >= 17) {
                total += c.totalMilliseconds
            }
        }

        return total
    }

    val totalRemainingTime: Long
    get(){
        var total = 432000000L

        total -= (totalNightTime + totalTime)

        if(total <= 0) {
            total = 0
            return total
        }

        return total
    }
}