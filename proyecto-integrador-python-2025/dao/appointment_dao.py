from models.appointment import Appointment
from db.conexion import conectar

class AppointmentDAO:
    def __init__(self):
        self.conn = conectar()

    def get_all(self):
        cursor = self.conn.cursor()
        cursor.execute("""
            SELECT id, date_time, provider_id, status, user_id
            FROM appointments
            ORDER BY date_time
        """)
        rows = cursor.fetchall()
        cursor.close()
        return rows

    def get_all_available(self):
        cursor = self.conn.cursor()
        cursor.execute("""
            SELECT id, date_time, provider_id, user_id
            FROM appointments
            WHERE status = 'available'
            ORDER BY date_time
        """)
        rows = cursor.fetchall()
        appointments = [Appointment(id=row[0], date_time=row[1], provider_id=row[2], user_id=row[3]) for row in rows]
        cursor.close()
        return appointments

    def book_appointment(self, appointment_id):
        cursor = self.conn.cursor()
        cursor.execute("""
            UPDATE appointments
            SET status = 'not available'
            WHERE id = %s
        """, (appointment_id,))
        self.conn.commit()
        cursor.close()

    def remove_appointment(self, appointment: Appointment):
        cursor = self.conn.cursor()
        # Assuming you have an 'id' attribute; if not, you might want to add it to Appointment
        cursor.execute(
            "DELETE FROM appointments WHERE date_time = %s",
            (appointment.date_time,)
        )
        cursor.close()

    def get_by_provider(self, provider_id):
        cursor = self.conn.cursor()
        cursor.execute("""
            SELECT id, date_time, provider_id
            FROM appointments
            WHERE provider_id = %s AND status = 'available'
            ORDER BY date_time
        """, (provider_id,))
        rows = cursor.fetchall()
        appointments = [Appointment(id=row[0], date_time=row[1], provider_id=row[2]) for row in rows]
        cursor.close()
        return appointments
    
    def assign_appointment_to_user(self, appointment_id, user_id):
        cursor = self.conn.cursor()
        cursor.execute("""
            UPDATE appointments
            SET user_id = %s, status = 'not available'
            WHERE id = %s
        """, (user_id, appointment_id))
        self.conn.commit()
        cursor.close()

    def get_appointments_by_user(self, user_id):
        cursor = self.conn.cursor()
        cursor.execute("""
            SELECT a.id, a.date_time, a.provider_id, a.user_id, p.name as provider_name, c.name as category_name
            FROM appointments a
            JOIN providers p ON a.provider_id = p.id
            JOIN categories c ON p.category_id = c.id
            WHERE a.user_id = %s AND a.status = 'not available'
            ORDER BY a.date_time
        """, (user_id,))
        rows = cursor.fetchall()
        appointments = []
        for row in rows:
            appointment = {
                'id': row[0],
                'date_time': row[1],
                'provider_id': row[2],
                'user_id': row[3],
                'provider_name': row[4],
                'category_name': row[5]
            }
            appointments.append(appointment)
        cursor.close()
        return appointments
    
    def cancel_appointment(self, appointment_id):
        cursor = self.conn.cursor()
        cursor.execute("""
            UPDATE appointments
            SET status = 'available', user_id = NULL
            WHERE id = %s
        """, (appointment_id,))
        self.conn.commit()
        cursor.close()