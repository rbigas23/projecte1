from sqlalchemy import Column, Integer, String, ForeignKey, Table, Date
from sqlalchemy.orm import declarative_base, relationship

Base = declarative_base()

professor_grup_association = Table(
    'professor_grup',
    Base.metadata,
    Column('professor_id', Integer, ForeignKey('professor.id_professor'), primary_key=True),
    Column('grup_id', Integer, ForeignKey('grup.id_grup'), primary_key=True)
)

class Usuari(Base):
    __tablename__ = 'usuari'
    id_usuari = Column(Integer, primary_key=True, autoincrement=True)
    nom = Column(String(255), nullable=False)
    email = Column(String(255), unique=True, nullable=False)
    password = Column(String(255), nullable=False)
    data_naixement = Column(Date, nullable=True)
    id_targeta = Column(Integer, unique=True)

    registres = relationship("Registre", back_populates="usuari", cascade="all, delete-orphan")

    __mapper_args__ = {
        'polymorphic_identity': 'usuari',
    }

class Alumne(Usuari):
    __tablename__ = 'alumne'
    id_alumne = Column(Integer, ForeignKey('usuari.id_usuari'), primary_key=True)
    data_matriculacio = Column(Date, nullable=True)
    id_grup = Column(Integer, ForeignKey('grup.id_grup'))
    grup = relationship("Grup", back_populates="alumnes")

    __mapper_args__ = {
        'polymorphic_identity': 'alumne',
    }

class Professor(Usuari):
    __tablename__ = 'professor'
    id_professor = Column(Integer, ForeignKey('usuari.id_usuari'), primary_key=True)
    grups = relationship("Grup", secondary=professor_grup_association, back_populates="professors")

    __mapper_args__ = {
        'polymorphic_identity': 'professor',
    }

class Grup(Base):
    __tablename__ = 'grup'
    id_grup = Column(Integer, primary_key=True, autoincrement=True)
    nom_grup = Column(String(255), nullable=False)
    professors = relationship("Professor", secondary=professor_grup_association, back_populates="grups")
    alumnes = relationship("Alumne", back_populates="grup")

class Registre(Base):
    __tablename__ = 'registre'
    id_registre = Column(Integer, primary_key=True, autoincrement=True)
    data_hora = Column(Date, nullable=False)
    id_targeta = Column(Integer, ForeignKey("usuari.id_targeta"), nullable=False)

    usuari = relationship("Usuari", back_populates="registres")


class Assistencia(Base):
    __tablename__ = 'assistencia'
    id_assistencia = Column(Integer, primary_key=True, autoincrement=True)
    id_alumne = Column(Integer, ForeignKey('alumne.id_alumne'), nullable=False)
    id_professor = Column(Integer, ForeignKey('professor.id_professor'), nullable=False)
    id_uf = Column(Integer, ForeignKey('uf.id_uf'), nullable=False)
    data = Column(Date, nullable=False)
    tipus_assistencia = Column(String(255), nullable=False)

class Modul(Base):
    __tablename__ = 'modul'
    id_modul = Column(Integer, primary_key=True, autoincrement=True)
    desc_modul = Column(String(255), nullable=False)
    num_hores = Column(Integer, nullable=False)
    id_professor = Column(Integer, ForeignKey('professor.id_professor'))
    
    ufs = relationship("Uf", back_populates="modul", cascade="all, delete-orphan")

class Uf(Base):
    __tablename__ = 'uf'
    id_uf = Column(Integer, primary_key=True, autoincrement=True)
    codi_uf = Column(String(255), nullable=False)
    num_hores = Column(Integer, nullable=False)
    id_modul = Column(Integer, ForeignKey('modul.id_modul'), nullable=False)

    modul = relationship("Modul", back_populates="ufs")
