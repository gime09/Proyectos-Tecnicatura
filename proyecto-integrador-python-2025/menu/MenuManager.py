from services.category_service import CategoryService
from services.provider_service import ProviderService
from services.appointment_service import AppointmentService
from notifier.confirmation_service import ConfirmationService

class MenuManager:
    def __init__(self):
        self.category_service = CategoryService()
        self.provider_service = ProviderService()
        self.appointment_service = AppointmentService()
        self.logged_user_id = None  # Se asignará después del login

    def show_main_menu(self):
        """Muestra el menú principal y retorna la opción seleccionada"""
        print("\n" + "=" * 50)
        print("🏥 APPOINTMENT MANAGER - MAIN MENU")  # Gestor de Turnos - Menú Principal
        print("=" * 50)
        print("1. 📅 Schedule new appointment")  # Agendar nuevo turno
        print("2. 👀 View my appointments")  # Ver mis turnos
        print("3. ❌ Cancel appointment")  # Cancelar turno
        print("0. 🚪 Exit")  # Salir
        print("-" * 50)

        try:
            option = int(input("Select an option: "))  # Seleccione una opción
            return option
        except ValueError:
            print("❌ Please enter a valid number.")  # Por favor ingrese un número válido
            return -1

    def book_appointment_flow(self):
        """Flujo completo para agendar un turno"""
        print("\n📅 SCHEDULE NEW APPOINTMENT")  # Agendar Nuevo Turno
        print("-" * 30)

        # Step 1: Select category
        selected_category = self._select_category()
        if not selected_category:
            return

        # Step 2: Select provider
        selected_provider = self._select_provider(selected_category)
        if not selected_provider:
            return

        # Step 3: Select appointment
        booked_appointment = self._select_appointment(selected_provider)
        if booked_appointment:
            ConfirmationService.confirm(booked_appointment)
            input("\nPress Enter to continue...")  # Presione Enter para continuar

    def view_my_appointments(self):
        """Muestra todos los turnos del usuario"""
        print("\n👀 MY APPOINTMENTS")  # Mis Turnos
        print("-" * 30)

        # Llama al servicio para obtener los turnos asignados al usuario desde la base de datos
        if self.logged_user_id is not None:
            appointments = self.appointment_service.get_appointments_by_user(self.logged_user_id)
            if not appointments:
                print("📭 You have no scheduled appointments.")  # No tienes turnos agendados
                input("\nPress Enter to continue...")  # Presione Enter para continuar
                return

            for i, appointment in enumerate(appointments):
                print(f"\n{i + 1}. 📋 Appointment:")
                print(f"   📅 Date & Time: {appointment['date_time']}") # Horario
                print(f"   🏷️ Category: {appointment['category_name']}") # Categoria
                print(f"   👨‍⚕️ Professional: {appointment['provider_name']}") # Profesional
                print("-" * 40)
        else:
            print("⚠️ No user logged in.")
        
        input("\nPress Enter to continue...")  # Presione Enter para continuar

    def cancel_appointment(self):
        """Flujo para cancelar un turno"""
        print("\n❌ CANCEL APPOINTMENT")  # Cancelar Turno
        print("-" * 30)

        # Llama al servicio para obtener los turnos asignados al usuario desde la base de datos
        if self.logged_user_id is not None:
            appointments = self.appointment_service.get_appointments_by_user(self.logged_user_id)
            if not appointments:
                print("📭 You have no scheduled appointments.")  # No tienes turnos agendados
                input("\nPress Enter to continue...")  # Presione Enter para continuar
                return
            
            while True: # Bucle infinito que se rompe cuando la opción es válida
                for i, appointment in enumerate(appointments):
                    print(f"\n{i + 1}. 📋 Appointment:")
                    print(f"   📅 Date & Time: {appointment['date_time']}") # Horario
                    print(f"   🏷️ Category: {appointment['category_name']}") # Categoria
                    print(f"   👨‍⚕️ Professional: {appointment['provider_name']}") # Profesional
                    print("-" * 40)
            
                try:
                    appointment_option = int(input("Select an appointment to cancel (1-{}, 0 to go back): ".format(len(appointments))))  # Seleccionar un turno para cancelar o 0 para volver al menu anterior
                    if appointment_option == 0:
                        return

                    if 1 <= appointment_option <= len(appointments):
                        appointment_to_cancel = appointments[appointment_option - 1]
                        self.appointment_service.cancel_appointment(appointment_to_cancel['id']) # Se le pasa el id del turno a cancelar
                        print("✅ Appointment cancelled successfully.")  # Turno cancelado exitosamente
                        break
                    else:
                        print("❌ Invalid appointment option.")  # Opción de turno inválida

                except ValueError:
                    print("❌ Please enter a valid number.")  # Por favor ingrese un número válido

        input("\nPress Enter to continue...")  # Presione Enter para continuar

    def _select_category(self):
        """Método auxiliar para seleccionar categoría"""
        categories = self.category_service.list_categories()

        if not categories:
            print("⚠️ No categories available.")  # No hay categorías disponibles
            return None

        print("\n📂 Available categories:")  # Categorías disponibles
        for i, category in enumerate(categories):
            print(f"{i + 1}. {category.name}")

        try:
            category_option = int(input(f"\nSelect a category (1-{len(categories)}, 0 to go back): "))  # Seleccione una categoría, 0 para volver

            if category_option == 0:
                return None

            if 1 <= category_option <= len(categories):
                return categories[category_option - 1]
            else:
                print("❌ Invalid category option.")  # Opción de categoría inválida
                return None

        except ValueError:
            print("❌ Please enter a valid number.")  # Por favor ingrese un número válido
            return None

    def _select_provider(self, category):
        """Método auxiliar para seleccionar proveedor"""
        providers = self.provider_service.list_providers_by_category(category.id)

        if not providers:
            print(f"⚠️ No providers available in category {category.name}.")  # No hay proveedores disponibles en la categoría
            return None

        print(f"\n👨‍⚕️ Providers in {category.name}:")  # Proveedores en
        for i, provider in enumerate(providers):
            print(f"{i + 1}. {provider.name}")

        try:
            provider_option = int(input(f"\nSelect a provider (1-{len(providers)}, 0 to go back): "))  # Seleccione un proveedor, 0 para volver

            if provider_option == 0:
                return None

            if 1 <= provider_option <= len(providers):
                return providers[provider_option - 1]
            else:
                print("❌ Invalid provider option.")  # Opción de proveedor inválida
                return None

        except ValueError:
            print("❌ Please enter a valid number.")  # Por favor ingrese un número válido
            return None

    def _select_appointment(self, provider):
        """Método auxiliar para seleccionar cita"""
        appointments = self.appointment_service.list_appointments_for_provider(provider.id)

        if not appointments:
            print(f"⏳ No appointments available for {provider.name}.")  # No hay turnos disponibles para
            return None

        print(f"\n📅 Available appointments for {provider.name}:")  # Turnos disponibles para
        for i, appointment in enumerate(appointments):
            print(f"{i + 1}. {appointment}")

        try:
            appointment_option = int(input(f"\nSelect an appointment (1-{len(appointments)}, 0 to go back): "))  # Seleccione un turno, 0 para volver

            if appointment_option == 0:
                return None

            if 1 <= appointment_option <= len(appointments):
                booked = self.appointment_service.book_appointment_by_provider(provider.id, appointment_option - 1)
                if booked and self.logged_user_id is not None:
                    self.appointment_service.assign_appointment_to_logged_user(booked.id, self.logged_user_id)
                return booked
            else:
                print("❌ Invalid appointment option.")  # Opción de turno inválida
                return None

        except ValueError:
            print("❌ Please enter a valid number.")  # Por favor ingrese un número válido
            return None

    def run(self):
        """Método principal que ejecuta el menú"""
        print("🏥 Welcome to the Appointment Manager")  # Bienvenido al Gestor de Turnos

        while True:
            option = self.show_main_menu()

            if option == 0:
                print("👋 Thank you for using the Appointment Manager!")  # ¡Gracias por usar el Gestor de Turnos!
                break
            elif option == 1:
                self.book_appointment_flow()
            elif option == 2:
                self.view_my_appointments()
            elif option == 3:
                self.cancel_appointment()
            else:
                if option != -1:  # -1 es error de input, ya mostrado
                    print("❌ Invalid option. Please select an option from the menu.")  # Opción inválida. Por favor seleccione una opción del menú
                input("\nPress Enter to continue...")  # Presione Enter para continuar