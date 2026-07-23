from dao.provider_dao import ProviderDAO

class ProviderService:
    def __init__(self):
        self.dao = ProviderDAO()

    def list_providers_by_category(self, category_id):
        return self.dao.get_by_category(category_id)
