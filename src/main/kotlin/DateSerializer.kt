import kotlinx.serialization.*
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

@Serializer(forClass = DateSerializer::class)
object DateSerializer : KSerializer<Date> {
    override val descriptor: SerialDescriptor get() = PrimitiveSerialDescriptor("Date Serializer", PrimitiveKind.STRING)

    override fun serialize(output: Encoder, obj: Date) {
        output.encodeString(obj.time.toString())
    }

    override fun deserialize(input: Decoder): Date {


        var s = input.decodeString().replace("T", " ")

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

        var localDate = LocalDateTime.parse(s, formatter)

        val zdt = localDate.atZone(ZoneId.of("Australia/Sydney"));

        val milliseconds = zdt.toInstant().toEpochMilli()

        return Date(milliseconds)
    }
}