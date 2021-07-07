package by.ibrel.fmanager.component

import by.ibrel.fmanager.model.TrainingRequest
import by.ibrel.fmanager.model.TrainingRequestBody

interface TrainingComponent {
    fun <T : TrainingRequestBody> doTraining(request: TrainingRequest<T>)
}