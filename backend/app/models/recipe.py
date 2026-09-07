"""Recipe database model."""
from __future__ import annotations

import uuid
from datetime import datetime, timezone
from typing import Optional

from sqlalchemy import JSON, Boolean, DateTime, Float, Index, Integer, String, Text
from sqlalchemy.dialects.postgresql import UUID
from sqlalchemy.orm import Mapped, mapped_column

from app.core.database import Base
from app.models.enums import Cuisine, Difficulty, MealType, RecipeStatus


def utcnow() -> datetime:
    return datetime.now(timezone.utc)


class Recipe(Base):
    __tablename__ = "recipes"

    id: Mapped[uuid.UUID] = mapped_column(UUID(as_uuid=True), primary_key=True, default=uuid.uuid4)
    name: Mapped[str] = mapped_column(String(255), nullable=False)
    slug: Mapped[str] = mapped_column(String(255), unique=True, nullable=False, index=True)
    description: Mapped[str] = mapped_column(Text, nullable=False, default="")
    cuisine: Mapped[str] = mapped_column(String(32), nullable=False, index=True)
    meal_type: Mapped[str] = mapped_column(String(32), nullable=False, index=True)
    prep_time_minutes: Mapped[int] = mapped_column(Integer, nullable=False, default=0)
    cook_time_minutes: Mapped[int] = mapped_column(Integer, nullable=False, default=0)
    servings: Mapped[int] = mapped_column(Integer, nullable=False, default=2)
    final_cooked_weight_g: Mapped[Optional[float]] = mapped_column(Float, nullable=True)
    calories_per_100g: Mapped[float] = mapped_column(Float, nullable=False, index=True)
    protein_g_per_100g: Mapped[float] = mapped_column(Float, nullable=False, index=True)
    vegetarian: Mapped[bool] = mapped_column(Boolean, nullable=False, default=True, index=True)
    vegan: Mapped[bool] = mapped_column(Boolean, nullable=False, default=False)
    gluten_free: Mapped[bool] = mapped_column(Boolean, nullable=False, default=False)
    difficulty: Mapped[str] = mapped_column(String(16), nullable=False, default=Difficulty.MEDIUM.value)
    image_url: Mapped[Optional[str]] = mapped_column(Text, nullable=True)
    nutrition_source: Mapped[str] = mapped_column(String(64), nullable=False, default="estimated")
    status: Mapped[str] = mapped_column(String(16), nullable=False, default=RecipeStatus.PUBLISHED.value)
    ingredients: Mapped[list] = mapped_column(JSON, nullable=False, default=list)
    instructions: Mapped[list] = mapped_column(JSON, nullable=False, default=list)
    tags: Mapped[list] = mapped_column(JSON, nullable=False, default=list)
    created_at: Mapped[datetime] = mapped_column(DateTime(timezone=True), default=utcnow, nullable=False)
    updated_at: Mapped[datetime] = mapped_column(
        DateTime(timezone=True), default=utcnow, onupdate=utcnow, nullable=False
    )

    __table_args__ = (
        Index("ix_recipes_cuisine_meal_type", "cuisine", "meal_type"),
        Index("ix_recipes_status", "status"),
    )

    @property
    def cuisine_enum(self) -> Cuisine:
        return Cuisine(self.cuisine)

    @property
    def meal_type_enum(self) -> MealType:
        return MealType(self.meal_type)
