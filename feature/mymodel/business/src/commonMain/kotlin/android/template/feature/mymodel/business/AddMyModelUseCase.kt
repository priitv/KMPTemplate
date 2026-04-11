package android.template.feature.mymodel.business

import android.template.feature.mymodel.business.contract.MyModelRepository
import org.koin.core.annotation.Factory

interface AddMyModelUseCase {
    suspend operator fun invoke(name: String)
}

@Factory
class AddMyModelUseCaseImpl(
    private val myModelRepository: MyModelRepository
) : AddMyModelUseCase {
    override suspend operator fun invoke(name: String) = myModelRepository.add(name)
}
