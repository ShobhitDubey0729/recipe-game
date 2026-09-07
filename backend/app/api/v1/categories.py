"""Category and cuisine metadata endpoints."""

from fastapi import APIRouter

from app.models.enums import Cuisine, MealType

router = APIRouter()


@router.get("/categories")
async def list_categories() -> dict:
    return {
        "items": [
            {"id": meal.value, "name": meal.value.replace("_", " ").title()}
            for meal in MealType
        ]
    }


@router.get("/cuisines")
async def list_cuisines() -> dict:
    return {
        "items": [
            {"id": cuisine.value, "name": cuisine.value.replace("_", " ").title()}
            for cuisine in Cuisine
        ]
    }
