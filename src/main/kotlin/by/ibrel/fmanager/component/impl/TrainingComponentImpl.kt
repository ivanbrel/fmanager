package by.ibrel.fmanager.component.impl

import by.ibrel.fmanager.component.TrainingComponent
import by.ibrel.fmanager.model.TrainingRequest
import by.ibrel.fmanager.model.TrainingRequestBody
import mu.KLogging
import org.jetbrains.annotations.NotNull
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import reactor.core.publisher.Mono
import reactor.util.retry.Retry
import java.time.Duration
import java.util.concurrent.atomic.AtomicInteger


@Component
final class TrainingComponentImpl(
    private val webClient: WebClient,
    @NotNull @Value("\${url.base}") private val baseUrl: String
) : TrainingComponent {

    companion object : KLogging();

    override fun <T : TrainingRequestBody> doTraining(request: TrainingRequest<T>) {

        val count = AtomicInteger(0)

        webClient
            .post()
            .uri("$baseUrl/${request.url()}")
            .bodyValue(request.body())
            .headers {
                it.add("x-auth-id", request.userAuthId())
                it.add("x-country", "gb")
                it.add("x-auth-token", request.userToken())
            }
            .retrieve()
            .bodyToMono(Array<String>::class.java)
            .repeat(request.count())
            .retryWhen(
                Retry.indefinitely()
                    .filter { ex -> ex is WebClientResponseException.UnprocessableEntity }
                    .doBeforeRetryAsync { Mono.delay(request.duration()).then() }
            )
            .retryWhen(
                Retry.indefinitely()
                    .filter { it is WebClientResponseException.BadRequest }
                    .maxAttempts(request.count() / 100 * 25)
                    .doBeforeRetryAsync { Mono.delay(Duration.ofSeconds(5)).then() }
            )
            .map {
                logger.info {
                    "\nAttempt details: \nthread-id: ${Thread.currentThread().id} \ntraining-count: ${count.incrementAndGet()}"
                }
            }
            .subscribe()
    }
}