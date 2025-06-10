from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
from database import get_db
from schemas import ProfessorCreate, ProfessorUpdate, ProfessorOut, ProfessorGet
from crud import create_professor, read_professor, update_professor, delete_professor


router = APIRouter(prefix="/professors", tags=["professors"])

@router.post("/", response_model=ProfessorOut)
def create_professor_route(professor: ProfessorCreate, db: Session = Depends(get_db)):
    return create_professor(db, professor)

@router.get("/{id_professor}", response_model=ProfessorGet)
def get_professor_route(id_professor: int, db: Session = Depends(get_db)):
    professor = read_professor(db, id_professor)
    if not professor:
        raise HTTPException(status_code=404, detail=f"El professor amb id {id_professor} no existeix.")
    return professor

@router.put("/{id_professor}", response_model=ProfessorOut)
def update_professor_route(id_professor: int, professor: ProfessorUpdate, db: Session = Depends(get_db)):
    data = professor.model_dump(exclude_unset=True)
    updated_professor = update_professor(db, id_professor, **data)
    if not updated_professor:
        raise HTTPException(status_code=404, detail=f"El professor amb id {id_professor} no existeix.")
    return updated_professor

@router.delete("/{id_professor}", response_model=ProfessorGet)
def delete_professor_route(id_professor: int, db: Session = Depends(get_db)):
    professor = delete_professor(db, id_professor)
    if not professor:
        raise HTTPException(status_code=404, detail=f"El professor amb id {id_professor} no existeix.")
    return professor
