import pytest
from httpx import ASGITransport, AsyncClient

from app.main import app
from app.seed.catalog import get_seed_recipes


@pytest.mark.asyncio
async def test_health_endpoint():
    transport = ASGITransport(app=app)
    async with AsyncClient(transport=transport, base_url="http://test") as client:
        response = await client.get("/health")
    assert response.status_code == 200
    assert response.json() == {"status": "ok"}


@pytest.mark.asyncio
async def test_list_recipes_returns_paginated(client):
    response = await client.get("/api/v1/recipes")
    assert response.status_code == 200
    body = response.json()
    assert body["total"] == len(get_seed_recipes())
    assert len(body["items"]) == 20
    assert "name" in body["items"][0]


@pytest.mark.asyncio
async def test_filter_by_cuisine(client):
    response = await client.get("/api/v1/recipes?cuisine=south_indian")
    assert response.status_code == 200
    body = response.json()
    assert body["total"] >= 1
    assert all(item["cuisine"] == "south_indian" for item in body["items"])


@pytest.mark.asyncio
async def test_filter_by_meal_type(client):
    response = await client.get("/api/v1/recipes?meal_type=breakfast")
    assert response.status_code == 200
    body = response.json()
    assert body["total"] >= 1
    assert all(item["meal_type"] == "breakfast" for item in body["items"])


@pytest.mark.asyncio
async def test_search_recipes(client):
    response = await client.get("/api/v1/recipes/search?q=dosa")
    assert response.status_code == 200
    body = response.json()
    assert body["total"] >= 1


@pytest.mark.asyncio
async def test_get_recipe_by_id(client):
    list_response = await client.get("/api/v1/recipes")
    recipe_id = list_response.json()["items"][0]["id"]
    response = await client.get(f"/api/v1/recipes/{recipe_id}")
    assert response.status_code == 200
    body = response.json()
    assert body["id"] == recipe_id
    assert "ingredients" in body
    assert "instructions" in body


@pytest.mark.asyncio
async def test_get_recipe_not_found(client):
    response = await client.get("/api/v1/recipes/00000000-0000-0000-0000-000000000099")
    assert response.status_code == 404
    body = response.json()
    assert body["error"]["code"] == "RECIPE_NOT_FOUND"


@pytest.mark.asyncio
async def test_pagination(client):
    response = await client.get("/api/v1/recipes?page=1&page_size=3")
    assert response.status_code == 200
    body = response.json()
    assert body["page"] == 1
    assert body["page_size"] == 3
    assert len(body["items"]) == 3
    assert body["total"] == len(get_seed_recipes())


@pytest.mark.asyncio
async def test_categories_endpoint(client):
    response = await client.get("/api/v1/categories")
    assert response.status_code == 200
    assert len(response.json()["items"]) == 5


@pytest.mark.asyncio
async def test_cuisines_endpoint(client):
    response = await client.get("/api/v1/cuisines")
    assert response.status_code == 200
    assert len(response.json()["items"]) == 2
