package com.anurag.eduai.uikit.sample

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anurag.eduai.uikit.components.QuestTrailState
import com.anurag.eduai.uikit.components.RewardOverlay
import com.anurag.eduai.uikit.components.ThemeToggle
import com.anurag.eduai.uikit.navigation.EduBottomNavBadges
import com.anurag.eduai.uikit.navigation.EduBottomNavItem
import com.anurag.eduai.uikit.navigation.EduMainScaffold
import com.anurag.eduai.uikit.screens.EduHomeScreen
import com.anurag.eduai.uikit.screens.EduLeaguesScreen
import com.anurag.eduai.uikit.screens.EduOnboardingScreen
import com.anurag.eduai.uikit.screens.HomeUiState
import com.anurag.eduai.uikit.screens.LeagueUiState
import com.anurag.eduai.uikit.theme.EduAiTheme
import com.anurag.eduai.uikit.theme.EduThemeMode

@Composable
fun SampleApp() {
    var themeMode by rememberSaveable { mutableStateOf(EduThemeMode.Light) }
    var onboardingDone by rememberSaveable { mutableStateOf(false) }
    var currentTab by rememberSaveable { mutableStateOf(EduBottomNavItem.Home.route) }
    var showReward by rememberSaveable { mutableStateOf(false) }
    var bonusGems by rememberSaveable { mutableIntStateOf(0) }
    var todayDone by rememberSaveable { mutableStateOf(false) }
    var simsDone by rememberSaveable { mutableIntStateOf(2) }
    var simsClaimed by rememberSaveable { mutableStateOf(false) }
    var quizDone by rememberSaveable { mutableIntStateOf(0) }
    var quizClaimed by rememberSaveable { mutableStateOf(false) }
    var bonusClaimed by rememberSaveable { mutableStateOf(false) }

    val quests =
        QuestTrailState(
            simsDone = simsDone,
            simsTotal = 3,
            simsClaimed = simsClaimed,
            quizDone = quizDone,
            quizTotal = 1,
            quizClaimed = quizClaimed,
            bonusClaimed = bonusClaimed,
        )

    EduAiTheme(themeMode = themeMode) {
      if (!onboardingDone) {
        EduOnboardingScreen(onFinish = { onboardingDone = true })
      } else {
        Box {
        EduMainScaffold(
            currentRoute = currentTab,
            badges = EduBottomNavBadges(quests = true, leagues = true, profile = false),
            onItemSelected = { currentTab = it.route },
        ) {
            when (currentTab) {
                EduBottomNavItem.Home.route ->
                    EduHomeScreen(
                        state = HomeUiState(gems = 240 + bonusGems, todayDone = todayDone, quests = quests),
                        onProfileClick = { currentTab = EduBottomNavItem.Profile.route },
                        onLeagueClick = { currentTab = EduBottomNavItem.Leagues.route },
                        onQuestsSeeAll = { currentTab = EduBottomNavItem.Quests.route },
                        onStartToday = { showReward = true },
                        onSimsQuestClick = {
                            when {
                                simsDone >= 3 && !simsClaimed -> {
                                    simsClaimed = true
                                    bonusGems += 20
                                }
                                simsDone < 3 -> simsDone++
                            }
                        },
                        onQuizQuestClick = {
                            when {
                                quizDone >= 1 && !quizClaimed -> {
                                    quizClaimed = true
                                    bonusGems += 15
                                }
                                quizDone < 1 -> quizDone = 1
                            }
                        },
                        onBonusQuestClick = {
                            if (simsDone >= 3 && quizDone >= 1 && !bonusClaimed) {
                                bonusClaimed = true
                                bonusGems += 30
                            }
                        },
                    )
                EduBottomNavItem.Leagues.route ->
                    EduLeaguesScreen(
                        state = LeagueUiState(),
                        onParticipantClick = {},
                    )
                EduBottomNavItem.Profile.route ->
                    ProfilePlaceholder(
                        isDark = themeMode == EduThemeMode.Dark,
                        onToggleTheme = {
                            themeMode =
                                if (themeMode == EduThemeMode.Dark) EduThemeMode.Light else EduThemeMode.Dark
                        },
                    )
                else ->
                    PlaceholderScreen(
                        title =
                            when (currentTab) {
                                EduBottomNavItem.Plan.route -> "Your prep plan"
                                EduBottomNavItem.Quests.route -> "Quests"
                                else -> currentTab
                            },
                        subtitle = "Full screen comes next — Home matches the prototype layout.",
                    )
            }
        }

        RewardOverlay(
            visible = showReward,
            xpEarned = 35,
            gemsEarned = 30,
            xpFrom = 0.45f,
            xpTo = 0.72f,
            onCollect = {
                showReward = false
                bonusGems += 30
                todayDone = true
                currentTab = EduBottomNavItem.Home.route
            },
        )
        }
      }
    }
}

@Composable
private fun ProfilePlaceholder(
    isDark: Boolean,
    onToggleTheme: () -> Unit,
) {
    val colors = EduAiTheme.colors
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(colors.surface1)
                .padding(16.dp),
    ) {
        Text("Aanya", color = colors.text, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text("Dark theme", color = colors.text, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            ThemeToggle(isDark = isDark, onToggle = onToggleTheme)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Matches prototype Profile toggle — light is default.",
            color = colors.textMuted,
            fontSize = 12.sp,
        )
    }
}

@Composable
private fun PlaceholderScreen(
    title: String,
    subtitle: String,
) {
    val colors = EduAiTheme.colors
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(colors.surface1)
                .padding(16.dp),
    ) {
        Text(text = title, color = colors.text, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = subtitle, color = colors.textSecondary, fontSize = 13.sp)
    }
}
