"""API v1 routes."""

from fastapi import APIRouter

from app.api.v1.categories import router as categories_router
from app.api.v1.recipes import router as recipes_router

api_router = APIRouter()
api_router.include_router(recipes_router, prefix="/recipes", tags=["recipes"])
api_router.include_router(categories_router, tags=["categories"])
