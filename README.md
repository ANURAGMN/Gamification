# EduAI Gamification (Capacitor-style WebView demo)

This folder wraps the latest click-through prototype in an Android WebView so you can install and demo the full gamification UI on a phone.

**Source of truth (latest):**
- Product spec: `EduAI_Product_Spec_latetst.docx`
- Prototype: `EduAI_Master_Prototype_latest.html`

## What is included

- `www/index.html` — mobile-adapted prototype (full screen, safe-area aware)
- `capacitor.config.json` + `package.json` — Capacitor config for later SDUI work
- `android/` — WebView shell app (HTML prototype demo)
- `phase0-native/` — **Native Compose UI kit (Phase 0)** — design tokens, components, 5-tab shell
- `PHASE0_TASKS.md` — Phase 0 task tracker

## Build the APK now (no Node required)

```powershell
cd C:\Users\anurag.mn\Desktop\Gamification\android
.\gradlew.bat assembleDebug
```

APK output:

`android\app\build\outputs\apk\debug\app-debug.apk`

Install on a connected device:

```powershell
adb install -r app\build\outputs\apk\debug\app-debug.apk
```

## Update the prototype

1. Edit `EduAI_Master_Prototype_latest.html`
2. Run:

```powershell
python scripts\prepare_www.py
```

3. Rebuild the APK

## Capacitor workflow (optional, after Node.js is installed)

Node was not available on this machine during setup. Once Node LTS is installed:

```powershell
cd C:\Users\anurag.mn\Desktop\Gamification
npm install
npm run sync:android
npm run open:android
```

`npm run sync:android` copies `www/` into the Android assets folder and keeps Capacitor in sync.

## Notes

- This demo loads the HTML fully offline from app assets.
- Internet permission is included for future Capacitor plugins / SDUI APIs.
- The original browser mockup frame (380px phone bezel) is removed for real devices.
