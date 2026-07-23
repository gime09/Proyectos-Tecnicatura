from db.conexion import conectar
from models.category import Category

class CategoryDAO:
    def __init__(self):
        self.conn = conectar()

    def get_all(self):
        cursor = self.conn.cursor()
        cursor.execute("SELECT id, name FROM categories ORDER BY name")
        rows = cursor.fetchall()
        cursor.close()
        return [Category(id=row[0], name=row[1]) for row in rows]
