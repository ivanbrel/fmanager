package by.ibrel.fmanager.component

import by.ibrel.fmanager.model.LoginRequest

interface RestrictionComponent {
    fun isAllow(loginRequest: LoginRequest) : Boolean
}