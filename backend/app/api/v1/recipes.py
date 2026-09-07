"""Recipe API endpoints."""

from typing import Optional
from uuid import UUID

from fastapi import APIRouter, Depends, HTTPException, Query
from sqlalchemy.ext.asyncio import AsyncSession

from app.core.database import get_db
from app.models.enums import Cuisine, MealType, RecipeSort
from app.schemas.common import PaginatedResponse
from app.schemas.recipe import RecipeDetail, RecipeFilterParams, RecipeSummary
from app.services.recipe_service import RecipeService

router = APIRouter()


def get_recipe_service(db: AsyncSession = Depends(get_db)) -> RecipeService:
    return RecipeService(db)


def _build_filters(
    q: Optional[str] = None,
    cuisine: Optional[Cuisine] = None,
    meal_type: Optional[MealType] = None,
    vegetarian: Optional[bool] = None,
    vegan: Optional[bool] = None,
    gluten_free: Optional[bool] = None,
    high_protein: Optional[bool] = None,
    low_calorie: Optional[bool] = None,
    max_prep_time_minutes: Optional[int] = Query(default=None, ge=0),
    sort: RecipeSort = RecipeSort.POPULAR,
    page: int = Query(default=1, ge=1),
    page_size: int = Query(default=20, ge=1, le=100),
) -> RecipeFilterParams:
    return RecipeFilterParams(
        q=q,
        cuisine=cuisine,
        meal_type=meal_type,
        vegetarian=vegetarian,
        vegan=vegan,
        gluten_free=gluten_free,
        high_protein=high_protein,
        low_calorie=low_calorie,
        max_prep_time_minutes=max_prep_time_minutes,
        sort=sort,
        page=page,
        page_size=page_size,
    )


@router.get("", response_model=PaginatedResponse[RecipeSummary])
async def list_recipes(
    filters: RecipeFilterParams = Depends(_build_filters),
    service: RecipeService = Depends(get_recipe_service),
) -> PaginatedResponse[RecipeSummary]:
    return await service.list_recipes(filters)


@router.get("/search", response_model=PaginatedResponse[RecipeSummary])
async def search_recipes(
    q: str = Query(min_length=1),
    filters: RecipeFilterParams = Depends(_build_filters),
    service: RecipeService = Depends(get_recipe_service),
) -> PaginatedResponse[RecipeSummary]:
    filters.q = q
    return await service.search_recipes(filters)


@router.get("/{recipe_id}", response_model=RecipeDetail)
async def get_recipe(
    recipe_id: UUID,
    service: RecipeService = Depends(get_recipe_service),
) -> RecipeDetail:
    recipe = await service.get_recipe(recipe_id)
    if recipe is None:
        raise HTTPException(
            status_code=404,
            detail={"error": {"code": "RECIPE_NOT_FOUND", "message": "Recipe not found."}},
        )
    return recipe
