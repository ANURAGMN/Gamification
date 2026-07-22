package com.anurag.eduai.uikit.screens

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.key
import com.anurag.eduai.uikit.components.Entrance
import com.anurag.eduai.uikit.components.EduPrimaryButton
import com.anurag.eduai.uikit.components.pressScaleClickable
import com.anurag.eduai.uikit.theme.EduAiTheme
import com.anurag.eduai.uikit.theme.EduChipRole
import com.anurag.eduai.uikit.theme.forRole

private data class OnboardingSlide(
    val icon: ImageVector,
    val role: EduChipRole,
    val title: String,
    val body: String,
)

private data class OnboardingGoal(
    val label: String,
    val icon: ImageVector,
    val role: EduChipRole,
)

private val onboardingSlides =
    listOf(
        OnboardingSlide(
            icon = Icons.Filled.AutoAwesome,
            role = EduChipRole.Accent,
            title = "Learn a little,\nevery single day",
            body = "Short guided sessions — a concept, a hands-on simulation, and a quick quiz. About 18 minutes a day.",
        ),
        OnboardingSlide(
            icon = Icons.Filled.LocalFireDepartment,
            role = EduChipRole.Warning,
            title = "Build a streak\nworth protecting",
            body = "Show up daily to grow your flame. Miss a day? Streak freezes and repairs have your back.",
        ),
        OnboardingSlide(
            icon = Icons.Filled.EmojiEvents,
            role = EduChipRole.Pro,
            title = "Climb leagues\nwith your friends",
            body = "Earn XP, rise through weekly leagues, and cheer each other on. Ranked on effort, never on grades.",
        ),
    )

private val onboardingGoals =
    listOf(
        OnboardingGoal("Ace my next exam", Icons.Filled.Flag, EduChipRole.Accent),
        OnboardingGoal("Build a daily habit", Icons.Filled.LocalFireDepartment, EduChipRole.Warning),
        OnboardingGoal("Get ahead of class", Icons.Filled.Bolt, EduChipRole.Pro),
        OnboardingGoal("Just explore", Icons.Filled.AutoAwesome, EduChipRole.Success),
    )

/**
 * First-run onboarding — three intro slides followed by a goal picker. Mirrors
 * the prototype's `screenOnboarding` flow (content, order, and "Skip"). Calls
 * [onFinish] with the chosen goal (null if skipped) when the user is done.
 */
@Composable
fun EduOnboardingScreen(
    onFinish: (String?) -> Unit,
    modifier: Modifier = Modifier,
) {
    var step by rememberSaveable { mutableIntStateOf(0) }
    var selectedGoal by rememberSaveable { mutableStateOf<String?>(null) }
    val colors = EduAiTheme.colors

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(colors.surface1)
                .padding(horizontal = 22.dp)
                .padding(top = 20.dp, bottom = 26.dp),
    ) {
        if (step < onboardingSlides.size) {
            IntroSlide(
                slide = onboardingSlides[step],
                index = step,
                total = onboardingSlides.size,
                onSkip = { onFinish(null) },
                onNext = { if (step < onboardingSlides.size - 1) step++ else step = onboardingSlides.size },
            )
        } else {
            GoalPicker(
                selected = selectedGoal,
                onSelect = { selectedGoal = it },
                onContinue = { onFinish(selectedGoal) },
            )
        }
    }
}

