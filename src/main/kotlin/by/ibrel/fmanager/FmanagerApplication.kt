package by.ibrel.fmanager

import by.ibrel.fmanager.component.LoginComponent
import by.ibrel.fmanager.component.RestrictionComponent
import by.ibrel.fmanager.component.TrainingComponent
import by.ibrel.fmanager.model.CommonTrainingRequestBody
import by.ibrel.fmanager.model.LoginRequest
import by.ibrel.fmanager.model.SpecializationTrainingType
import by.ibrel.fmanager.model.SpecializationsTrainingRequestBody
import by.ibrel.fmanager.model.TrainingRequest
import by.ibrel.fmanager.model.TrainingRequestBody
import by.ibrel.fmanager.model.TrainingRequestData
import by.ibrel.fmanager.model.TrainingType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.jetbrains.annotations.NotNull
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.time.Duration
import javax.annotation.PostConstruct

@SpringBootApplication
class FmanagerApplication(
    @NotNull @Value("\${user-data.username}") private val username: String,
    @NotNull @Value("\${user-data.password}") private val password: String,
    private val loginComponent: LoginComponent,
    private val trainingComponent: TrainingComponent,
    private val restrictionComponent: RestrictionComponent
) {

    companion object {
        private const val trainingPath = "/training/normal"
        private const val specializationTrainingPath = "/training/specializations/playmaking/train"
    }

    @PostConstruct
    fun run() {

        val loginRequest = LoginRequest(email = username, password = password)

//        if (!restrictionComponent.isAllow(loginRequest)) {
//            throw IllegalAccessException("User with username ${loginRequest.email} cannot use this application.")
//        }

        val response = loginComponent.login(loginRequest)

        val userAuthId = response.id.toString()
        val userToken = response.token

        runBlocking {

            launch(Dispatchers.Default) {
                doTraining(
                    TrainingRequestData(
                        body = SpecializationsTrainingRequestBody(SpecializationTrainingType.CROSS.path),
                        url = specializationTrainingPath,
                        duration = Duration.ofSeconds(20),
                        userAuthId = userAuthId,
                        userToken = userToken
                    )
                )
            }

            launch(Dispatchers.Default) {
                doTraining(
                    TrainingRequestData(
                        body = SpecializationsTrainingRequestBody(SpecializationTrainingType.TECHNICS.path),
                        url = specializationTrainingPath,
                        duration = Duration.ofSeconds(20),
                        userAuthId = userAuthId,
                        userToken = userToken
                    )
                )
            }

//            launch(Dispatchers.Default) {
//                doTraining(
//                    TrainingRequestData(
//                        body = CommonTrainingRequestBody(TrainingType.FREEKICKS.name.toLowerCase(), 35),
//                        url = trainingPath,
//                        userAuthId = userAuthId,
//                        userToken = userToken
//                    )
//                )
//            }
        }
    }

    private suspend fun <T : TrainingRequestBody> doTraining(request: TrainingRequest<T>) {
        coroutineScope {
            launch { trainingComponent.doTraining(request) }
        }
    }
}

fun main(args: Array<String>) {
    runApplication<FmanagerApplication>(*args)

}
