from datetime import datetime

class Appointment:
    def __init__(self, id, date_time, provider_id, status='available', user_id=None):
        self.id = id
        self.date_time = date_time
        self.provider_id = provider_id
        self.status = status
        self.user_id = user_id


    def __str__(self):
        return self.date_time.strftime('%A %d/%m/%Y - %H:%M')
