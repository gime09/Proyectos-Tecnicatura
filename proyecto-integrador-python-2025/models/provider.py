from models.category import Category

class Provider:
    def __init__(self, id, name, category_id):
        self.id = id
        self.name = name
        self.category_id = category_id

    def __str__(self):
        return self.name
