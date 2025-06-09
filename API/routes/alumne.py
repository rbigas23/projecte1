from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
from typing import List

from database import get_db
from schemas import AlumneCreate, AlumneGet, AlumneOut, AlumneUpdate
from crud import create_alumne, read_alumne, update_alumne

router = APIRouter(prefix="/alumnes", tags=["alumnes"])

@router.post("/", response_model=AlumneOut)
def create_alumne_route(alumne: AlumneCreate, db: Session = Depends(get_db)):
    return create_alumne(db, alumne)

@router.get("/{id_alumne}", response_model=AlumneGet)
def get_alumne_route(id_alumne: int, db: Session = Depends(get_db)):
    alumne = read_alumne(db, id_alumne)
    if not alumne:
        raise HTTPException(status_code=404, detail=f"L'alumne amb id {id_alumne} no existeix.")
    return alumne

# @router.get("/", response_model=List[AlumneOut])
# def get_all_alumnes_route(db: Session = Depends(get_db)):
#     return get_all_alumnes(db)

@router.put("/{id_alumne}", response_model=AlumneOut)
def update_alumne_route(id_alumne: int, alumne: AlumneUpdate, db: Session = Depends(get_db)):
    data = alumne.model_dump(exclude_unset=True)
    updated_alumne = update_alumne(db, id_alumne, **data)
    if not updated_alumne:
        raise HTTPException(status_code=404, detail=f"L'alumne amb id {id_alumne} no existeix.")
    return updated_alumne
