# Rasoi Royale

Production-ready Android app for Indian recipes and the **"What Should We Cook?"** multiplayer game.

## Stack

- **Android**: Kotlin, Jetpack Compose, Material 3, Room, Hilt, Retrofit
- **Backend**: FastAPI, PostgreSQL, SQLAlchemy, Alembic
- **CI**: GitHub Actions

## Repository structure

```text
rasoi-royale/
├── android/          # Kotlin / Compose app
├── backend/          # FastAPI API
├── data/             # Recipe seed data
├── docs/             # API & deployment docs
└── docker-compose.yml
```

## Prerequisites

- JDK 17+
- Android Studio (latest stable)
- Python 3.11+
- Docker & Docker Compose (for local PostgreSQL)
- Git & GitHub account (for CI/CD)
- Google Play Console account ($25 one-time fee for publishing)

## Local development

### Backend

```bash
cd backend
python3 -m venv .venv
source .venv/bin/activate
pip install -e ".[dev]"
cp .env.example .env
# Start PostgreSQL
docker compose -f ../docker-compose.yml up -d db
alembic upgrade head
uvicorn app.main:app --reload --host 0.0.0.0 --port 8000
```

Health check: http://localhost:8000/health

### Android

```bash
cd android
./gradlew assembleDebug
```

Debug API base URL (emulator): `http://10.0.2.2:8000/`

### Docker (full stack)

```bash
docker compose up --build
```

## Tests

```bash
# Backend
cd backend && pytest

# Android
cd android && ./gradlew test lint
```

## GitHub setup

This project is hosted at: `https://github.com/YOUR_USERNAME/recipe-game`

To push updates:

```bash
git add -A
git commit -m "Your message"
git push origin main
```

## Google Play release

See [docs/play-store.md](docs/play-store.md) for the full submission checklist.

Quick release build (after configuring signing):

```bash
cd android
./gradlew bundleRelease
# Output: android/app/build/outputs/bundle/release/app-release.aab
```

## License

MIT — see [LICENSE](LICENSE).
# recipe-game
