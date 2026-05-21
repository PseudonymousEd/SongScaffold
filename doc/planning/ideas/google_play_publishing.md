# Google Play Publishing Steps

## One-time account setup
- Register a Google Play Developer account at play.google.com/console — one-time $25 USD fee
- Accept the Developer Distribution Agreement

## Legal and policy documents
- **Privacy policy**: Copy and adapt from PlayStreak. Must be hosted at a publicly accessible URL (e.g. GitHub Pages, a simple webpage). Required by Google Play.
- **Terms of service**: Optional but advisable. Can copy from PlayStreak.
- Neither needs updating for this app as it collects no user data and has no network access.

## App signing
- Generate a release keystore (if you don't already have one from PlayStreak):
  ```
  keytool -genkey -v -keystore songscaffold-release.keystore -alias songscaffold -keyalg RSA -keysize 2048 -validity 10000
  ```
- Store the keystore file and passwords somewhere safe — losing it means you can never update the app
- Configure signing in `app/build.gradle.kts` under `signingConfigs` (credentials via local.properties, not committed to git)
- Consider enrolling in Play App Signing (Google manages the final signing key; you upload with an upload key)

## Build the release AAB
```
./gradlew bundleRelease
```
Output: `app/build/outputs/bundle/release/app-release.aab`

## Google Play Console — create the app
1. Play Console → "Create app"
2. Set app name ("SongScaffold"), default language, category (Music & Audio), free/paid (free)
3. Confirm it is not primarily directed at children

## Store listing
- Short description (80 chars max)
- Full description (4000 chars max)
- App icon: already exists (`ic_launcher.webp`, xxxhdpi = 192×192px) — Play requires 512×512 PNG
- Feature graphic: 1024×500 PNG (Play Store banner, required)
- Screenshots: at least 2 phone screenshots (Play Console will prompt for the right sizes)
- No video required

## Content rating
- Complete the IARC questionnaire in Play Console (takes ~5 minutes)
- SongScaffold should rate "Everyone" — no violence, no user interaction, no location

## Data safety section
- SongScaffold collects no data and has no network access
- Answer "No" to all data collection questions
- This section must still be completed; it will show "No data shared / No data collected" on the listing

## Pricing and distribution
- Set to Free
- Select target countries (can start with all countries)

## App content declarations
- Confirm no ads, no in-app purchases (unless added later)
- Target audience: 13+ is fine for this app

## Upload and submit
1. Create a new release in the "Production" track (or start with "Internal testing" to verify the build first)
2. Upload the `.aab`
3. Add release notes (what's new)
4. Review the release summary and submit for review

## After submission
- Initial review typically takes 1–3 days
- Once approved, the app goes live on the Play Store
- Future updates: increment `versionCode` and `versionName` in `app/build.gradle.kts`, build a new AAB, upload to a new release

## Assets still needed
- 512×512 PNG app icon (export from existing source)
- 1024×500 feature graphic
- At least 2 phone screenshots
- Hosted URL for the privacy policy
