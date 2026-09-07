# Rasoi Royale API

Base URL (development): `http://localhost:8000`

## Health

```http
GET /health
GET /ready
```

## Recipes

```http
GET /api/v1/recipes
GET /api/v1/recipes/{id}
GET /api/v1/recipes/search?q=masala
```

### Query parameters

| Parameter | Type | Description |
|-----------|------|-------------|
| `q` | string | Search name, description, tags, ingredients |
| `cuisine` | `north_indian` \| `south_indian` | Filter by cuisine |
| `meal_type` | `breakfast` \| `brunch` \| `lunch` \| `snacks` \| `dinner` | Filter by meal |
| `vegetarian` | boolean | Vegetarian only |
| `vegan` | boolean | Vegan only |
| `gluten_free` | boolean | Gluten-free only |
| `high_protein` | boolean | Protein ≥ 8g per 100g |
| `low_calorie` | boolean | Calories ≤ 200 per 100g |
| `max_prep_time_minutes` | int | Max prep time |
| `sort` | `popular` \| `newest` \| `preparation_time` \| `calories` \| `protein` | Sort order |
| `page` | int | Page number (default 1) |
| `page_size` | int | Items per page (default 20, max 100) |

### Example list response

```json
{
  "items": [
    {
      "id": "uuid",
      "name": "Masala Dosa",
      "slug": "masala-dosa",
      "cuisine": "south_indian",
      "meal_type": "breakfast",
      "prep_time_minutes": 15,
      "cook_time_minutes": 15,
      "calories_per_100g": 200.0,
      "protein_g_per_100g": 5.5,
      "vegetarian": true,
      "vegan": true,
      "gluten_free": true,
      "difficulty": "medium",
      "image_url": null
    }
  ],
  "page": 1,
  "page_size": 20,
  "total": 105
}
```

## Categories

```http
GET /api/v1/categories
GET /api/v1/cuisines
```

## Error format

```json
{
  "error": {
    "code": "RECIPE_NOT_FOUND",
    "message": "Recipe not found."
  }
}
```

## Seed data

```bash
cd backend
alembic upgrade head
python scripts/seed_recipes.py
```
