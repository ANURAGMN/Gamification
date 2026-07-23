package com.anurag.eduai.uikit.avatar

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.core.view.drawToBitmap
import com.anurag.eduai.uikit.avatar.core.AvatarState
import com.anurag.eduai.uikit.theme.EduAiTheme
import com.anurag.eduai.uikit.theme.EduThemeMode

private const val SHARE_SIZE_PX = 720

/**
 * Renders the tutor into an off-screen [ComposeView], waits one frame for layout,
 * then returns a PNG-ready bitmap. Must be invoked on the main thread.
 */
internal fun captureTutorShareBitmap(
    context: Context,
    config: TutorConfig,
    state: AvatarState = AvatarState.Happy,
    onReady: (Bitmap) -> Unit,
    onError: () -> Unit = {},
) {
    val activity = context.findActivity()
    if (activity == null) {
        onError()
        return
    }

    val composeView =
        ComposeView(activity).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnDetachedFromWindow)
            setContent {
                EduAiTheme(themeMode = EduThemeMode.Light) {
                    ShareAvatarCard(config = config, state = state)
                }
            }
        }

    val container =
        FrameLayout(activity).apply {
            visibility = android.view.View.INVISIBLE
            layoutParams = ViewGroup.LayoutParams(SHARE_SIZE_PX, SHARE_SIZE_PX)
        }
    container.addView(
        composeView,
        FrameLayout.LayoutParams(SHARE_SIZE_PX, SHARE_SIZE_PX),
    )

    val decor = activity.window.decorView as ViewGroup
    decor.addView(container)

    composeView.post {
        composeView.measure(
            android.view.View.MeasureSpec.makeMeasureSpec(SHARE_SIZE_PX, android.view.View.MeasureSpec.EXACTLY),
            android.view.View.MeasureSpec.makeMeasureSpec(SHARE_SIZE_PX, android.view.View.MeasureSpec.EXACTLY),
        )
        composeView.layout(0, 0, SHARE_SIZE_PX, SHARE_SIZE_PX)
        composeView.post {
            try {
                onReady(composeView.drawToBitmap(Bitmap.Config.ARGB_8888))
            } catch (_: Exception) {
                onError()
            } finally {
                decor.removeView(container)
            }
        }
    }
}

@Composable
private fun ShareAvatarCard(
    config: TutorConfig,
    state: AvatarState,
) {
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(32.dp))
                .background(Color(0xFFF0F4F8)),
        contentAlignment = Alignment.Center,
    ) {
        EduTutorAvatar(
            character = config.character,
            state = state,
            modifier = Modifier.fillMaxSize().padding(48.dp),
            outfitVariant = config.outfit,
            hairStyle = config.hair,
            hairColor = config.hairColor,
            glassesStyle = config.glasses,
            glassesColor = config.frameColor,
            neckStyle = config.neck,
            underEyeLine = config.eyeLine,
            cheekShading = config.cheeks,
        )
    }
}

internal fun Context.findActivity(): Activity? {
    var ctx = this
    while (ctx is ContextWrapper) {
        if (ctx is Activity) return ctx
        ctx = ctx.baseContext
    }
    return null
}
