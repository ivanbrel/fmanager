package by.ibrel.fmanager.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class LoginResponse(
    val id: Int,
    val token: String
)