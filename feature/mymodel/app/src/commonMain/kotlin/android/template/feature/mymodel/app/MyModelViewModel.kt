package android.template.feature.mymodel.app

import android.template.feature.mymodel.app.MyModelUiState.Success
import android.template.feature.mymodel.business.AddMyModelUseCase
import android.template.feature.mymodel.business.GetMyModelsUseCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MyModelViewModel(
    getMyModels: GetMyModelsUseCase,
    private val addMyModel: AddMyModelUseCase,
) : ViewModel() {
    val uiState: StateFlow<MyModelUiState> = getMyModels()
        .map<List<String>, MyModelUiState> { Success(data = it) }
        .catch { emit(MyModelUiState.Error(it)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), MyModelUiState.Loading)

    fun addMyModel(name: String) {
        viewModelScope.launch {
            addMyModel.invoke(name)
        }
    }
}

sealed interface MyModelUiState {
    object Loading : MyModelUiState
    data class Error(val throwable: Throwable) : MyModelUiState
    data class Success(val data: List<String>) : MyModelUiState
}
