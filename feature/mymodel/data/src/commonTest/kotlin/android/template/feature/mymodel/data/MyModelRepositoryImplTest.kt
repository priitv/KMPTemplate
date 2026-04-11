package android.template.feature.mymodel.data

import android.template.core.database.MyModel
import android.template.core.database.MyModelDao
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MyModelRepositoryImplTest {
    @Test
    fun `When item is saved, then it is returned in the list`() = runTest {
        val repository = MyModelRepositoryImpl(FakeMyModelDao())

        repository.add("Repository")

        repository.myModels.first().size shouldBe 1
    }
}

private class FakeMyModelDao : MyModelDao {
    private val data = mutableListOf<MyModel>()

    override fun getMyModels(): Flow<List<MyModel>> = flow {
        emit(data)
    }

    override suspend fun insertMyModel(item: MyModel) = data
        .add(0, item)
}
