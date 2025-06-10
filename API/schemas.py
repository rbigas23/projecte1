from pydantic import BaseModel
from datetime import date
from typing import List, Optional, Union

# Schema Classes

class ClassesOut(BaseModel):
    codi_uf: str
    desc_modul: str

    class Config:
        orm_mode = True

# Schema Grup

class Grup(BaseModel):
    id_grup: int
    nom_grup: str

    class Config:
        orm_mode = True


# Schemas Alumne

class AlumneBase(BaseModel):
    nom: str
    email: str
    data_naixement: date
    data_matriculacio: Optional[date] = None
    id_grup: Optional[int] = None
    id_targeta: Optional[int] = None

    class Config:
        orm_mode = True

class AlumneCreate(AlumneBase):
    password: str

class AlumneUpdate(BaseModel):
    nom: Optional[str] = None
    email: Optional[str] = None
    password: Optional[str] = None
    data_naixement: Optional[date] = None
    data_matriculacio: Optional[date] = None
    id_grup: Optional[int] = None
    id_targeta: Optional[int] = None

class AlumneOut(AlumneBase):
    id_alumne: int

class AlumneGet(BaseModel):
    id_alumne: int
    nom: Optional[str] = None
    email: Optional[str] = None
    password: Optional[str] = None
    data_naixement: Optional[date] = None
    data_matriculacio: Optional[date] = None
    grup: Optional[Grup] = None
    id_targeta: Optional[int] = None

    class Config:
        orm_mode = True


# Schemas Professor

class ProfessorBase(BaseModel):
    nom: str
    email: str
    data_naixement: date
    id_targeta: Optional[int] = None

    class Config:
        orm_mode = True

class ProfessorCreate(ProfessorBase):
    password: str

class ProfessorUpdate(BaseModel):
    nom: Optional[str] = None
    email: Optional[str] = None
    password: Optional[str] = None
    data_naixement: Optional[date] = None
    id_targeta: Optional[int] = None

class ProfessorOut(ProfessorBase):
    id_professor: int

class ProfessorGet(BaseModel):
    id_professor: int
    nom: Optional[str] = None
    email: Optional[str] = None
    password: Optional[str] = None
    data_naixement: Optional[date] = None
    grups: List[Grup] = []
    id_targeta: Optional[int] = None

    class Config:
        orm_mode = True


# Schema Login

class LoginResponse(BaseModel):
    type: str
    user: Union[AlumneGet, ProfessorGet]


# Schemas Assistencia

class AssistenciaBase(BaseModel):
    id_alumne: int
    id_professor: int
    id_uf: int
    data: date
    tipus_assistencia: str

class AssistenciaCreate(AssistenciaBase):
    pass

class AssistenciaUpdate(BaseModel):
    tipus_assistencia: Optional[str] = None

class AssistenciaOut(BaseModel):
    data: date
    tipus_assistencia: str
    uf: str
    
    class Config:
        orm_mode = True
