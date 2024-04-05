/*
 * (C) Copyright 2022 Thanox
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

@file:OptIn(ExperimentalFoundationApi::class)

package tornaco.apps.shortx.ui.base

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed

fun Modifier.clickableWithRipple(onLongClick: (() -> Unit)? = null, onClick: () -> Unit) =
    composed {
        combinedClickable(
            interactionSource = remember { MutableInteractionSource() },
            // You can also change the color and radius of the ripple
            indication = rememberRipple(bounded = true),
            onClick = onClick,
            onLongClick = onLongClick,
        )
    }

fun Modifier.clickableWithRippleBorderless(onLongClick: (() -> Unit)? = null, onClick: () -> Unit) =
    composed {
        combinedClickable(
            interactionSource = remember { MutableInteractionSource() },
            // You can also change the color and radius of the ripple
            indication = rememberRipple(bounded = false),
            onClick = onClick,
            onLongClick = onLongClick,
        )
    }