package by.ibrel.fmanager.model

data class LoginRequest(val email: String, val password: String, val locale : String = "GB")