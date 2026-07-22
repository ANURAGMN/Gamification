# Phase 0 — Foundation (Native UI Kit)

Standalone Compose module in `phase0-native/`. Merge into `EduAI_app-main` after components are validated.

**Sources of truth:** `EduAI_Product_Spec_latetst.docx`, `EduAI_Master_Prototype_latest.html`

---

## Goal

Design system + navigation shell + event model — **no business logic**, no backend. Reusable building blocks only.

---

## Task list

### A. Project setup

| ID | Task | Status | Output |
|----|------|--------|--------|
| P0-A1 | Gradle multi-module: `:ui-kit` library + `:sample` app | ⬜ | `phase0-native/` builds |
| P0-A2 | Package namespace `com.anurag.eduai.uikit` | ⬜ | Consistent merge path |
| P0-A3 | Component catalog sample app | ⬜ | Preview all elements on device |

### B. Design tokens (spec §3.6)

| ID | Task | Status | Output |
|----|------|--------|--------|
| P0-B1 | `EduAiColorScheme` — accent, success, warning, danger, pro + backgrounds | ✅ | `theme/EduAiColors.kt` |
| P0-B2 | Light + dark palettes from prototype CSS variables | ✅ | `theme/EduAiColorScheme.kt` |
| P0-B3 | Spacing / radius / elevation tokens | ✅ | `theme/EduAiDimens.kt` |
| P0-B4 | Typography scale (section title, body, chip, caption) | ✅ | `theme/EduAiTypography.kt` |
| P0-B5 | `EduAiTheme` — system dark + manual override hook | ✅ | `theme/EduAiTheme.kt` |
| P0-B6 | Theme toggle composable (Profile preview) | ✅ | `components/ThemeToggle.kt` |

### C. Atomic components

| ID | Task | Status | Output |
|----|------|--------|--------|
| P0-C1 | `EduPrimaryButton` / `EduSecondaryButton` / `EduGhostButton` | ✅ | `components/EduButtons.kt` |
| P0-C2 | `EduCard` (flat surface) | ✅ | `components/EduCard.kt` |
| P0-C3 | `EduChip` + role variants (accent, success, warning, pro) | ✅ | `components/EduChip.kt` |
| P0-C4 | `EduProgressBar` | ✅ | `components/EduProgressBar.kt` |
| P0-C5 | `NotificationDot` | ✅ | `components/NotificationDot.kt` |
| P0-C6 | `SectionHeader` + optional "See all" action | ✅ | `components/SectionHeader.kt` |

### D. Composite components (home building blocks)

| ID | Task | Status | Output |
|----|------|--------|--------|
| P0-D1 | `HeroFocusCard` — gradient hero, today's focus pattern | ✅ | `components/HeroFocusCard.kt` |
| P0-D2 | `HeroDoneCard` — all-done-for-today state | ✅ | `components/HeroFocusCard.kt` |
| P0-D3 | `HorizontalRail` — scroll container for cards | ✅ | `components/HorizontalRail.kt` |
| P0-D4 | `RailCard` — min-width card for rails | ✅ | `components/RailCard.kt` |
| P0-D5 | `TopBarChips` — avatar, streak, gems, league row | ✅ | `components/TopBarChips.kt` |
| P0-D6 | `QuestTrail` / `PlanTrail` — wavy SVG trails | ✅ | `components/QuestTrail.kt`, `components/PlanTrail.kt` |

### E. Navigation shell (spec §3.2)

| ID | Task | Status | Output |
|----|------|--------|--------|
| P0-E1 | `EduBottomNavItem` enum — Home, Plan, Quests, Leagues, Profile | ✅ | `navigation/EduBottomNavItem.kt` |
| P0-E2 | `EduBottomNavBar` — 5 tabs + notification dots | ✅ | `navigation/EduBottomNavBar.kt` |
| P0-E3 | `EduMainScaffold` — bottom nav + content slot | ✅ | `navigation/EduMainScaffold.kt` |
| P0-E4 | Placeholder tab screens (empty state copy) | ✅ | `sample/.../PlaceholderScreens.kt` |

### F. Event model (spec §12)

| ID | Task | Status | Output |
|----|------|--------|--------|
| P0-F1 | `GamificationEvent` sealed types | ✅ | `events/GamificationEvent.kt` |
| P0-F2 | `XpScope` (weekly vs lifetime) | ✅ | `events/XpScope.kt` |
| P0-F3 | `BookmarkType` enum | ✅ | `events/BookmarkType.kt` |
| P0-F4 | `GamificationEventBus` interface (in-memory impl for sample) | ✅ | `events/GamificationEventBus.kt` |

### H. Full screens (unplanned, built ahead of schedule)

| ID | Task | Status | Output |
|----|------|--------|--------|
| P0-H1 | `EduHomeScreen` — full Home tab wired to real rails | ✅ | `screens/HomeScreen.kt` |
| P0-H2 | `EduLeaguesScreen` — leaderboard with promotion/safe/demotion zones | ✅ | `screens/LeaguesScreen.kt`, `components/LeagueLeaderboard.kt` |
| P0-H3 | Plan / Quests full screens (still placeholders in sample) | ⬜ | Phase 1 |
| P0-H4 | Home screen motion polish — press-scale taps, animated progress fills, streak/claim/notification pulses, staggered section entrance | ✅ | `components/Motion.kt` + touch-ups across `TopBarChips`, `QuestTrail`, `PlanTrail`, `NotificationDot`, `EduProgressBar`, `RailCard`, `HomeRails`, `HeroFocusCard`, `HomeScreen` |
| P0-H5 | "New level" interactivity — haptics + sound, full-screen reward moment, Lottie pipeline w/ graceful fallback | ✅ | `components/Feedback.kt`, `components/RewardOverlay.kt`, `components/EduLottie.kt`, `res/raw/eduai_*.json`, `ConfettiRain` in `Motion.kt`; needs `libs.lottie.compose` + VIBRATE permission |
| P0-H6 | Onboarding flow — 3 intro slides + goal picker, shown once before Home (ports prototype `screenOnboarding`) | ✅ | `screens/OnboardingScreen.kt`; gated in `SampleApp.kt` via `onboardingDone` |

### G. Merge prep (later)

| ID | Task | Status | Notes |
|----|------|--------|-------|
| P0-G1 | Copy `:ui-kit` into main app as module | ⬜ | After Phase 0 sign-off |
| P0-G2 | Replace `Colors.kt` / `Theme.kt` incrementally | ⬜ | Keep old theme until screens migrated |
| P0-G3 | Wire real ViewModels to event bus | ⬜ | Phase 1+ |

---

## Build & run sample

```powershell
cd C:\Users\anurag.mn\Desktop\Gamification\phase0-native
.\gradlew.bat :sample:assembleDebug
adb install -r sample\build\outputs\apk\debug\sample-debug.apk
```

---

## Phase 0 done when

- [ ] Sample app runs on device showing component catalog + 5-tab shell
- [ ] Light and dark theme both look correct
- [ ] All P0-B, P0-C, P0-E, P0-F tasks checked
- [ ] Team agrees tokens match prototype on real phone

**Next:** Phase 1 — wire `HeroFocusCard`, rails, and scaffold into real Home data.
