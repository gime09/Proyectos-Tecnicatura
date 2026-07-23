from db.conexion import conectar
from dao.user_dao import UserDAO
from notifier.smtp_sender import send_email

def register_user():
    print("📝 --- User Registration --- 📝")
    conn = conectar()
    if conn is None:
        print("❌ Failed to connect to the database.")
        return

    dao = UserDAO(conn)

    while True:
        email = input("📧 Enter your email: ").strip()
        if not dao.validate_email(email):
            print("⚠️ Invalid email format. Example: user@example.com")
            continue

        if dao.user_exists(email):
            print("⚠️ This email is already registered.")
            choice = input("🔁 Try a different email? (y/n): ").strip().lower()
            if choice == 'y':
                continue
            else:
                break

        first_name = input("🧑 Enter your first name: ").strip()
        last_name = input("👨 Enter your last name: ").strip()

        password = input("🔒 Enter your password: ").strip()
        confirm = input("🔒 Confirm your password: ").strip()

        if password != confirm:
            print("⚠️ Passwords do not match.")
            if input("🔁 Retry? (y/n): ").strip().lower() != 'y':
                break
            continue

        if len(password) < 6:
            print("⚠️ Password must be at least 6 characters long.")
            if input("🔁 Retry? (y/n): ").strip().lower() != 'y':
                break
            continue

        # Create user with full name
        if dao.create_user(email, password, first_name, last_name):
            print("✅ Registration successful.")

            # Send confirmation email
            subject = "Welcome to the Appointment System Solución 202"  # Totalmente editable
            body = f"Hi {first_name},\n\nYour registration was successful.\n\nYou can now log in and book appointments.\n\nRegards,\nSolución202 System Team"

            if send_email(email, subject, body):
                print("📧 Confirmation email sent.")
            else:
                print(
                    "⚠️ Registration completed, but failed to send confirmation email.")
        else:
            print("❌ Error saving user. Please try again later.")
        break

    conn.close()


def login():
    conn = conectar()
    if conn is None:
        print("❌ Failed to connect to the database.")
        return None

    dao = UserDAO(conn)

    print("\n🔑 --- User Login --- 🔑")
    email = input("📧 Email: ").strip()
    password = input("🔒 Password: ").strip()

    user_id = dao.authenticate(email, password)
    if user_id:
        print("✅ Login successful!")
        conn.close()
        return user_id  # Retorna el id
    else:
        print("❌ Invalid email or password.")
        conn.close()
        return None


def show_auth_menu():
    """
    Main authentication menu. Returns user_id if login was successful,
    or None if user chooses to exit.
    """
    while True:
        print("\n🔐 === Authentication Menu === 🔐")
        print("1️⃣  Log In")
        print("2️⃣  Register")
        print("3️⃣  Exit")
        choice = input("Choose an option (1/2/❌): ").strip()

        if choice == '1':
            user_id = login()
            if user_id:
                return user_id  # Retorna el id
        elif choice == '2':
            register_user()
        elif choice.lower() in ['❌', 'x', 'exit', '3']:
            print("👋 Goodbye!")
            return None
        else:
            print("⚠️ Invalid option. Please try again.")
