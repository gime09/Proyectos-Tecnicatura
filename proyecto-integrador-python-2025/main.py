from menu.MenuManager import MenuManager
from menu.auth_menu import show_auth_menu

def main():
    # 1) LOGIN / REGISTER BLOCK
    user_id = show_auth_menu()
    if not user_id:
        return
    # 2) POST-LOGIN MENU
    menu_manager = MenuManager()
    menu_manager.logged_user_id = user_id  # Asigna el id del usuario logueado
    menu_manager.run()

if __name__ == "__main__":
    main()
