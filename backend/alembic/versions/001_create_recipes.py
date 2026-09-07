"""Initial recipes table."""

from typing import Sequence, Union

import sqlalchemy as sa
from alembic import op
from sqlalchemy.dialects import postgresql

revision: str = "001_create_recipes"
down_revision: Union[str, None] = None
branch_labels: Union[str, Sequence[str], None] = None
depends_on: Union[str, Sequence[str], None] = None


def upgrade() -> None:
    op.create_table(
        "recipes",
        sa.Column("id", postgresql.UUID(as_uuid=True), primary_key=True),
        sa.Column("name", sa.String(255), nullable=False),
        sa.Column("slug", sa.String(255), nullable=False),
        sa.Column("description", sa.Text(), nullable=False),
        sa.Column("cuisine", sa.String(32), nullable=False),
        sa.Column("meal_type", sa.String(32), nullable=False),
        sa.Column("prep_time_minutes", sa.Integer(), nullable=False),
        sa.Column("cook_time_minutes", sa.Integer(), nullable=False),
        sa.Column("servings", sa.Integer(), nullable=False),
        sa.Column("final_cooked_weight_g", sa.Float(), nullable=True),
        sa.Column("calories_per_100g", sa.Float(), nullable=False),
        sa.Column("protein_g_per_100g", sa.Float(), nullable=False),
        sa.Column("vegetarian", sa.Boolean(), nullable=False),
        sa.Column("vegan", sa.Boolean(), nullable=False),
        sa.Column("gluten_free", sa.Boolean(), nullable=False),
        sa.Column("difficulty", sa.String(16), nullable=False),
        sa.Column("image_url", sa.Text(), nullable=True),
        sa.Column("nutrition_source", sa.String(64), nullable=False),
        sa.Column("status", sa.String(16), nullable=False),
        sa.Column("ingredients", sa.JSON(), nullable=False),
        sa.Column("instructions", sa.JSON(), nullable=False),
        sa.Column("tags", sa.JSON(), nullable=False),
        sa.Column("created_at", sa.DateTime(timezone=True), nullable=False),
        sa.Column("updated_at", sa.DateTime(timezone=True), nullable=False),
    )
    op.create_index("ix_recipes_slug", "recipes", ["slug"], unique=True)
    op.create_index("ix_recipes_cuisine", "recipes", ["cuisine"])
    op.create_index("ix_recipes_meal_type", "recipes", ["meal_type"])
    op.create_index("ix_recipes_vegetarian", "recipes", ["vegetarian"])
    op.create_index("ix_recipes_calories_per_100g", "recipes", ["calories_per_100g"])
    op.create_index("ix_recipes_protein_g_per_100g", "recipes", ["protein_g_per_100g"])
    op.create_index("ix_recipes_cuisine_meal_type", "recipes", ["cuisine", "meal_type"])
    op.create_index("ix_recipes_status", "recipes", ["status"])


def downgrade() -> None:
    op.drop_table("recipes")
