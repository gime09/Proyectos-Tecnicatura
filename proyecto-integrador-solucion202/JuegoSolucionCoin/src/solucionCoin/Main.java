package solucionCoin;
import AdivinanzasAnimales.AdivinanzasAnimales;
import BlackJack.BlackJack;
import acertijo.JuegoAcertijos;
import JuegoDeDados.JugarDados;
import jackpot.Jackpot;
import ruleta.Ruleta;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Jugador jugador = new Jugador();
        Scanner scanner = new Scanner(System.in);
        titulo();
        int opcion;
        System.out.print("Por favor ingrese su nombre:");
        jugador.setNombre(scanner.nextLine());

        System.out.println("Bienvenido " + jugador.getNombre());
        do {
            System.out.println("\nSeleccione un juego: ");
            System.out.println("""
                1 - BlackJack
                2 - Ruleta
                3 - Juego De Dados
                4 - acertijos
                5 - adivinanzas De Animales
                6 - JackPot
                7 - Salir
                """);

            System.out.print("Seleccione un juego: ");
            opcion = Integer.parseInt(scanner.nextLine());

            switch (opcion) {
                case 1:
                    BlackJack blackJack = new BlackJack(jugador);
                    blackJack.iniciarJuego();
                    break;
                case 2:
                    Ruleta ruleta = new Ruleta(jugador);
                    ruleta.apostar();
                    break;
                case 3:
                    JugarDados JuegoDeDados = new JugarDados(jugador);
                    JuegoDeDados.iniciarJuego();
                    break;
                case 4:
                    JuegoAcertijos acertijos = new JuegoAcertijos(jugador);
                    acertijos.inicio();
                    break;
                case 5:
                    AdivinanzasAnimales adivinanzasAnimales = new AdivinanzasAnimales(jugador);
                    adivinanzasAnimales.jugar();

                    break;
                case 6:
                    Jackpot jackpot = new Jackpot();
                    jackpot.jugar(jugador);
                    break;
                case 7:
                    if (jugador.getPlata() > 0) {
                        System.out.println("Retirando  = $" + jugador.getPlata());
                    }
                    System.out.println("Adioss ¡Vuelva Pronto! ");
                    break;
                default:
                    System.out.println("Introduzca un valor permitido.");
            }
        } while (opcion != 7);

    }

    public static void titulo() {
        System.out.println(
                """
                 
                        !  ███████╗ ██████╗ ██╗     ██╗ ██████╗██╗ ██████╗ ███╗   ██╗ ██████╗ ██████╗ ██╗███╗   ██╗
                        !  ██╔════╝██╔═══██╗██║     ██║██╔════╝██║██╔═══██╗████╗  ██║██╔════╝██╔═══██╗██║████╗  ██║
                        !  ███████╗██║   ██║██║     ██║██║     ██║██║   ██║██╔██╗ ██║██║     ██║   ██║██║██╔██╗ ██║
                        !  ╚════██║██║   ██║██║     ██║██║     ██║██║   ██║██║╚██╗██║██║     ██║   ██║██║██║╚██╗██║
                        !  ███████║╚██████╔╝███████╗██║╚██████╗██║╚██████╔╝██║ ╚████║╚██████╗╚██████╔╝██║██║ ╚████║
                        !  ╚══════╝ ╚═════╝ ╚══════╝╚═╝ ╚═════╝╚═╝ ╚═════╝ ╚═╝  ╚═══╝ ╚═════╝ ╚═════╝ ╚═╝╚═╝  ╚═══╝
                 """);
        
    }

}
