package BlackJack;
import solucionCoin.Juego;
import solucionCoin.Jugador;
import solucionCoin.Utilidades;

import java.util.Scanner;

public class BlackJack extends Juego {
    private Mazo mazo;
    private Mano manoJugador;
    private Mano manoCrupier;
    private double apuesta;
    private Jugador jugadorBJ;

    Scanner scanner = new Scanner(System.in);

    // Constructor
    public BlackJack(Jugador jugador) {
        mazo = new Mazo();
        manoJugador = new Mano();
        manoCrupier = new Mano();
        apuesta = 0.0;
        jugadorBJ = jugador;
    }

    // Método para iniciar el menu del juego
    @Override
    public void iniciarJuego() {
        cargando();
        System.out.print("Bienvenido al Juego de Black Jack");
        int opcion;
        do{
            System.out.println("...:::MENU BLACKJACK:::...");
            System.out.println("");
            System.out.println("""
                    1 - Jugar
                    2 - Ingresar Dinero
                    3 - Consultar Saldo
                    4 - Salir
                    """);

            System.out.print("Seleccione una opcion: ");
            opcion = Integer.parseInt(scanner.nextLine());

            switch (opcion) {
                case 1:
                    if (jugadorBJ.getPlata() > 0){
                        apostar();
                    }else{
                        Utilidades.imprimirSeparador();
                        System.out.println("Debe ingresar dinero para poder apostar. ");
                        Utilidades.imprimirSeparador();
                    }
                    break;
                case 2:
                    System.out.print("\nDigite el monto que desea ingresar :");
                    double ingreso = Double.parseDouble(scanner.nextLine());
                    if (ingreso >0){
                        jugadorBJ.setPlata(ingreso);
                    }else {
                        Utilidades.imprimirSeparador();
                        System.out.println("Debe ingresar un monto valido.");
                        Utilidades.imprimirSeparador();
                    }
                    break;
                case 3:
                    Utilidades.imprimirSeparador();
                    System.out.println(jugadorBJ.getNombre()+ " Tienes el saldo de : $" + jugadorBJ.getPlata());
                    Utilidades.imprimirSeparador();
                    break;
                case 4:
                    System.out.println("Hasta luego..");
                    break;
                default:
                    Utilidades.imprimirSeparador();
                    System.out.println("Opcion no valida");
                    Utilidades.imprimirSeparador();
            }
        }
        while (opcion != 4);
    }

    // Método para iniciar la apuesta
    @Override
    public void apostar() {
        System.out.print("Ingresa tu apuesta: ");
        apuesta = scanner.nextDouble();
        scanner.nextLine(); // Limpiar el buffer

        if (apuesta> jugadorBJ.getPlata()){
            Utilidades.imprimirSeparador();
            System.out.print("No tiene suficiente dinero para poder realizar esta apuesta. ");
            Utilidades.imprimirSeparador();
        } else {
            // Repartir cartas iniciales
            repartirCartasIniciales();

            // Mostrar las manos iniciales
            Mano.mostrarManos(manoJugador, manoCrupier, false);

            // Turno del jugador
            turnoJugador();

            // Turno del crupier
            if (!manoJugador.sePasoDe21()) {
                turnoCrupier();
            }

            // Determinar ganador
            determinarGanador();
        }
    }

    private void repartirCartasIniciales() {
        manoJugador.agregarCarta(mazo.repartirCarta());
        manoCrupier.agregarCarta(mazo.repartirCarta());
        manoJugador.agregarCarta(mazo.repartirCarta());
        manoCrupier.agregarCarta(mazo.repartirCarta());
    }

    private void turnoJugador() {
        boolean jugadorSePlanta = false;
        if(manoJugador.tieneBlackJack()) {
            blackJackWins();
            ganarApuesta(apuesta);
        }else{
            while (!jugadorSePlanta && !manoJugador.sePasoDe21()) {
                System.out.print("""
                    MENU DEL JUGADOR:
                        1- Pedir una carta
                        2- Plantarse
                    """);
                System.out.print("Ingresa tu elección: ");
                int respuesta = Integer.parseInt(scanner.nextLine());

                switch (respuesta){
                    case 1:
                        Utilidades.imprimirSeparador();
                        manoJugador.agregarCarta(mazo.repartirCarta());
                        Mano.mostrarManos(manoJugador, manoCrupier, false);
                        break;
                    case 2:
                        jugadorSePlanta = true;
                        break;
                    default:
                        Utilidades.imprimirSeparador();
                        System.out.println("Digite una opción valida.");
                        Utilidades.imprimirSeparador();
                }
            }
        }

    }

