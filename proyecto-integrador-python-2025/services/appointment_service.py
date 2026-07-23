from dao.appointment_dao import AppointmentDAO

class AppointmentService:
    def __init__(self):
        self.dao = AppointmentDAO()

    def list_appointments_for_provider(self, provider_id):
        return self.dao.get_by_provider(provider_id)

    def book_appointment_by_provider(self, provider_id, index):
        appointments = self.dao.get_by_provider(provider_id)
        if 0 <= index < len(appointments):
            selected = appointments[index]
            self.dao.book_appointment(selected.id)
            return selected
        return None

    def list_available_appointments(self):
        return self.dao.get_all_available()

    def list_all_appointments(self):
        return self.dao.get_all()
    
    def assign_appointment_to_logged_user(self, appointment_id, user_id):
        self.dao.assign_appointment_to_user(appointment_id, user_id)
    
    def get_appointments_by_user(self, user_id):
        """Devuelve los turnos asignados al usuario."""
        return self.dao.get_appointments_by_user(user_id)
    
    def cancel_appointment(self, appointment_id):
        """Cancelación de un turno."""
        self.dao.cancel_appointment(appointment_id)
