from dao.category_dao import CategoryDAO

class CategoryService:
    def __init__(self):
        self.dao = CategoryDAO()

    def list_categories(self):
        return self.dao.get_all()
