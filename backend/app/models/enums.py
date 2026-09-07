"""Shared enumerations for recipe domain."""

from enum import Enum


class Cuisine(str, Enum):
    NORTH_INDIAN = "north_indian"
    SOUTH_INDIAN = "south_indian"


class MealType(str, Enum):
    BREAKFAST = "breakfast"
    BRUNCH = "brunch"
    LUNCH = "lunch"
    SNACKS = "snacks"
    DINNER = "dinner"


class Difficulty(str, Enum):
    EASY = "easy"
    MEDIUM = "medium"
    HARD = "hard"


class RecipeStatus(str, Enum):
    DRAFT = "draft"
    REVIEW = "review"
    PUBLISHED = "published"
    ARCHIVED = "archived"


class RecipeSort(str, Enum):
    POPULAR = "popular"
    NEWEST = "newest"
    PREPARATION_TIME = "preparation_time"
    CALORIES = "calories"
    PROTEIN = "protein"