@Composable
private fun ColumnScope.IntroSlide(
    slide: OnboardingSlide,
    index: Int,
    total: Int,
    onSkip: () -> Unit,
    onNext: () -> Unit,
) {
    val colors = EduAiTheme.colors
    val (fg, bg) = colors.forRole(slide.role)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
    ) {
        Text(
            text = "Skip",
            color = colors.textMuted,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            modifier =
                Modifier
                    .pressScaleClickable(onClick = onSkip, pressedScale = 0.9f)
                    .padding(6.dp),
        )
    }

    // Art + copy — re-keyed on index so it pops in fresh on every slide change.
    key(index) {
        Column(
            modifier = Modifier.fillMaxWidth().weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Entrance(delayMillis = 0) {
                Box(
                    modifier =
                        Modifier
                            .size(132.dp)
                            .clip(RoundedCornerShape(40.dp))
                            .background(bg),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(slide.icon, contentDescription = null, tint = fg, modifier = Modifier.size(58.dp))
                }
            }
            Spacer(modifier = Modifier.height(26.dp))
            Entrance(delayMillis = 80) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = slide.title,
                        color = colors.text,
                        fontSize = 27.sp,
                        lineHeight = 31.sp,
                        fontWeight = FontWeight.Black,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = slide.body,
                        color = colors.textSecondary,
                        fontSize = 14.5.sp,
                        lineHeight = 22.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.width(300.dp),
                    )
                }
            }
        }
    }

    // Progress dots — the active one stretches into a pill.
    Row(
        modifier = Modifier.fillMaxWidth().padding(bottom = 18.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        repeat(total) { i ->
            val active = i == index
            val dotWidth by animateDpAsState(
                targetValue = if (active) 22.dp else 8.dp,
                animationSpec = tween(250),
                label = "dot$i",
            )
            Box(
                modifier =
                    Modifier
                        .padding(horizontal = 3.5.dp)
                        .height(8.dp)
                        .width(dotWidth)
                        .clip(RoundedCornerShape(4.dp))
                        .background(if (active) colors.accent else colors.borderStrong),
            )
        }
    }

    EduPrimaryButton(
        text = if (index < total - 1) "Next" else "Get started",
        onClick = onNext,
        fillMaxWidth = true,
    )
}

@Composable
private fun ColumnScope.GoalPicker(
    selected: String?,
    onSelect: (String) -> Unit,
    onContinue: () -> Unit,
) {
    val colors = EduAiTheme.colors

    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = "What brings you here?",
        color = colors.text,
        fontSize = 25.sp,
        fontWeight = FontWeight.Black,
    )
    Spacer(modifier = Modifier.height(6.dp))
    Text(
        text = "We'll tune your plan and quests to match.",
        color = colors.textSecondary,
        fontSize = 14.sp,
    )
    Spacer(modifier = Modifier.height(22.dp))

    Column(
        modifier = Modifier.fillMaxWidth().weight(1f),
        verticalArrangement = Arrangement.spacedBy(11.dp),
    ) {
        onboardingGoals.forEachIndexed { i, goal ->
            Entrance(delayMillis = i * 60) {
                GoalCard(
                    goal = goal,
                    selected = selected == goal.label,
                    onClick = { onSelect(goal.label) },
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(14.dp))
    EduPrimaryButton(
        text = "Continue",
        onClick = onContinue,
        fillMaxWidth = true,
        enabled = selected != null,
    )
}

@Composable
private fun GoalCard(
    goal: OnboardingGoal,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val colors = EduAiTheme.colors
    val (fg, bg) = colors.forRole(goal.role)
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(if (selected) bg else colors.surface2)
                .border(
                    width = if (selected) 2.dp else 1.dp,
                    color = if (selected) fg else colors.border,
                    shape = RoundedCornerShape(14.dp),
                )
                .pressScaleClickable(onClick = onClick, pressedScale = 0.97f)
                .padding(horizontal = 14.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier.size(38.dp).clip(RoundedCornerShape(11.dp)).background(bg),
            contentAlignment = Alignment.Center,
        ) {
            Icon(goal.icon, contentDescription = null, tint = fg, modifier = Modifier.size(19.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = goal.label,
            color = colors.text,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(1f),
        )
        if (selected) {
            Box(
                modifier = Modifier.size(22.dp).clip(CircleShape).background(fg),
                contentAlignment = Alignment.Center,
            ) {
                Icon(Icons.Outlined.Check, contentDescription = null, tint = colors.onAccent, modifier = Modifier.size(14.dp))
            }
        }
    }
}
