from fastapi import APIRouter
from routes.alumne import router as alumne_router
from routes.professor import router as professor_router
from routes.usuari import router as usuari_router

api_router = APIRouter()
api_router.include_router(alumne_router)
api_router.include_router(professor_router)
api_router.include_router(usuari_router)
