package android.template.feature.mymodel.app

import android.template.feature.mymodel.app.MyModelUiState.*
import android.template.feature.mymodel.business.AddMyModel
import android.template.feature.mymodel.business.GetMyModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyModelViewModel @Inject constructor(
    getMyModels: GetMyModels,
    private val addMyModel: AddMyModel,
) : ViewModel() {

    val uiState: StateFlow<MyModelUiState> = getMyModels()
        .map<List<String>, MyModelUiState> { Success(data = it) }
        .catch { emit(Error(it)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Loading)

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
