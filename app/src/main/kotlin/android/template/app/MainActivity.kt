package android.template.app

import android.os.Bundle
import android.template.shared.app.AppNavigation
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { AppNavigation() }
    }
}

