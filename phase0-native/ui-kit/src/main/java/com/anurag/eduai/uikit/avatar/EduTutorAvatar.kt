package com.anurag.eduai.uikit.avatar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import com.anurag.eduai.uikit.avatar.animation.AvatarAnimationEngine
import com.anurag.eduai.uikit.avatar.core.AvatarFrame
import com.anurag.eduai.uikit.avatar.core.AvatarState
import com.anurag.eduai.uikit.avatar.core.TutorCharacter
import com.anurag.eduai.uikit.avatar.renderer.AvatarRenderer
import com.anurag.eduai.uikit.avatar.renderer.OrbAvatarRenderer

/**
 * Drives an [AvatarAnimationEngine] on the Compose frame clock and emits a fresh
 * [AvatarFrame] each frame — so the avatar breathes, blinks, sways, and reacts
 * even when fully idle. [state] can be changed to shift the tutor's mood.
 */
@Composable
fun rememberTutorFrame(state: AvatarState = AvatarState.Idle): AvatarFrame {
    val engine = remember { AvatarAnimationEngine() }
    LaunchedEffect(state) { engine.setState(state) }
    var frame by remember { mutableStateOf(engine.update(0L, 16L)) }
    LaunchedEffect(Unit) {
        var last = withFrameNanos { it }
        while (true) {
            val now = withFrameNanos { it }
            val deltaMs = ((now - last) / 1_000_000L).coerceIn(1L, 64L)
            last = now
            frame = engine.update(now / 1_000_000L, deltaMs)
        }
    }
    return frame
}

/**
 * Single entry point for the ported tutor avatars. [TutorCharacter.Orb] renders
 * the reactive glowing orb; anything else renders the code-drawn "Free" face.
 * Both are pure Compose — no Rive, no assets.
 */
@Composable
fun EduTutorAvatar(
    character: TutorCharacter = TutorCharacter.Orb,
    state: AvatarState = AvatarState.Idle,
    modifier: Modifier = Modifier,
    outfitVariant: Int = 0,
    hairStyle: Int = 0,
    hairColor: Int = 0,
    glassesStyle: Int = 0,
    glassesColor: Int = 0,
    neckStyle: Int = 0,
    underEyeLine: Boolean = false,
    cheekShading: Boolean = true,
    moodOverride: Int = 0,
    gestureOverride: Int = 0,
    spinTrigger: Int = 0,
) {
    val frame = rememberTutorFrame(state)
    when (character) {
        TutorCharacter.Orb -> OrbAvatarRenderer(frame = frame, modifier = modifier)
        else ->
            AvatarRenderer(
                frame = frame,
                modifier = modifier,
                outfitVariant = outfitVariant,
                hairStyle = hairStyle,
                hairColor = hairColor,
                glassesStyle = glassesStyle,
                glassesColor = glassesColor,
                neckStyle = neckStyle,
                underEyeLine = underEyeLine,
                cheekShading = cheekShading,
                moodOverride = moodOverride,
                gestureOverride = gestureOverride,
                spinTrigger = spinTrigger,
            )
    }
}
