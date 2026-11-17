# Stickman: Road to 100 – Android app

Wraps the HTML game in a full-screen, landscape WebView app with its own launcher icon.

## Option A – Android Studio
1. (Optional, for offline play) run `fetch_libs.bat` (Windows) or `./fetch_libs.sh` (Mac/Linux).
2. Open this folder in Android Studio -> let Gradle sync.
3. Build > Build APK(s), or press Run with a phone connected.

## Option B – No install, build online (GitHub)
1. Create a free GitHub repo and upload this whole folder (keep `.github/workflows/build.yml`).
2. Open the **Actions** tab -> "Build Android APK" -> wait ~3 min.
3. Download the `Stickman-APK` artifact, unzip, copy `app-debug.apk` to your phone and install.

## Play Store
Create a keystore, set env vars KEYSTORE_FILE, KEYSTORE_PASSWORD, KEY_ALIAS, KEY_PASSWORD,
then run `gradle bundleRelease` and upload `app/build/outputs/bundle/release/app-release.aab`.
Change `applicationId` in `app/build.gradle` to your own unique ID first.
Play Store also needs a 512x512 icon (included as `play_store_icon_512.png`).

## New: Kicks
Buy **Power Training** in the Wandering Trader's store -> the Kick Wheel spins and awards a kick (duplicates level it up).
Tap the on-screen **Kick** button to kick; **hold** it to open the kick selector (works mid-fight).

## Animations
Spring-based squash & stretch, run lean/gait, hit flinches, enemy wind-ups, boss slam pose, spinning deaths, weapon draw flourish / inertia / recoil / impact pulse, animated damage numbers and UI transitions.
