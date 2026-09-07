# Rasoi Royale API

Base URL (development): `http://localhost:8000`

## Health

```http
GET /health
```

Response:

```json
{"status": "ok"}
```

## Recipes (Phase 2+)

```http
GET /api/v1/recipes?page=1&page_size=20
GET /api/v1/recipes/{id}
GET /api/v1/recipes/search?q=masala
```

Query parameters:

- `cuisine` — `north_indian` | `south_indian`
- `meal_type` — `breakfast` | `brunch` | `lunch` | `snacks` | `dinner`
- `vegetarian`, `vegan`, `gluten_free` — boolean
- `high_protein`, `low_calorie` — boolean filters

## Games (Phase 5+)

```http
POST /api/v1/games
POST /api/v1/games/{game_id}/join
POST /api/v1/games/{game_id}/start
POST /api/v1/games/{game_id}/vote
GET /api/v1/games/{game_id}/result
```

WebSocket: `/ws/games/{game_id}`

## Error format

```json
{
  "error": {
    "code": "GAME_EXPIRED",
    "message": "This game has expired."
  }
}
```
