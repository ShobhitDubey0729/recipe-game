"""Recipe API schemas."""

from datetime import datetime
from typing import List, Optional
from uuid import UUID

from pydantic import BaseModel, ConfigDict, Field

from app.models.enums import Cuisine, Difficulty, MealType, RecipeSort, RecipeStatus


class IngredientSchema(BaseModel):
    name: str
    quantity_g: float


class RecipeBase(BaseModel):
    name: str
    slug: str
    description: str
    cuisine: Cuisine
    meal_type: MealType
    prep_time_minutes: int = Field(ge=0)
    cook_time_minutes: int = Field(ge=0)
    servings: int = Field(ge=1)
    final_cooked_weight_g: Optional[float] = Field(default=None, ge=0)
    calories_per_100g: float = Field(ge=0)
    protein_g_per_100g: float = Field(ge=0)
    vegetarian: bool = True
    vegan: bool = False
    gluten_free: bool = False
    difficulty: Difficulty = Difficulty.MEDIUM
    image_url: Optional[str] = None
    nutrition_source: str = "estimated"
    status: RecipeStatus = RecipeStatus.PUBLISHED
    ingredients: List[IngredientSchema] = Field(default_factory=list)
    instructions: List[str] = Field(default_factory=list)
    tags: List[str] = Field(default_factory=list)


class RecipeSummary(BaseModel):
    model_config = ConfigDict(from_attributes=True)

    id: UUID
    name: str
    slug: str
    cuisine: Cuisine
    meal_type: MealType
    prep_time_minutes: int
    cook_time_minutes: int
    calories_per_100g: float
    protein_g_per_100g: float
    vegetarian: bool
    vegan: bool
    gluten_free: bool
    difficulty: Difficulty
    image_url: Optional[str] = None


class RecipeDetail(RecipeSummary):
    description: str
    servings: int
    final_cooked_weight_g: Optional[float] = None
    nutrition_source: str
    ingredients: List[IngredientSchema]
    instructions: List[str]
    tags: List[str]
    created_at: datetime
    updated_at: datetime


class RecipeFilterParams(BaseModel):
    q: Optional[str] = None
    cuisine: Optional[Cuisine] = None
    meal_type: Optional[MealType] = None
    vegetarian: Optional[bool] = None
    vegan: Optional[bool] = None
    gluten_free: Optional[bool] = None
    high_protein: Optional[bool] = None
    low_calorie: Optional[bool] = None
    max_prep_time_minutes: Optional[int] = Field(default=None, ge=0)
    sort: RecipeSort = RecipeSort.POPULAR
    page: int = Field(default=1, ge=1)
    page_size: int = Field(default=20, ge=1, le=100)
