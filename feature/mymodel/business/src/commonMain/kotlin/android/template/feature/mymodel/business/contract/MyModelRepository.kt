package android.template.feature.mymodel.business.contract

import kotlinx.coroutines.flow.Flow

interface MyModelRepository {
    val myModels: Flow<List<String>>

    suspend fun add(name: String)
}
