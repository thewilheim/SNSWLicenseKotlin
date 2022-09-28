package models

import kotlinx.serialization.Serializable


@Serializable
data class SearchUserDTO(
    val firstName: String,
    val lastName: String,
    val dateOfBirth: String,
    val mobile: String = "",
    val email: String = "",
)
