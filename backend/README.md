# Rasoi Royale Backend

FastAPI service for recipes, favorites, and multiplayer game sessions.

## Setup

```bash
python3 -m venv .venv
source .venv/bin/activate
pip install -e ".[dev]"
cp .env.example .env
```

## Database

Start PostgreSQL (from repo root):

```bash
docker compose up -d db
alembic upgrade head
python scripts/seed_recipes.py
```

## Run

```bash
uvicorn app.main:app --reload --host 0.0.0.0 --port 8000
```

## Test

```bash
pytest -v
```
