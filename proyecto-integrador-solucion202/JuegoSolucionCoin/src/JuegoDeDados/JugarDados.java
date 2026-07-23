package JuegoDeDados;

import solucionCoin.Jugador;
import solucionCoin.Juego;
import java.util.Scanner;

public class JugarDados extends Juego {
    private final Jugador jugador;
    private final Dados dados;
    private int apuesta;
    private final Scanner scanner = new Scanner(System.in);
    private boolean apuestaValida;

    public JugarDados(Jugador jugador) {
        this.jugador = jugador;
        this.dados = new Dados();
        this.apuesta = 0;
        this.apuestaValida = false;
    }

    private void imprimirSaldo() {
        System.out.printf("Tu saldo es: $%d%n", (int) jugador.getPlata());
    }

    // Apostar
    @Override
    public void apostar() {
        imprimirSaldo();
        System.out.print("Ingresa tu apuesta: ");
        apuesta = scanner.nextInt();

        if (apuesta <= 0) {
            System.out.println("La apuesta debe ser mayor que cero.");
            apuestaValida = false;
        } else if (apuesta > jugador.getPlata()) {
            System.out.println("No tienes suficiente saldo para esa apuesta.");
            apuestaValida = false;
        } else {
            jugador.setPlata(-apuesta);
            System.out.printf("Apuesta aceptada. Nuevo saldo: $%d%n", (int) jugador.getPlata());
            apuestaValida = true;
        }
    }

    private void jugarRonda() {
        boolean seguirJugando;
        do {
            apostar();
            if (!apuestaValida) {
                System.out.println("No se puede jugar la ronda sin una apuesta válida.");
                break;
            }

            int dado1 = (int) (Math.random() * 6) + 1;
            int dado2 = (int) (Math.random() * 6) + 1;
            int resultado = dado1 + dado2;

            System.out.printf("Has sacado un %d y un %d, total: %d%n", dado1, dado2, resultado);
            dados.mostrarDado(dado1);
            dados.mostrarDado(dado2);

            if (resultado == 7) {
                System.out.println("¡Recuperas tu apuesta! El total de los dados es 7.");
                jugador.setPlata(apuesta);
            } else if (resultado >= 8) {
                System.out.println("¡Felicidades! Has ganado el doble de tu apuesta.");
                jugador.setPlata(apuesta * 2);
            } else {
                System.out.println("Lo siento, has perdido.");
            }

            imprimirSaldo();

            if (jugador.getPlata() > 0) {
                System.out.print("¿Quieres seguir jugando? (presione 1 para seguir jugando, cualquier otro número para Salir): ");
                int respuesta = scanner.nextInt();
                seguirJugando = respuesta == 1;
            } else {
                System.out.println("No tienes suficiente saldo para seguir jugando.");
                seguirJugando = false;
            }

        } while (seguirJugando);
    }

    public void iniciarMenu() {
        System.out.print("Bienvenido al Juego de Dados");
        int opcion;
        do {
            System.out.println("\n...::: JUEGO DE DADOS :::...");
            System.out.println("""
                    1 - Jugar
                    2 - Ingresar Dinero
                    3 - Consultar Saldo
                    4 - Salir
                    """);

            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextInt();

            switch (opcion) {
                case 1 -> {
                    if (jugador.getPlata() > 0) {
                        jugarRonda();
                    } else {
                        System.out.println("Debes ingresar dinero para poder jugar.");
                    }
                }
                case 2 -> {
                    System.out.print("Ingresa el monto que deseas agregar a tu saldo: ");
                    double ingreso = scanner.nextDouble();
                    if (ingreso > 0) {
                        jugador.setPlata(ingreso);
                        System.out.printf("Nuevo saldo: $%d%n", (int) jugador.getPlata());
                    } else {
                        System.out.println("Debes ingresar un monto válido.");
                    }
                }
                case 3 -> imprimirSaldo();
                case 4 -> System.out.println("Gracias por jugar a los Dados. ¡Hasta luego!");
                default -> System.out.println("Opción no válida.");
            }
        } while (opcion != 4);
    }

    @Override
    public void iniciarJuego() {
        iniciarMenu();
    }
}
