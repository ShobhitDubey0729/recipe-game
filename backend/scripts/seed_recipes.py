"""Load recipe catalog into PostgreSQL."""

import asyncio
import sys

from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession, async_sessionmaker, create_async_engine

from app.core.config import settings
from app.models.recipe import Recipe
from app.seed.catalog import get_seed_recipes


async def seed(session: AsyncSession) -> int:
    recipes = get_seed_recipes()
    inserted = 0

    for data in recipes:
        existing = await session.execute(select(Recipe).where(Recipe.slug == data["slug"]))
        if existing.scalar_one_or_none():
            continue
        session.add(Recipe(**data))
        inserted += 1

    await session.commit()
    return inserted


async def main() -> None:
    engine = create_async_engine(settings.database_url, echo=False)
    session_factory = async_sessionmaker(engine, class_=AsyncSession, expire_on_commit=False)

    async with session_factory() as session:
        count = await seed(session)
        print(f"Seeded {count} new recipes ({len(get_seed_recipes())} total in catalog).")

    await engine.dispose()


if __name__ == "__main__":
    try:
        asyncio.run(main())
    except Exception as exc:
        print(f"Seed failed: {exc}", file=sys.stderr)
        sys.exit(1)
