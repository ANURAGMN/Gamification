package com.anurag.eduai.uikit.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anurag.eduai.uikit.avatar.AdRewardOverlay
import com.anurag.eduai.uikit.avatar.AdRewardRequest
import com.anurag.eduai.uikit.avatar.AvatarPreset
import com.anurag.eduai.uikit.avatar.AvatarUnlockStore
import com.anurag.eduai.uikit.avatar.EduTutorAvatar
import com.anurag.eduai.uikit.avatar.TutorConfig
import com.anurag.eduai.uikit.avatar.TutorConfigStore
import com.anurag.eduai.uikit.avatar.core.AvatarState
import com.anurag.eduai.uikit.avatar.core.TutorCharacter
import com.anurag.eduai.uikit.avatar.daysUntilNextDrop
import com.anurag.eduai.uikit.avatar.rememberSavedTutorConfig
import com.anurag.eduai.uikit.avatar.rememberUnlockedAvatars
import com.anurag.eduai.uikit.avatar.shareAvatar
import com.anurag.eduai.uikit.avatar.weeklyAvatarPresets
import com.anurag.eduai.uikit.components.EduChip
import com.anurag.eduai.uikit.components.EduPrimaryButton
import com.anurag.eduai.uikit.components.EduSecondaryButton
import com.anurag.eduai.uikit.components.SectionHeader
import com.anurag.eduai.uikit.components.pressScaleClickable
import com.anurag.eduai.uikit.components.rememberEduFeedback
import com.anurag.eduai.uikit.theme.EduAiTheme
import com.anurag.eduai.uikit.theme.EduChipRole
import kotlinx.coroutines.delay

