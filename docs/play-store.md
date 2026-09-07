# Google Play Store Release Checklist

## 1. Prerequisites

- [ ] Google Play Console developer account ($25 one-time registration)
- [ ] Unique production package ID (replace `com.example.rasoifood`)
- [ ] Production API deployed with HTTPS
- [ ] Privacy policy hosted at a public URL
- [ ] Support email address

## 2. App signing

1. Generate upload keystore (keep offline, never commit):

```bash
keytool -genkey -v \
  -keystore android/release.keystore \
  -alias rasoi-royale \
  -keyalg RSA -keysize 2048 -validity 10000
```

2. Copy `android/keystore.properties.example` to `android/keystore.properties` and fill values.
3. Enable Play App Signing in Play Console (recommended).

## 3. Release build

```bash
cd android
./gradlew bundleRelease
```

Output: `android/app/build/outputs/bundle/release/app-release.aab`

## 4. Store listing assets

| Asset | Size | Notes |
|-------|------|-------|
| App icon | 512×512 PNG | No transparency |
| Feature graphic | 1024×500 PNG | |
| Phone screenshots | min 2 | Home, recipes, game flows |
| Short description | ≤ 80 chars | |
| Full description | ≤ 4000 chars | |

Suggested short description:

> Browse Indian recipes, nutrition info, and play "What Should We Cook?" with friends.

## 5. Play Console forms

- [ ] **App content** → Privacy policy URL
- [ ] **Data safety** — declare network, display name for games, no unnecessary PII
- [ ] **Content rating** questionnaire
- [ ] **Target audience** — not directed at children unless intended
- [ ] **Ads** — declare if no ads in MVP
- [ ] **Target API level** — meet current Google Play requirements (check Play Console)

## 6. Testing tracks

1. Upload AAB to **Internal testing**
2. Add testers via email list
3. Fix issues from pre-launch report
4. Promote to **Closed testing** → **Open testing** → **Production**

## 7. Production verification

- [ ] Release API URL configured in `BuildConfig.RELEASE_BASE_URL`
- [ ] Cleartext traffic disabled in production (`usesCleartextTraffic=false`)
- [ ] ProGuard/R8 enabled for release
- [ ] Offline recipe cache works
- [ ] Game requires network with clear error messaging
- [ ] Nutrition labeled as estimates

## 8. Post-launch

- Monitor crash reports (Play Console / Firebase Crashlytics if added)
- Respond to user reviews
- Plan content updates via admin API (Phase 7)
