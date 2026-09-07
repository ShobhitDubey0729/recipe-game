"""Recipe business logic."""
from __future__ import annotations

from uuid import UUID

from sqlalchemy.ext.asyncio import AsyncSession

from app.repositories.recipe_repository import RecipeRepository
from app.schemas.common import PaginatedResponse
from app.schemas.recipe import RecipeDetail, RecipeFilterParams, RecipeSummary


class RecipeService:
    def __init__(self, session: AsyncSession) -> None:
        self._repo = RecipeRepository(session)

    async def list_recipes(self, filters: RecipeFilterParams) -> PaginatedResponse[RecipeSummary]:
        recipes, total = await self._repo.list_recipes(filters)
        return PaginatedResponse[RecipeSummary](
            items=[RecipeSummary.model_validate(r) for r in recipes],
            page=filters.page,
            page_size=filters.page_size,
            total=total,
        )

    async def get_recipe(self, recipe_id: UUID) -> RecipeDetail | None:
        recipe = await self._repo.get_by_id(recipe_id)
        if recipe is None:
            return None
        return RecipeDetail.model_validate(recipe)

    async def search_recipes(self, filters: RecipeFilterParams) -> PaginatedResponse[RecipeSummary]:
        return await self.list_recipes(filters)
