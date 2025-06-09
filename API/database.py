from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker
from urllib.parse import quote_plus

# Configuracio de la base de dades
MYSQL_HOST = "192.168.1.10"
MYSQL_PORT = 3306
MYSQL_USER = "admin"
MYSQL_PASSWORD = "pirineus"
MYSQL_DB_NAME = "projecte1"

# Codificacio de la contrasenya
password_encoded = quote_plus(MYSQL_PASSWORD)

# URL de conexio
DATABASE_URL = f"mysql+pymysql://{MYSQL_USER}:{password_encoded}@{MYSQL_HOST}:{MYSQL_PORT}/{MYSQL_DB_NAME}"

# Motor de conexio
engine = create_engine(DATABASE_URL)

# Configuracio de la sessio
SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)

# Dependencia de la base de dades
def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()
