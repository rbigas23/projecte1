from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
from typing import List
from models import Alumne, Professor
from database import get_db
from schemas import LoginResponse, ClassesOut, AssistenciaOut, AssistenciaBase
from crud import get_classes_for_user, get_asistencies_by_user_id, get_last_7_days_asistencies_by_user_id, delete_assistencia

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

@router.get("/assistencies/{id_usuari}", response_model=List[AssistenciaOut])
def read_assistencias(id_usuari: int, db: Session = Depends(get_db)):
    asistencias = get_asistencies_by_user_id(db, id_usuari)
    if not asistencias:
        raise HTTPException(status_code=404, detail="No s'han trobat assistencies.")
    return asistencias

@router.get("/assistencies_home/{id_usuari}", response_model=List[AssistenciaOut])
def read_assistencias(id_usuari: int, db: Session = Depends(get_db)):
    asistencias = get_last_7_days_asistencies_by_user_id(db, id_usuari)
    if not asistencias:
        raise HTTPException(status_code=404, detail="No s'han trobat assistencies..")
    return asistencias
