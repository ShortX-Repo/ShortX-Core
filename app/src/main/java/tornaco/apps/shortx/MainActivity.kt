@file:OptIn(ExperimentalMaterial3Api::class)

package tornaco.apps.shortx

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import org.lsposed.hiddenapibypass.HiddenApiBypass
import tornaco.apps.shortx.core.shortXManager
import tornaco.apps.shortx.ui.base.ListItem
import tornaco.apps.shortx.ui.theme.ShortXTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            HiddenApiBypass.addHiddenApiExemptions("")
        }
        enableEdgeToEdge()
        setContent {
            val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
            ShortXTheme {
                Scaffold(
                    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                    topBar = {
                        TopAppBar(title = {
                            Text(text = "Core")
                        }, scrollBehavior = scrollBehavior)
                    }) {
                    LazyColumn(Modifier.consumeWindowInsets(it)) {
                        items(100) {
                            ListItem(title = "ShortX: ${shortXManager.version()}")
                        }
                    }
                }
            }
        }
    }
}