from sqlalchemy.orm import Session
from models import Alumne, Professor, Assistencia, Uf, Modul
from schemas import *


# CRUD Alumne

def create_alumne(db: Session, alumne: AlumneCreate):
    new_alumne = Alumne(
        nom=alumne.nom,
        email=alumne.email,
        password=alumne.password,
        data_naixement=alumne.data_naixement,
        id_targeta=alumne.id_targeta,
        data_matriculacio=alumne.data_matriculacio,
        id_grup=alumne.id_grup
    )
    db.add(new_alumne)
    db.commit()
    db.refresh(new_alumne)
    return new_alumne

def read_alumne(db: Session, alumne_id: int):
    return db.query(Alumne).filter(Alumne.id_alumne == alumne_id).first()

def update_alumne(db: Session, alumne_id: int, **kwargs):
    alumne = db.query(Alumne).filter(Alumne.id_alumne == alumne_id).first()
    if not alumne:
        return None
    for key, value in kwargs.items():
        if hasattr(alumne, key):
            setattr(alumne, key, value)
    db.commit()
    db.refresh(alumne)
    return alumne


# CRUD Professor

def create_professor(db: Session, professor: ProfessorCreate):
    new_professor = Professor(
        nom=professor.nom,
        email=professor.email,
        password=professor.password,
        data_naixement=professor.data_naixement,
        id_targeta=professor.id_targeta
    )
    db.add(new_professor)
    db.commit()
    db.refresh(new_professor)
    return new_professor

def read_professor(db: Session, professor_id: int):
    return db.query(Professor).filter(Professor.id_professor == professor_id).first()

def update_professor(db: Session, professor_id: int, **kwargs):
    professor = db.query(Professor).filter(Professor.id_professor == professor_id).first()
    if not professor:
        return None
    for key, value in kwargs.items():
        if hasattr(professor, key):
            setattr(professor, key, value)
    db.commit()
    db.refresh(professor)
    return professor


# CRUD Assistencia

def create_assistencia(db: Session, id_alumne: int, id_professor: int, id_uf: int, data, tipus_assistencia: str):
    new_assistencia = Assistencia(
        id_alumne=id_alumne,
        id_professor=id_professor,
        id_uf=id_uf,
        data=data,
        tipus_assistencia=tipus_assistencia
    )
    db.add(new_assistencia)
    db.commit()
    db.refresh(new_assistencia)
    return new_assistencia

def read_assistencia(db: Session, assistencia_id: int):
    return db.query(Assistencia).filter(Assistencia.id_assistencia == assistencia_id).first()

def update_assistencia(db: Session, assistencia_id: int, **kwargs):
    assistencia = db.query(Assistencia).filter(Assistencia.id_assistencia == assistencia_id).first()
    if not assistencia:
        return None
    for key, value in kwargs.items():
        if hasattr(assistencia, key):
            setattr(assistencia, key, value)
    db.commit()
    db.refresh(assistencia)
    return assistencia

def delete_assistencia(db: Session, assistencia_id: int):
    db_assistencia = db.query(Assistencia).filter(Assistencia.id_assistencia == assistencia_id).first()
    if not db_assistencia:
        return None
    db.delete(db_assistencia)
    db.commit()
    return db_assistencia


# CRUD Classes

def get_classes_for_user(db: Session, id_usuari: int):

    professor = db.query(Professor).filter_by(id_usuari=id_usuari).first()
    if professor:
        return (
            db.query(Uf.codi_uf.label("codi_uf"), Modul.desc_modul.label("desc_modul"))
              .join(Modul, Uf.id_modul == Modul.id_modul)
              .filter(Modul.id_professor == professor.id_professor)
              .distinct()
              .all()
        )

    alumne = db.query(Alumne).filter_by(id_usuari=id_usuari).first()
    if alumne:
        return (
            db.query(Uf.codi_uf.label("codi_uf"), Modul.desc_modul.label("desc_modul"))
              .join(Assistencia, Assistencia.id_uf == Uf.id_uf)
              .join(Modul, Uf.id_modul == Modul.id_modul)
              .filter(Assistencia.id_alumne == alumne.id_alumne)
              .distinct()
              .all()
        )

    return None
