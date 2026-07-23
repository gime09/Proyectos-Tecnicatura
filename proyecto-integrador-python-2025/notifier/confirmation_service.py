# notifier/confirmation_service.py

class ConfirmationService:
    """
    This class handles confirmation messages after a successful booking.
    """

    @staticmethod
    def confirm(appointment):
        """
        Print a user-friendly confirmation message for the booked appointment.
        :param appointment: The Appointment object that was booked.
        """
        print("\n✅ Your appointment has been successfully booked!")
        print(f"📅 Date & Time: {appointment}")
        print("\n🙏 Thank you for using our scheduling service.\n")
