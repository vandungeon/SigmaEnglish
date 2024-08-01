package com.example.sigmaenglish.main

import android.graphics.Rect
import android.view.ViewTreeObserver
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalView

fun calculateGradientAlpha(lazyListState: LazyListState): Float {
    val totalItems = lazyListState.layoutInfo.totalItemsCount
    val viewportHeight = lazyListState.layoutInfo.viewportSize.height
    val itemHeight = lazyListState.layoutInfo.visibleItemsInfo.firstOrNull()?.size ?: 1

    // Calculate the total scrollable height
    val totalScrollableHeight = (totalItems * itemHeight) - viewportHeight

    // Calculate the current scroll position
    val currentScrollOffset = (lazyListState.firstVisibleItemIndex * itemHeight) + lazyListState.firstVisibleItemScrollOffset

    return if (totalScrollableHeight > 0) {
        1f - (currentScrollOffset / totalScrollableHeight.toFloat())
    } else {
        1f // No scrollable content, so full alpha
    }
}


fun checkAnswer(userAnswer: String, correctAnswer: String): Boolean {
    return userAnswer.trim().equals(correctAnswer.trim(), ignoreCase = true)
}

@Composable
fun rememberKeyboardVisibilityObserver(): State<Boolean> {
    val isKeyboardVisible = remember { mutableStateOf(false) }
    val rootView = LocalView.current
    DisposableEffect(rootView) {
        val listener = ViewTreeObserver.OnGlobalLayoutListener {
            val rect = Rect()
            rootView.getWindowVisibleDisplayFrame(rect)
            val screenHeight = rootView.height
            val keypadHeight = screenHeight - rect.bottom
            isKeyboardVisible.value = keypadHeight > screenHeight * 0.15
        }
        rootView.viewTreeObserver.addOnGlobalLayoutListener(listener)
        onDispose {
            rootView.viewTreeObserver.removeOnGlobalLayoutListener(listener)
        }
    }
    return isKeyboardVisible
}