/**
 * Playground for the ported avatars — pick Orb or the code-drawn Free character,
 * set a mood, and (for Free) cycle through every customization the Animation
 * project shipped: outfit, neck, hair, hair colour, glasses, frame colour,
 * under-eye line, cheek shading, mood/gesture overrides, and a 360° spin.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EduAvatarStudioScreen(modifier: Modifier = Modifier) {
    val colors = EduAiTheme.colors
    val context = LocalContext.current
    val saved = rememberSavedTutorConfig()

    // Draft seeded from the saved look; edits are live in the preview until saved.
    var character by rememberSaveable { mutableStateOf(saved.character) }
    var state by rememberSaveable { mutableStateOf(AvatarState.Idle) }
    var outfit by rememberSaveable { mutableIntStateOf(saved.outfit) }
    var neck by rememberSaveable { mutableIntStateOf(saved.neck) }
    var hair by rememberSaveable { mutableIntStateOf(saved.hair) }
    var hairColor by rememberSaveable { mutableIntStateOf(saved.hairColor) }
    var glasses by rememberSaveable { mutableIntStateOf(saved.glasses) }
    var frameColor by rememberSaveable { mutableIntStateOf(saved.frameColor) }
    var eyeLine by rememberSaveable { mutableStateOf(saved.eyeLine) }
    var cheeks by rememberSaveable { mutableStateOf(saved.cheeks) }
    var mood by rememberSaveable { mutableIntStateOf(0) }
    var gesture by rememberSaveable { mutableIntStateOf(0) }
    var spin by rememberSaveable { mutableIntStateOf(0) }
    var justSaved by rememberSaveable { mutableStateOf(false) }

    val feedback = rememberEduFeedback()
    LaunchedEffect(justSaved) {
        if (justSaved) {
            delay(1600)
            justSaved = false
        }
    }

    val weekly = remember { weeklyAvatarPresets() }
    val unlockedIds = rememberUnlockedAvatars()
    val daysLeft = remember { daysUntilNextDrop() }
    var adRequest by remember { mutableStateOf<AdRewardRequest?>(null) }

    fun currentDraftConfig() =
        TutorConfig(
            character = character,
            outfit = outfit,
            neck = neck,
            hair = hair,
            hairColor = hairColor,
            glasses = glasses,
            frameColor = frameColor,
            eyeLine = eyeLine,
            cheeks = cheeks,
        )

    Box(modifier = modifier.fillMaxSize()) {
      Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(colors.surface1)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 14.dp)
                .padding(bottom = 48.dp),
      ) {
        SectionHeader(title = "Avatar studio")

        // Character toggle
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(TutorCharacter.Orb, TutorCharacter.Free).forEach { c ->
                EduChip(
                    label = c.label,
                    role = if (character == c) EduChipRole.Accent else EduChipRole.Neutral,
                    onClick = { character = c },
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(20.dp))
                    .background(colors.surface2),
            contentAlignment = Alignment.Center,
        ) {
            EduTutorAvatar(
                character = character,
                state = state,
                modifier = Modifier.fillMaxSize().padding(12.dp),
                outfitVariant = outfit,
                hairStyle = hair,
                hairColor = hairColor,
                glassesStyle = glasses,
                glassesColor = frameColor,
                neckStyle = neck,
                underEyeLine = eyeLine,
                cheekShading = cheeks,
                moodOverride = mood,
                gestureOverride = gesture,
                spinTrigger = spin,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        StudioLabel("Mood")
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            AvatarState.entries.forEach { s ->
                CycleChip(text = s.name, selected = state == s, onClick = { state = s })
            }
        }

        if (character == TutorCharacter.Free) {
            Spacer(modifier = Modifier.height(18.dp))
            StudioLabel("Customize")
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                CycleChip(outfitLabel(outfit)) { outfit = (outfit + 1) % 4 }
                CycleChip(neckLabel(neck)) { neck = (neck + 1) % 3 }
                CycleChip(hairLabel(hair)) { hair = (hair + 1) % 3 }
                CycleChip(hairColorLabel(hairColor)) { hairColor = (hairColor + 1) % 3 }
                CycleChip(glassesLabel(glasses)) { glasses = (glasses + 1) % 3 }
                CycleChip(frameLabel(frameColor)) { frameColor = (frameColor + 1) % 3 }
                CycleChip(if (eyeLine) "Eye line: On" else "Eye line: Off") { eyeLine = !eyeLine }
                CycleChip(if (cheeks) "Cheeks: On" else "Cheeks: Off") { cheeks = !cheeks }
            }

            Spacer(modifier = Modifier.height(18.dp))
            StudioLabel("Expression")
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                CycleChip(moodLabel(mood)) { mood = (mood + 1) % 6 }
                CycleChip(gestureLabel(gesture)) { gesture = (gesture + 1) % 6 }
                CycleChip("Spin 360°", role = EduChipRole.Accent) { spin += 1 }
            }
        }

        Spacer(modifier = Modifier.height(22.dp))
        EduPrimaryButton(
            text =
                when {
                    justSaved -> "Saved ✓"
                    else -> "Save · 2 ads"
                },
            onClick = {
                if (justSaved) return@EduPrimaryButton
                adRequest =
                    AdRewardRequest(
                        sessionId = "save_custom",
                        actionLabel = "Saving your tutor",
                    )
            },
            fillMaxWidth = true,
        )
        Spacer(modifier = Modifier.height(10.dp))
        EduSecondaryButton(
            text = "Share with friends",
            onClick = {
                shareAvatar(
                    context = context,
                    avatarName = "my custom tutor",
                    config = currentDraftConfig(),
                )
            },
            fillMaxWidth = true,
        )
        Text(
            text = "Watch 2 ads to save. Your tutor appears on Home and in celebrations.",
            color = colors.textMuted,
            fontSize = 11.sp,
            modifier = Modifier.padding(top = 8.dp),
        )

        Spacer(modifier = Modifier.height(26.dp))
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "This week's avatars",
                color = colors.text,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = if (daysLeft == 1) "New in 1 day" else "New in $daysLeft days",
                color = colors.accent,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
            )
        }
        Text(
            text = "Watch 2 ads to unlock. Yours to keep and share.",
            color = colors.textMuted,
            fontSize = 11.sp,
            modifier = Modifier.padding(bottom = 10.dp),
        )
        Row(
            modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            weekly.forEach { preset ->
                WeeklyAvatarCard(
                    preset = preset,
                    unlocked = unlockedIds.contains(preset.id),
                    onUnlock = {
                        adRequest =
                            AdRewardRequest(
                                sessionId = "unlock_${preset.id}",
                                actionLabel = "Unlocking “${preset.name}”",
                            )
                    },
                    onUse = {
                        TutorConfigStore.save(context, preset.config)
                        character = preset.config.character
                        outfit = preset.config.outfit
                        neck = preset.config.neck
                        hair = preset.config.hair
                        hairColor = preset.config.hairColor
                        glasses = preset.config.glasses
                        frameColor = preset.config.frameColor
                        eyeLine = preset.config.eyeLine
                        cheeks = preset.config.cheeks
                        feedback.claim()
                    },
                    onShare = { shareAvatar(context, preset.name, preset.config) },
                )
            }
        }
      }

      AdRewardOverlay(
          request = adRequest,
          onComplete = {
              when {
                  adRequest?.sessionId == "save_custom" -> {
                      TutorConfigStore.save(context, currentDraftConfig())
                      feedback.claim()
                      justSaved = true
                  }
                  adRequest?.sessionId?.startsWith("unlock_") == true -> {
                      val presetId = adRequest!!.sessionId.removePrefix("unlock_")
                      weekly.find { it.id == presetId }?.let {
                          AvatarUnlockStore.unlock(context, it.id)
                      }
                      feedback.reward()
                  }
              }
              adRequest = null
          },
          onCancel = { adRequest = null },
      )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun WeeklyAvatarCard(
    preset: AvatarPreset,
    unlocked: Boolean,
    onUnlock: () -> Unit,
    onUse: () -> Unit,
    onShare: () -> Unit,
) {
    val colors = EduAiTheme.colors
    Column(
        modifier =
            Modifier
                .width(150.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(colors.surface2)
                .border(1.dp, colors.border, RoundedCornerShape(16.dp))
                .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(colors.surface1),
            contentAlignment = Alignment.Center,
        ) {
            EduTutorAvatar(
                character = preset.config.character,
                state = AvatarState.Idle,
                modifier = Modifier.fillMaxSize().padding(6.dp),
                outfitVariant = preset.config.outfit,
                hairStyle = preset.config.hair,
                hairColor = preset.config.hairColor,
                glassesStyle = preset.config.glasses,
                glassesColor = preset.config.frameColor,
                neckStyle = preset.config.neck,
                underEyeLine = preset.config.eyeLine,
                cheekShading = preset.config.cheeks,
            )
            if (!unlocked) {
                Box(
                    modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.35f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        Icons.Outlined.Lock,
                        contentDescription = "Locked",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp),
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(preset.name, color = colors.text, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
        Text(
            preset.tagline,
            color = colors.textMuted,
            fontSize = 11.sp,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(10.dp))
        if (unlocked) {
            EduPrimaryButton(text = "Use", onClick = onUse, fillMaxWidth = true)
            Spacer(modifier = Modifier.height(6.dp))
            EduSecondaryButton(text = "Share", onClick = onShare, fillMaxWidth = true)
        } else {
            EduPrimaryButton(text = "Unlock · 2 ads", onClick = onUnlock, fillMaxWidth = true)
        }
    }
}

@Composable
private fun StudioLabel(text: String) {
    val colors = EduAiTheme.colors
    Text(
        text = text,
        color = colors.textMuted,
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(bottom = 8.dp),
    )
}

@Composable
private fun CycleChip(
    text: String,
    selected: Boolean = false,
    role: EduChipRole? = null,
    onClick: () -> Unit,
) {
    val colors = EduAiTheme.colors
    val active = selected || role == EduChipRole.Accent
    Text(
        text = text,
        color = if (active) colors.onAccent else colors.textSecondary,
        fontSize = 13.sp,
        fontWeight = FontWeight.SemiBold,
        modifier =
            Modifier
                .clip(RoundedCornerShape(999.dp))
                .background(if (active) colors.accent else colors.surface2)
                .pressScaleClickable(onClick = onClick, pressedScale = 0.94f)
                .padding(horizontal = 14.dp, vertical = 8.dp),
    )
}

private fun outfitLabel(v: Int) = when (v) {
    1 -> "Outfit: Shirt"; 2 -> "Outfit: Hoodie"; 3 -> "Outfit: V-neck"; else -> "Outfit: Tee"
}

private fun neckLabel(v: Int) = when (v) {
    1 -> "Neck: Slim"; 2 -> "Neck: Broad"; else -> "Neck: Regular"
}

private fun hairLabel(v: Int) = when (v) {
    1 -> "Hair: Side-part"; 2 -> "Hair: Curly"; else -> "Hair: Tousled"
}

private fun hairColorLabel(v: Int) = when (v) {
    1 -> "Color: Black"; 2 -> "Color: Auburn"; else -> "Color: Brown"
}

private fun glassesLabel(v: Int) = when (v) {
    1 -> "Specs: Round"; 2 -> "Specs: None"; else -> "Specs: Classic"
}

private fun frameLabel(v: Int) = when (v) {
    1 -> "Frame: Brown"; 2 -> "Frame: Navy"; else -> "Frame: Black"
}

private fun moodLabel(v: Int) = when (v) {
    1 -> "Mood: Happy"; 2 -> "Mood: Angry"; 3 -> "Mood: Thinking"; 4 -> "Mood: Surprised"; 5 -> "Mood: Sad"; else -> "Mood: Auto"
}

private fun gestureLabel(v: Int) = when (v) {
    1 -> "Gesture: Wave"; 2 -> "Gesture: Clap"; 3 -> "Gesture: Point"; 4 -> "Gesture: Palms"; 5 -> "Gesture: Think"; else -> "Gesture: Auto"
}
