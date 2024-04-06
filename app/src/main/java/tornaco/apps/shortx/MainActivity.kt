package tornaco.apps.shortx

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import org.lsposed.hiddenapibypass.HiddenApiBypass
import tornaco.apps.shortx.core.shortXManager
import tornaco.apps.shortx.ui.base.ListItem

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            HiddenApiBypass.addHiddenApiExemptions("")
        }
        setContent {
            Scaffold {
                Column(modifier = Modifier.padding(it)) {
                    ListItem(title = "ShortX: ${shortXManager.version()}")
                }
            }
        }
    }
}