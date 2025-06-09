from fastapi import FastAPI
from routes import api_router
from models import Base
from database import engine


app = FastAPI()

Base.metadata.create_all(bind=engine)

app.include_router(api_router)
