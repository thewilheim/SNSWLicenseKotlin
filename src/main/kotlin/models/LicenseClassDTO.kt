import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import models.LogbookEntry
import java.util.*

@Serializable
data class LicenseClassDTO (
    val _id: String = "",
    val userEmail:String,
    val issuedBy: String,
    @Serializable(with = DateSerializer::class) @Contextual
    val issuedDate: Date,
    val practiceLogEntries: MutableList<LogbookEntry> = mutableListOf(),
    val totalTime: Long
)