    private void turnoCrupier() {
        Utilidades.limpiarPantalla();

        while (manoCrupier.getValorMano() <= 16) {
            System.out.println("el crupier esta tomando una carta");
            Utilidades.pausar(1000);
            manoCrupier.agregarCarta(mazo.repartirCarta());
        }
        Mano.mostrarManos(manoJugador, manoCrupier, true);
    }

    private void determinarGanador() {
        int valorJugador = manoJugador.getValorMano();
        int valorCrupier = manoCrupier.getValorMano();

        if (valorJugador > 21) {
            System.out.println("¡Te has pasado de 21! Perdiste.");
            perderApuesta(apuesta);
        } else if (valorCrupier > 21) {
            System.out.println("¡El crupier se ha pasado de 21! Has ganado.");
            ganarApuesta(apuesta);

        } else if (valorJugador == valorCrupier) {
            System.out.println("Empate.");
            empatar();

        } else if (valorJugador > valorCrupier) {
            System.out.println("¡Has ganado!");
            ganarApuesta(apuesta);
        } else {
            System.out.println("Has perdido.");
            perderApuesta(apuesta);

        }
        manoJugador.limpiarMano();
        manoCrupier.limpiarMano();
    }

    private void ganarApuesta(double apuesta) {
        jugadorBJ.setPlata(apuesta);
        Utilidades.imprimirSeparador();
        System.out.println("Has ganado $" + apuesta*2);
        Utilidades.imprimirSeparador();
    }

    private void perderApuesta(double apuesta) {
        jugadorBJ.setPlata(-apuesta);
        Utilidades.imprimirSeparador();
        System.out.println("Has perdido $" + apuesta + ".");
        Utilidades.imprimirSeparador();
    }

    private void empatar() {
        Utilidades.imprimirSeparador();
        System.out.println("Tu apuesta de $" + apuesta + " te ha sido devuelta.");
        Utilidades.imprimirSeparador();
    }

    private void cargando(){
        System.out.println(
                """
        
                !  ██████╗ ██╗      █████╗  ██████╗██╗  ██╗     ██╗ █████╗  ██████╗██╗  ██╗
                !  ██╔══██╗██║     ██╔══██╗██╔════╝██║ ██╔╝     ██║██╔══██╗██╔════╝██║ ██╔╝
                !  ██████╔╝██║     ███████║██║     █████╔╝      ██║███████║██║     █████╔╝
                !  ██╔══██╗██║     ██╔══██║██║     ██╔═██╗ ██   ██║██╔══██║██║     ██╔═██╗
                !  ██████╔╝███████╗██║  ██║╚██████╗██║  ██╗╚█████╔╝██║  ██║╚██████╗██║  ██╗
                !  ╚═════╝ ╚══════╝╚═╝  ╚═╝ ╚═════╝╚═╝  ╚═╝ ╚════╝ ╚═╝  ╚═╝ ╚═════╝╚═╝  ╚═╝
                
        
                !  ┓ ┏┓┏┓┳┓┳┳┓┏┓
                !  ┃ ┃┃┣┫┃┃┃┃┃┃┓
                !  ┗┛┗┛┛┗┻┛┻┛┗┗┛ ♦♦♦
   
                """
        );

        Utilidades.pausar(2000);
        Utilidades.limpiarPantalla();

    }

    private void blackJackWins(){
        System.out.println(
                """
        
                !  ██████╗ ██╗      █████╗  ██████╗██╗  ██╗     ██╗ █████╗  ██████╗██╗  ██╗
                !  ██╔══██╗██║     ██╔══██╗██╔════╝██║ ██╔╝     ██║██╔══██╗██╔════╝██║ ██╔╝
                !  ██████╔╝██║     ███████║██║     █████╔╝      ██║███████║██║     █████╔╝
                !  ██╔══██╗██║     ██╔══██║██║     ██╔═██╗ ██   ██║██╔══██║██║     ██╔═██╗
                !  ██████╔╝███████╗██║  ██║╚██████╗██║  ██╗╚█████╔╝██║  ██║╚██████╗██║  ██╗
                !  ╚═════╝ ╚══════╝╚═╝  ╚═╝ ╚═════╝╚═╝  ╚═╝ ╚════╝ ╚═╝  ╚═╝ ╚═════╝╚═╝  ╚═╝  
                """
        );

        Utilidades.pausar(1000);

    }


}