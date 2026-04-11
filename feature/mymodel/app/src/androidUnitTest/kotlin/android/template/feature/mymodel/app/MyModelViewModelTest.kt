package android.template.feature.mymodel.app

import android.template.feature.mymodel.business.AddMyModelUseCase
import android.template.feature.mymodel.business.GetMyModelsUseCase
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MyModelViewModelTest {
    private val getMyModels = mockk<GetMyModelsUseCase>()
    private val addMyModel = mockk<AddMyModelUseCase>()

    private val testDispatcher = UnconfinedTestDispatcher()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `When ViewModel is created, then initial state is Loading`() = runTest {
        every { getMyModels() } returns emptyFlow()
        val viewModel = MyModelViewModel(getMyModels, addMyModel)
        viewModel.uiState.value shouldBe MyModelUiState.Loading
    }

    @Test
    fun `When models are available, then state is Success with items`() = runTest {
        val items = listOf("item1")
        every { getMyModels() } returns flowOf(items)
        val viewModel = MyModelViewModel(getMyModels, addMyModel)
        val result = viewModel.uiState.first { it is MyModelUiState.Success }
        result shouldBe MyModelUiState.Success(items)
    }
}
