package models

import ObjectIdAsStringSerializer
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import org.litote.kmongo.newId

@Serializable
data class User(
    val firstName:String = "",
    val lastName:String = "",
    val mobile:String = "",
    val dateOfBirth:String = "",
    val email:String,
    val password:String,
    val roles : List<String> = listOf(),
    @Serializable(with = ObjectIdAsStringSerializer::class)
    val _id: Id<User> = newId()
)