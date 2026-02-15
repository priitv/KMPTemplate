package android.template.feature.mymodel.app

import android.template.feature.mymodel.business.AddMyModel
import android.template.feature.mymodel.business.GetMyModels
import android.template.feature.mymodel.business.contract.MyModelRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@OptIn(ExperimentalCoroutinesApi::class) // TODO: Remove when stable
class MyModelViewModelTest {
    private val repository = FakeMyModelRepository()
    private val getMyModels = FakeGetMyModels(repository)
    private val addMyModel = FakeAddMyModel(repository)

    @Test
    fun uiState_initiallyLoading() = runTest {
        val viewModel = MyModelViewModel(getMyModels, addMyModel)
        assertEquals(viewModel.uiState.first(), MyModelUiState.Loading)
    }

    @Test
    fun uiState_onItemSaved_isDisplayed() = runTest {
        val viewModel = MyModelViewModel(getMyModels, addMyModel)
        assertEquals(viewModel.uiState.first(), MyModelUiState.Loading)
    }
}

private class FakeGetMyModels(private val repository: MyModelRepository) : GetMyModels {
    override fun invoke(): Flow<List<String>> = repository.myModels
}

private class FakeAddMyModel(private val repository: MyModelRepository) : AddMyModel {
    override suspend fun invoke(name: String) = repository.add(name)
}

private class FakeMyModelRepository : MyModelRepository {
    private val data = mutableListOf<String>()
    override val myModels: Flow<List<String>>
        get() = flow { emit(data.toList()) }

    override suspend fun add(name: String) {
        data.add(0, name)
    }
}
