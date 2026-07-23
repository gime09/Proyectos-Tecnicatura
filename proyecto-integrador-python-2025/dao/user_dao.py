import re
from psycopg2.extensions import connection

class UserDAO:
    def __init__(self, conn: connection):
        self.conn = conn

    def validate_email(self, email: str) -> bool:
        """
        Validates email format using a regular expression.
        """
        pattern = r'^[\w\.\-]+@[\w\.\-]+\.\w+$'
        return re.match(pattern, email) is not None

    def user_exists(self, email: str) -> bool:
        """
        Returns True if a user with the given email exists.
        """
        try:
            cur = self.conn.cursor()
            cur.execute("SELECT 1 FROM users WHERE email = %s;", (email,))
            exists = cur.fetchone() is not None
            cur.close()
            return exists
        except Exception:
            return False

    def create_user(self, email: str, password: str, first_name: str, last_name: str) -> bool:
        """
        Inserts a new user record into the database with first name and last name.
        """
        try:
            cur = self.conn.cursor()
            cur.execute(
                "INSERT INTO users (email, password, first_name, last_name) VALUES (%s, %s, %s, %s);",
                (email, password, first_name, last_name)
            )
            self.conn.commit()
            cur.close()
            return True
        except Exception as e:
            self.conn.rollback()
            print(f"❌ Error creating user: {e}")
            return False

    def authenticate(self, email: str, password: str):
        """
        Validates user credentials and returns user id if valid, else None.
        """
        try:
            cur = self.conn.cursor()
            cur.execute(
                "SELECT id FROM users WHERE email = %s AND password = %s;",
                (email, password)
            )
            result = cur.fetchone()
            cur.close()
            if result:
                return result[0]  # El id del usuario
            return None
        except Exception:
            return None
