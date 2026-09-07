"""Recipe data access."""
from __future__ import annotations

from uuid import UUID

from sqlalchemy import String, cast, func, or_, select
from sqlalchemy.ext.asyncio import AsyncSession

from app.models.enums import RecipeSort, RecipeStatus
from app.models.recipe import Recipe
from app.schemas.recipe import RecipeFilterParams

HIGH_PROTEIN_THRESHOLD = 8.0
LOW_CALORIE_THRESHOLD = 200.0


class RecipeRepository:
    def __init__(self, session: AsyncSession) -> None:
        self._session = session

    def _base_query(self):
        return select(Recipe).where(Recipe.status == RecipeStatus.PUBLISHED.value)

    def _apply_filters(self, query, filters: RecipeFilterParams):
        if filters.q:
            term = f"%{filters.q.lower()}%"
            tags_text = cast(Recipe.tags, String)
            ingredients_text = cast(Recipe.ingredients, String)
            query = query.where(
                or_(
                    func.lower(Recipe.name).like(term),
                    func.lower(Recipe.description).like(term),
                    func.lower(tags_text).like(term),
                    func.lower(ingredients_text).like(term),
                )
            )

        if filters.cuisine:
            query = query.where(Recipe.cuisine == filters.cuisine.value)
        if filters.meal_type:
            query = query.where(Recipe.meal_type == filters.meal_type.value)
        if filters.vegetarian is not None:
            query = query.where(Recipe.vegetarian == filters.vegetarian)
        if filters.vegan is not None:
            query = query.where(Recipe.vegan == filters.vegan)
        if filters.gluten_free is not None:
            query = query.where(Recipe.gluten_free == filters.gluten_free)
        if filters.high_protein:
            query = query.where(Recipe.protein_g_per_100g >= HIGH_PROTEIN_THRESHOLD)
        if filters.low_calorie:
            query = query.where(Recipe.calories_per_100g <= LOW_CALORIE_THRESHOLD)
        if filters.max_prep_time_minutes is not None:
            query = query.where(Recipe.prep_time_minutes <= filters.max_prep_time_minutes)

        return query

    def _apply_sort(self, query, sort: RecipeSort):
        if sort == RecipeSort.NEWEST:
            return query.order_by(Recipe.created_at.desc())
        if sort == RecipeSort.PREPARATION_TIME:
            return query.order_by(Recipe.prep_time_minutes.asc())
        if sort == RecipeSort.CALORIES:
            return query.order_by(Recipe.calories_per_100g.asc())
        if sort == RecipeSort.PROTEIN:
            return query.order_by(Recipe.protein_g_per_100g.desc())
        return query.order_by(Recipe.created_at.desc(), Recipe.name.asc())

    async def list_recipes(self, filters: RecipeFilterParams) -> tuple[list[Recipe], int]:
        base = self._apply_filters(self._base_query(), filters)
        count_query = select(func.count()).select_from(base.subquery())
        total = (await self._session.execute(count_query)).scalar_one()

        offset = (filters.page - 1) * filters.page_size
        query = self._apply_sort(base, filters.sort).offset(offset).limit(filters.page_size)
        result = await self._session.execute(query)
        return list(result.scalars().all()), total

    async def get_by_id(self, recipe_id: UUID) -> Recipe | None:
        query = self._base_query().where(Recipe.id == recipe_id)
        result = await self._session.execute(query)
        return result.scalar_one_or_none()

    async def get_by_slug(self, slug: str) -> Recipe | None:
        query = self._base_query().where(Recipe.slug == slug)
        result = await self._session.execute(query)
        return result.scalar_one_or_none()
