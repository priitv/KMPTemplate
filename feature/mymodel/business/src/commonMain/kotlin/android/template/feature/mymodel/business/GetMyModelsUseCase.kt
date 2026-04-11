package android.template.feature.mymodel.business

import android.template.feature.mymodel.business.contract.MyModelRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

interface GetMyModelsUseCase {
    operator fun invoke(): Flow<List<String>>
}

@Factory
class GetMyModelsUseCaseImpl(
    private val myModelRepository: MyModelRepository
) : GetMyModelsUseCase {
    override operator fun invoke() = myModelRepository.myModels
}
