package by.ibrel.fmanager.model

import java.io.Serializable
import java.time.Duration

interface TrainingRequest<out T : TrainingRequestBody> {
    fun body(): T
    fun url(): String
    fun count(): Long
    fun duration(): Duration
    fun userAuthId(): String
    fun userToken(): String
}

interface TrainingRequestBody : Serializable

data class CommonTrainingRequestBody(val skill: String, val duration: Int) : TrainingRequestBody

data class SpecializationsTrainingRequestBody(val specialization: String) : TrainingRequestBody

data class TrainingRequestData<out T : TrainingRequestBody>(
    val body: T,
    val url: String,
    val duration: Duration = Duration.ofSeconds(35),
    val count: Long = 1000,
    val userAuthId: String,
    val userToken: String
) : TrainingRequest<T> {


    override fun body(): T = body
    override fun url(): String = url
    override fun count(): Long = count
    override fun duration(): Duration = duration
    override fun userAuthId(): String = userAuthId
    override fun userToken(): String = userToken
}