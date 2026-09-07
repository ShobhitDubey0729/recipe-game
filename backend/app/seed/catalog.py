"""Recipe seed catalog — original descriptions, estimated nutrition."""

import re
from typing import Any


def slugify(name: str) -> str:
    slug = name.lower().strip()
    slug = re.sub(r"[^a-z0-9\s-]", "", slug)
    slug = re.sub(r"[\s_]+", "-", slug)
    return slug.strip("-")


def _recipe(
    name: str,
    cuisine: str,
    meal_type: str,
    prep: int,
    cook: int,
    calories: float,
    protein: float,
    *,
    vegan: bool = False,
    gluten_free: bool = False,
    difficulty: str = "medium",
    servings: int = 2,
    weight_g: int = 400,
) -> dict[str, Any]:
    base_ingredients = [
        {"name": "salt", "quantity_g": 3},
        {"name": "cooking oil", "quantity_g": 10},
    ]
    return {
        "name": name,
        "slug": slugify(name),
        "description": f"Homestyle {name} prepared with classic spices and fresh ingredients.",
        "cuisine": cuisine,
        "meal_type": meal_type,
        "prep_time_minutes": prep,
        "cook_time_minutes": cook,
        "servings": servings,
        "final_cooked_weight_g": float(weight_g),
        "calories_per_100g": calories,
        "protein_g_per_100g": protein,
        "vegetarian": True,
        "vegan": vegan,
        "gluten_free": gluten_free,
        "difficulty": difficulty,
        "image_url": None,
        "nutrition_source": "estimated",
        "status": "published",
        "ingredients": base_ingredients + [{"name": name.split()[0].lower(), "quantity_g": 150}],
        "instructions": [
            f"Prepare ingredients for {name}.",
            "Cook using traditional method until aromatic and done.",
            "Adjust seasoning and serve hot.",
        ],
        "tags": [meal_type, cuisine.replace("_", " "), name.split()[0].lower()],
    }


