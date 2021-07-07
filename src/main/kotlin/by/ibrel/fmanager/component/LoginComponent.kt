package by.ibrel.fmanager.component

import by.ibrel.fmanager.model.LoginRequest
import by.ibrel.fmanager.model.LoginResponse

interface LoginComponent {
    fun login(request: LoginRequest) : LoginResponse
}