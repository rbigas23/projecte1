from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
from typing import List
from models import Alumne, Professor
from database import get_db
from schemas import LoginResponse, ClassesOut
from crud import get_classes_for_user

router = APIRouter(prefix="/usuaris", tags=["usuaris"])

@router.get("/{email}", response_model=LoginResponse)
def get_user_info(email: str, db: Session = Depends(get_db)):

    alumne = db.query(Alumne).filter_by(email=email).first()
    print(email)
    if alumne:
        return {
            "type": 'a',
            "user": alumne
        }

    professor = db.query(Professor).filter_by(email=email).first()
    if professor:
        return {
            "type": 'p',
            "user": professor
        }

    raise HTTPException(status_code=404, detail="Usuari no trobat.")

@router.get("/classes/{id_usuari}", response_model=List[ClassesOut])
def read_classes_for_user(id_usuari: int, db: Session = Depends(get_db)):
    classes = get_classes_for_user(db, id_usuari)
    if classes is None:
        raise HTTPException(status_code=404, detail="Usuari no trobat.")
    if not classes:
        return []
    return classes