# (name, cuisine, meal_type)
_CATALOG: list[tuple] = [
    # North Indian — Breakfast
    ("Aloo Paratha", "north_indian", "breakfast"),
    ("Paneer Paratha", "north_indian", "breakfast"),
    ("Gobi Paratha", "north_indian", "breakfast"),
    ("Poori", "north_indian", "breakfast"),
    ("Chole Bhature", "north_indian", "breakfast"),
    ("Besan Chilla", "north_indian", "breakfast"),
    ("Moong Dal Chilla", "north_indian", "breakfast"),
    ("Poha", "north_indian", "breakfast"),
    ("Methi Paratha", "north_indian", "breakfast"),
    ("Thepla", "north_indian", "breakfast"),
    # North Indian — Brunch
    ("Chole", "north_indian", "brunch"),
    ("Rajma Chawal", "north_indian", "brunch"),
    ("Kadhi Pakora", "north_indian", "brunch"),
    ("Paneer Butter Masala", "north_indian", "brunch"),
    ("Stuffed Paratha", "north_indian", "brunch"),
    ("Aloo Puri", "north_indian", "brunch"),
    # North Indian — Lunch
    ("Dal Tadka", "north_indian", "lunch"),
    ("Rajma", "north_indian", "lunch"),
    ("Dal Makhani", "north_indian", "lunch"),
    ("Aloo Gobi", "north_indian", "lunch"),
    ("Bhindi Masala", "north_indian", "lunch"),
    ("Palak Paneer", "north_indian", "lunch"),
    ("Shahi Paneer", "north_indian", "lunch"),
    ("Jeera Rice", "north_indian", "lunch"),
    ("Roti", "north_indian", "lunch"),
    ("Naan", "north_indian", "lunch"),
    ("Matar Paneer", "north_indian", "lunch"),
    ("Dum Aloo", "north_indian", "lunch"),
    ("Malai Kofta", "north_indian", "lunch"),
    ("Chana Masala", "north_indian", "lunch"),
    # North Indian — Snacks
    ("Samosa", "north_indian", "snacks"),
    ("Aloo Tikki", "north_indian", "snacks"),
    ("Pakora", "north_indian", "snacks"),
    ("Dhokla", "north_indian", "snacks"),
    ("Paneer Tikka", "north_indian", "snacks"),
    ("Chaat", "north_indian", "snacks"),
    ("Kachori", "north_indian", "snacks"),
    ("Bread Pakora", "north_indian", "snacks"),
    # North Indian — Dinner
    ("Kadai Paneer", "north_indian", "dinner"),
    ("Mix Veg", "north_indian", "dinner"),
    ("Baingan Bharta", "north_indian", "dinner"),
    ("Dal Fry", "north_indian", "dinner"),
    ("Paneer Lababdar", "north_indian", "dinner"),
    ("Tawa Pulao", "north_indian", "dinner"),
    ("Lauki Chana Dal", "north_indian", "dinner"),
    ("Methi Malai Matar", "north_indian", "dinner"),
    # South Indian — Breakfast
    ("Idli", "south_indian", "breakfast"),
    ("Masala Dosa", "south_indian", "breakfast"),
    ("Plain Dosa", "south_indian", "breakfast"),
    ("Uttapam", "south_indian", "breakfast"),
    ("Medu Vada", "south_indian", "breakfast"),
    ("Pongal", "south_indian", "breakfast"),
    ("Upma", "south_indian", "breakfast"),
    ("Pesarattu", "south_indian", "breakfast"),
    ("Rava Idli", "south_indian", "breakfast"),
    ("Set Dosa", "south_indian", "breakfast"),
    # South Indian — Brunch
    ("Sambar", "south_indian", "brunch"),
    ("Lemon Rice", "south_indian", "brunch"),
    ("Curd Rice", "south_indian", "brunch"),
    ("Bisibele Bath", "south_indian", "brunch"),
    ("Vegetable Pongal", "south_indian", "brunch"),
    ("Coconut Chutney", "south_indian", "brunch"),
    # South Indian — Lunch
    ("Sambar Rice", "south_indian", "lunch"),
    ("Rasam Rice", "south_indian", "lunch"),
    ("Tamarind Rice", "south_indian", "lunch"),
    ("Vegetable Biryani", "south_indian", "lunch"),
    ("Avial", "south_indian", "lunch"),
    ("Poriyal", "south_indian", "lunch"),
    ("Tomato Rice", "south_indian", "lunch"),
    ("Coconut Rice", "south_indian", "lunch"),
    ("Kootu", "south_indian", "lunch"),
    # South Indian — Snacks
    ("Sundal", "south_indian", "snacks"),
    ("Banana Bajji", "south_indian", "snacks"),
    ("Murukku", "south_indian", "snacks"),
    ("Mysore Bonda", "south_indian", "snacks"),
    ("Paniyaram", "south_indian", "snacks"),
    ("Bonda", "south_indian", "snacks"),
    # South Indian — Dinner
    ("Vegetable Uttapam", "south_indian", "dinner"),
    ("Adai", "south_indian", "dinner"),
    ("Appam", "south_indian", "dinner"),
    ("Vegetable Stew", "south_indian", "dinner"),
    ("Neer Dosa", "south_indian", "dinner"),
    ("Rasam", "south_indian", "dinner"),
    ("Kerala Parotta", "south_indian", "dinner"),
    # Sweets & sides (extra to reach 100+)
    ("Kheer", "north_indian", "snacks"),
    ("Gulab Jamun", "north_indian", "snacks"),
    ("Jalebi", "north_indian", "snacks"),
    ("Raita", "north_indian", "snacks"),
    ("Cucumber Raita", "north_indian", "snacks"),
    ("Mango Lassi", "north_indian", "snacks"),
    ("Payasam", "south_indian", "snacks"),
    ("Kesari Bath", "south_indian", "breakfast"),
    ("Filter Coffee", "south_indian", "breakfast"),
    ("Tomato Chutney", "south_indian", "snacks"),
    ("Onion Uttapam", "south_indian", "dinner"),
    ("Masala Uttapam", "south_indian", "dinner"),
    ("Vegetable Dosa", "south_indian", "dinner"),
    ("Ghee Rice", "south_indian", "lunch"),
    ("Jeera Aloo", "north_indian", "dinner"),
    ("Aloo Matar", "north_indian", "lunch"),
    ("Baingan Ka Bharta", "north_indian", "dinner"),
    ("Moong Dal Halwa", "north_indian", "snacks"),
    ("Sabudana Khichdi", "north_indian", "breakfast"),
    ("Vegetable Pulao", "north_indian", "lunch"),
    ("Paneer Pulao", "north_indian", "lunch"),
    ("Masoor Dal", "north_indian", "lunch"),
    ("Toor Dal", "south_indian", "lunch"),
    ("Cabbage Poriyal", "south_indian", "lunch"),
    ("Beans Poriyal", "south_indian", "lunch"),
    ("Carrot Beans Poriyal", "south_indian", "lunch"),
]

_NUTRITION = {
    "breakfast": (210, 5.5),
    "brunch": (195, 6.0),
    "lunch": (175, 5.0),
    "snacks": (240, 4.0),
    "dinner": (165, 5.8),
}


def get_seed_recipes() -> list[dict[str, Any]]:
    recipes: list[dict[str, Any]] = []
    seen_slugs: set = set()

    for name, cuisine, meal_type in _CATALOG:
        slug = slugify(name)
        if slug in seen_slugs:
            slug = f"{slug}-{meal_type}"
        seen_slugs.add(slug)

        calories, protein = _NUTRITION.get(meal_type, (180, 5.0))
        # Light variation by cuisine
        if cuisine == "south_indian":
            calories -= 10
        if "dal" in name.lower() or "sambar" in name.lower():
            protein += 2.0

        recipe = _recipe(
            name,
            cuisine,
            meal_type,
            prep=15 if meal_type != "snacks" else 10,
            cook=20 if meal_type not in ("snacks", "breakfast") else 15,
            calories=calories,
            protein=protein,
            vegan="lassi" not in name.lower() and "raita" not in name.lower(),
            gluten_free="roti" not in name.lower() and "naan" not in name.lower() and "paratha" not in name.lower(),
        )
        recipe["slug"] = slug
        recipes.append(recipe)

    return recipes
