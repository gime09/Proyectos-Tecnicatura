import smtplib
from email.mime.text import MIMEText
from email.mime.multipart import MIMEMultipart
from dotenv import load_dotenv
import os

# Load environment variables from .env
load_dotenv()

GMAIL_USER = os.getenv("GMAIL_USER")
GMAIL_APP_PASSWORD = os.getenv("GMAIL_APP_PASSWORD")


def send_email(recipient: str, subject: str, body: str) -> bool:
    """
    Sends a plain text email to the specified recipient using Gmail SMTP.

    Args:
        recipient (str): Email address of the recipient.
        subject (str): Subject of the email.
        body (str): Plain text content of the email body.

    Returns:
        bool: True if the email was sent successfully, False otherwise.
    """
    try:
        # Build email message
        msg = MIMEMultipart()
        msg["From"] = GMAIL_USER
        msg["To"] = recipient
        msg["Subject"] = subject
        msg.attach(MIMEText(body, "plain"))

        # Send using Gmail SMTP
        with smtplib.SMTP("smtp.gmail.com", 587) as server:
            server.starttls()
            server.login(GMAIL_USER, GMAIL_APP_PASSWORD)
            server.send_message(msg)

        return True
    except Exception:
        return False
