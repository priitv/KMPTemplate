package android.template.feature.mymodel.business

import android.template.feature.mymodel.business.contract.MyModelRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface GetMyModels {
    operator fun invoke(): Flow<List<String>>
}

class GetMyModelsImpl @Inject constructor(
    private val myModelRepository: MyModelRepository
) : GetMyModels {
    override operator fun invoke() = myModelRepository.myModels
}