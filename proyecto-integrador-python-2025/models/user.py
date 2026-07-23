class User:
    """
    Represents a basic user in the system.
    Email and password are mandatory for authentication.

    Attributes:
        id (int): Optional user ID (assigned by the database)
        email (str): User email (required)
        password (str): User password (required, should be hashed)
        first_name (str): First name (optional)
        last_name (str): Last name (optional)
        dni (str): National ID number (optional)
    """

    def __init__(
        self,
        email: str,
        password: str,
        first_name: str = "",
        last_name: str = "",
        dni: str = "",
        id: int = None
    ):
        if not email or not password:
            raise ValueError("Email and password are required fields.")
        self.id = id
        self.email = email
        self.password = password
        self.first_name = first_name
        self.last_name = last_name
        self.dni = dni

    def __repr__(self):
        return f"<User {self.email}>"
