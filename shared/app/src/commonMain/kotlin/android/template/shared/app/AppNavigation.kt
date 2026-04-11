package android.template.shared.app

import android.template.core.ui.MyApplicationTheme
import android.template.feature.mymodel.app.MyModelScreen
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable

@Serializable
object MyModelDestination

@Composable
fun AppNavigation() {
    MyApplicationTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = MyModelDestination) {
                composable<MyModelDestination> { MyModelScreen(modifier = Modifier.padding(16.dp)) }
            }
        }
    }
}
