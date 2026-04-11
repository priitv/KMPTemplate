package android.template.feature.mymodel.data

import android.template.core.database.MyModel
import android.template.core.database.MyModelDao
import android.template.feature.mymodel.business.contract.MyModelRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single
class MyModelRepositoryImpl(
    private val myModelDao: MyModelDao
) : MyModelRepository {
    override val myModels: Flow<List<String>> = myModelDao
        .getMyModels()
        .map { items ->
            items.map { it.name }
        }

    override suspend fun add(name: String) = myModelDao
        .insertMyModel(MyModel(name = name))
}
