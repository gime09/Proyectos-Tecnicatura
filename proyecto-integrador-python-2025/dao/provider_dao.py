from db.conexion import conectar
from models.provider import Provider
from models.category import Category


from db.conexion import conectar
from models.provider import Provider

class ProviderDAO:
    def __init__(self):
        self.conn = conectar()

    def get_by_category(self, category_id):
        cursor = self.conn.cursor()
        cursor.execute(
            "SELECT id, name, category_id FROM providers WHERE category_id = %s ORDER BY name",
            (category_id,)
        )
        rows = cursor.fetchall()
        cursor.close()
        return [Provider(id=row[0], name=row[1], category_id=row[2]) for row in rows]
