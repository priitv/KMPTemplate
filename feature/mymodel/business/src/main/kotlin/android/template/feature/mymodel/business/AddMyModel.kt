package android.template.feature.mymodel.business

import android.template.feature.mymodel.business.contract.MyModelRepository
import javax.inject.Inject

interface AddMyModel {
    suspend operator fun invoke(name: String)
}

class AddMyModelImpl @Inject constructor(
    private val myModelRepository: MyModelRepository
) : AddMyModel {
    override suspend operator fun invoke(name: String) = myModelRepository.add(name)
}