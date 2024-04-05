package tornaco.apps.shortx

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import tornaco.apps.shortx.core.shortXManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Scaffold {
                Column(modifier = Modifier.padding(it)) {
                    Text(text = "ShortX: ${shortXManager.version()}")
                }
            }
        }
    }
}