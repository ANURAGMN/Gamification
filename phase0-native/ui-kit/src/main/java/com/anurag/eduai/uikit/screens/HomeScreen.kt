package com.anurag.eduai.uikit.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.anurag.eduai.uikit.components.BookmarkItem
import com.anurag.eduai.uikit.components.BookmarksRail
import com.anurag.eduai.uikit.components.Entrance
import com.anurag.eduai.uikit.components.FriendUpdate
import com.anurag.eduai.uikit.components.FriendsUpdatesRail
import com.anurag.eduai.uikit.components.HeroDoneCard
import com.anurag.eduai.uikit.components.HeroFocusCard
import com.anurag.eduai.uikit.components.PlanDayNode
import com.anurag.eduai.uikit.components.PlanDayStatus
import com.anurag.eduai.uikit.components.PlanDayType
import com.anurag.eduai.uikit.components.PlanTrail
import com.anurag.eduai.uikit.components.QuestTrail
import com.anurag.eduai.uikit.components.QuestTrailState
import com.anurag.eduai.uikit.components.RevisionItem
import com.anurag.eduai.uikit.components.RevisionRail
import com.anurag.eduai.uikit.components.SubjectTile
import com.anurag.eduai.uikit.components.SubjectsRail
import com.anurag.eduai.uikit.components.TopBarChips
import com.anurag.eduai.uikit.theme.EduChipRole
import com.anurag.eduai.uikit.theme.EduAiTheme

data class HomeUiState(
    val greeting: String = "Good morning",
    val userName: String = "Aanya",
    val streak: Int = 6,
    val gems: Int = 240,
    val leagueName: String = "Silver",
    val leagueRank: Int = 4,
    val todayDone: Boolean = false,
    val quests: QuestTrailState = QuestTrailState(),
    val friends: List<FriendUpdate> =
        listOf(
            FriendUpdate("Rehan", "Reached a 30 day streak", 4, EduChipRole.Accent, seen = false),
            FriendUpdate("Meher", "Earned the Integer Master badge", 2, EduChipRole.Success, seen = false),
            FriendUpdate("You", "Promoted to the Silver League", 7, EduChipRole.Pro, seen = true),
        ),
    val bookmarks: List<BookmarkItem> =
        listOf(
            BookmarkItem("Division of fractions", "Revision", EduChipRole.Warning),
            BookmarkItem("Properties of integers", "Concept", EduChipRole.Accent),
            BookmarkItem("Multiplication of fractions", "Simulation", EduChipRole.Pro),
        ),
    val planDays: List<PlanDayNode> =
        listOf(
            PlanDayNode(1, PlanDayStatus.Done),
            PlanDayNode(2, PlanDayStatus.Done),
            PlanDayNode(3, PlanDayStatus.Done),
            PlanDayNode(4, PlanDayStatus.Today, PlanDayType.Lesson, "Multiplication & division of integers"),
            PlanDayNode(5, PlanDayStatus.Upcoming, PlanDayType.Lesson, "Multiplication of fractions"),
            PlanDayNode(6, PlanDayStatus.Upcoming, PlanDayType.Lesson, "Operations on decimals"),
            PlanDayNode(7, PlanDayStatus.Upcoming, PlanDayType.Revise),
            PlanDayNode(8, PlanDayStatus.Upcoming, PlanDayType.Revise),
            PlanDayNode(9, PlanDayStatus.Upcoming, PlanDayType.Mock),
        ),
    val revision: List<RevisionItem> =
        listOf(
            RevisionItem("Division of fractions", 45),
            RevisionItem("Integer word problems", 58),
        ),
    val subjects: List<SubjectTile> =
        listOf(
            SubjectTile("Integers"),
            SubjectTile("Fractions"),
        ),
)

/**
 * Pixel-faithful Compose port of prototype `screenHome()` rail order:
 * TopBar → Hero → Quests trail → Friends → Bookmarks → Plan trail → Revision → Subjects
 */
@Composable
fun EduHomeScreen(
    state: HomeUiState = HomeUiState(),
    onProfileClick: () -> Unit = {},
    onStreakClick: () -> Unit = {},
    onGemsClick: () -> Unit = {},
    onLeagueClick: () -> Unit = {},
    onStartToday: () -> Unit = {},
    onQuestsSeeAll: () -> Unit = {},
    onSimsQuestClick: () -> Unit = {},
    onQuizQuestClick: () -> Unit = {},
    onBonusQuestClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val colors = EduAiTheme.colors
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(colors.surface1)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 14.dp)
                .padding(bottom = 48.dp),
    ) {
        Entrance(delayMillis = 0) {
            TopBarChips(
                greeting = state.greeting,
                userName = state.userName,
                streak = state.streak,
                gems = state.gems,
                leagueName = state.leagueName,
                leagueRank = state.leagueRank,
                showFriendDot = true,
                showGemsDot = true,
                showLeagueDot = true,
                onProfileClick = onProfileClick,
                onStreakClick = onStreakClick,
                onGemsClick = onGemsClick,
                onLeagueClick = onLeagueClick,
            )
        }

        Entrance(delayMillis = 80) {
            if (state.todayDone) {
                HeroDoneCard(
                    eyebrow = "All done for today · +35 XP earned",
                    title = "Nice work on Integers!",
                    subtitle = "Come back tomorrow for Day 5, or get ahead now.",
                    buttonLabel = "Start Day 5 early",
                    onActionClick = onStartToday,
                )
            } else {
                HeroFocusCard(
                    eyebrow = "Today's focus · 18 min",
                    title = "Multiplication & division of integers",
                    subtitle = "Concept + simulation + quick quiz",
                    buttonLabel = "Start now",
                    onStartClick = onStartToday,
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Entrance(delayMillis = 160) {
            QuestTrail(
                state = state.quests,
                onSeeAll = onQuestsSeeAll,
                onSimsClick = onSimsQuestClick,
                onQuizClick = onQuizQuestClick,
                onBonusClick = onBonusQuestClick,
            )
        }

        Entrance(delayMillis = 240) {
            FriendsUpdatesRail(friends = state.friends)
        }

        Entrance(delayMillis = 320) {
            BookmarksRail(bookmarks = state.bookmarks)
        }

        Entrance(delayMillis = 400) {
            PlanTrail(days = state.planDays)
        }

        Spacer(modifier = Modifier.height(12.dp))

        Entrance(delayMillis = 480) {
            RevisionRail(items = state.revision)
        }

        Entrance(delayMillis = 560) {
            SubjectsRail(title = "Math", subjects = state.subjects)
        }
    }
}
