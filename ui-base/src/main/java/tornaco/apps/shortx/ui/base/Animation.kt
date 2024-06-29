@file:OptIn(ExperimentalAnimationApi::class)

package tornaco.apps.shortx.ui.base

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable

@Composable
fun AnimateExpand(
    expanded: Boolean,
    expandedContent: @Composable () -> Unit,
    unExpandedContent: @Composable () -> Unit,
) {

    AnimatedContent(
        targetState = expanded, label = "AnimateExpand"
    ) { targetExpanded ->
        if (targetExpanded) {
            expandedContent()
        } else {
            unExpandedContent()
        }
    }
}