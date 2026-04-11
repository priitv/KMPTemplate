package android.template.feature.mymodel.app

import android.template.feature.mymodel.app.MyModelUiState.Error
import android.template.feature.mymodel.app.MyModelUiState.Loading
import android.template.feature.mymodel.app.MyModelUiState.Success
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MyModelScreen(
    modifier: Modifier = Modifier,
    viewModel: MyModelViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    when (val s = state) {
        is Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        is Error -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Error: ${s.throwable.message}")
        }
        is Success -> MyModelScreen(
            items = s.data,
            onSave = { name -> viewModel.addMyModel(name) },
            modifier = modifier
        )
    }
}

@Composable
internal fun MyModelScreen(
    items: List<String>,
    modifier: Modifier = Modifier,
    onSave: (name: String) -> Unit
) {
    Column(modifier.statusBarsPadding()) {
        var nameMyModel by remember { mutableStateOf("Compose") }
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TextField(
                modifier = Modifier.weight(1F),
                value = nameMyModel,
                onValueChange = { nameMyModel = it }
            )

            Button(
                modifier = Modifier.width(96.dp),
                onClick = { onSave(nameMyModel) }
            ) {
                Text("Save")
            }
        }
        items.forEach {
            Text("Saved item: $it")
        }
    }
}
