@file:OptIn(ExperimentalMaterial3Api::class)

package tornaco.apps.shortx

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import org.lsposed.hiddenapibypass.HiddenApiBypass
import tornaco.apps.shortx.ui.base.AnimateExpand
import tornaco.apps.shortx.ui.base.ListItem
import tornaco.apps.shortx.ui.base.ShortXAppBarScaffold
import tornaco.apps.shortx.ui.theme.ShortXTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            HiddenApiBypass.addHiddenApiExemptions("")
        }
        enableEdgeToEdge()
        setContent {
            ShortXTheme {
                ShortXAppBarScaffold(title = "ShortX Core") {
                    Column(
                        Modifier
                            .fillMaxSize()
                    ) {

                        var isExpand by remember {
                            mutableStateOf(false)
                        }
                        AnimateExpand(expanded = isExpand, expandedContent = {
                            ListItem(title = "Hello", onClick = {
                                isExpand = false
                            })
                            ListItem(title = "Hello", onClick = {
                                isExpand = false
                            })
                        }) {
                            Button(onClick = { isExpand = true }) {
                                Text(text = "Hello")
                            }
                        }

                        repeat(20) {
                            ListItem(title = "Hello- $it")
                        }
                    }
                }
            }
        }
    }
}