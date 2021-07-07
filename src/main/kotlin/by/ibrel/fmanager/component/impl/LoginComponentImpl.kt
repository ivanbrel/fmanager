package by.ibrel.fmanager.component.impl

import by.ibrel.fmanager.component.LoginComponent
import by.ibrel.fmanager.model.LoginRequest
import by.ibrel.fmanager.model.LoginResponse
import org.jetbrains.annotations.NotNull
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class LoginComponentImpl(
    @NotNull @Value("\${url.base}") private val baseUrl: String,
    private val restTemplate: RestTemplate
) : LoginComponent {

    companion object {
        private const val loginPath = "/auth/login"
    }

    override fun login(request: LoginRequest): LoginResponse {
        val url = "${baseUrl}${loginPath}"
        return restTemplate.postForEntity(
            url,
            request,
            LoginResponse::class.java
        )
            .let {
                if (it.statusCode.is2xxSuccessful) {
                    it.body!!
                } else throw RuntimeException("Authentication failed!")
            }
    }
}