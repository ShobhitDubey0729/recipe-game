"""Recipe API endpoints (Phase 2 foundation)."""

from fastapi import APIRouter

router = APIRouter()


@router.get("")
async def list_recipes() -> dict:
    """Placeholder until Phase 2 database integration."""
    return {
        "items": [],
        "page": 1,
        "page_size": 20,
        "total": 0,
        "message": "Recipe API ready — seed data coming in Phase 2",
    }
