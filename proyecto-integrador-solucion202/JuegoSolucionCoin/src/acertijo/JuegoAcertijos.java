package acertijo;

import solucionCoin.Jugador;
import java.util.Scanner;

public class JuegoAcertijos {
    private int apuesta;
    private int opcionElegida;
    int contadorRespuestas = 0;
    private Jugador jugadorAC;

    Scanner scanner = new Scanner(System.in);

    // Constructor para inicializar el saldo
    public JuegoAcertijos(Jugador jugador) {
        this.jugadorAC = jugador;
    }

    // Menu inicial
    public void inicio(){
        System.out.println("\n************** BIENVENIDO/A AL JUEGO **************\n");
        System.out.println(
                """
                 
                         ███████╗ ███████╗ ███████║ ███████╗ ████████║ ██║ █████████║ ███████║ ███████╗
                         ██╔══██║ ██╔════╝ ██╔════╝ ██╔══██║    ██╔══╝ ██║ ╚════██╔═╝ ██║  ██║ ██╔════╝
                         ███████║ ██║      ███████║ █████══╝    ██║    ██║      ██║   ██║  ██║ ███████╗
                         ██║  ██║ ██║      ██╔════╝ ██╔═██═╗    ██║    ██║  █║  ██║   ██║  ██║ ╚════██║
                         ██║  ██║ ███████╗ ███████╗ ██║ ╚██║    ██║    ██║  ██████║   ███████║ ███████║
                         ╚═╝  ╚═╝ ╚══════╝ ╚══════╝ ╚═╝  ╚═╝    ╚═╝    ╚═╝  ╚═════╝   ╚══════╝ ╚══════╝
                 """);

        while(jugadorAC.getPlata() < 100){
            System.out.print("\nPara jugar este juego la apuesta minima es de 100): ");
            System.out.println("\n¿Cuánta plata desea ingresar?: ");
            jugadorAC.setPlata(scanner.nextDouble());
            scanner.nextLine();
        }
        jugarRonda();
        }

    // Método para jugar una ronda
    public void jugarRonda() {

        // Pedir la apuesta
        do {
            System.out.print("\nLa apuesta no puede ser menos de 100 ni superior a su saldo: ");
            System.out.println("\nIngrese su apuesta (mínimo 100 de plata)");
            apuesta = scanner.nextInt();
        } while (apuesta < 100 || apuesta > jugadorAC.getPlata());

        // Generar acertijos
        generarAcertijo();

        // Calcular resultados e imprimirlos
        Calcularresultados();
    }
    // Método para generar un acertijo aleatorio
    private void generarAcertijo() {
        String[] acertijo = new String[4];
        String[] opcion = new String[4];

        System.out.println("\n¡¡¡¡ COMIENZA EL JUEGO !!!!");
        acertijo[0] = "\nMilitar importante que residió en Mendoza entre 1814 y 1816 ocupando un cargo importante";
        acertijo[1] = "\nLíder político argentino que gobernó en la década del 70 luego de volver del exilio";
        acertijo[2] = "\nPersonaje histórico al que se le asigna la creación de nuestra bandera";
        acertijo[3] = "\nLider político de la primera democracia ampliada en Argentina, cuyo apodo era 'El peludo'";

        opcion[0] = "\n1. San Martin\n2. Juan Domingo Perón\n3. Hipolito Yrigoyen\n4. Manuel Belgrano";
        opcion[1] = "\n1. Manuel Belgrano\n2. San martin\n3. Hipolito Yrigoyen\n4. Juan Domingo Perón";
        opcion[2] = "\n1. San Martin\n2. Manuel Belgrano\n3. Juan Domingo Perón\n4. Hipolito Yrigoyen";
        opcion[3] = "\n1. San Martin\n2. Juan Domingo Peron\n3. Manuel Belgrano\n4. Hipolito Yrigoyen";

        do {
            System.out.println(acertijo[0]);
            System.out.println(opcion[0]);
            System.out.print("\nDigita el número de la opción que elijas: ");
            opcionElegida = scanner.nextInt();
            if (opcionElegida == 1) {
                System.out.println("\nCorrecto");
                ++ contadorRespuestas;
            }
            else{
                System.out.println("\nIncorrecto");
            }
        } while (opcionElegida <= 0 || opcionElegida > 4);

        do {
            System.out.println(acertijo[1]);
            System.out.println(opcion[1]);
            System.out.print("\nDigita el número de la opción que elijas: ");
            opcionElegida = scanner.nextInt();
            if (opcionElegida == 4) {
                System.out.println("\nCorrecto");
                ++ contadorRespuestas;
            }
            else{
                System.out.println("\nIncorrecto");
            }
        } while (opcionElegida <= 0 || opcionElegida > 4);

        do {
            System.out.println(acertijo[2]);
            System.out.println(opcion[2]);
            System.out.print("\nDigita el número de la opción que elijas: ");
            opcionElegida = scanner.nextInt();
            if (opcionElegida == 2) {
                System.out.println("\nCorrecto");
                ++ contadorRespuestas;
            }
            else{
                System.out.println("\nIncorrecto");
            }
        } while (opcionElegida <= 0 || opcionElegida > 4);

        do {
            System.out.println(acertijo[3]);
            System.out.println(opcion[3]);
            System.out.print("\nDigita el número de la opción que elijas: ");
            opcionElegida = scanner.nextInt();
            if (opcionElegida == 4) {
                System.out.println("\nCorrecto");
                ++ contadorRespuestas;
            }
            else{
                System.out.println("\nIncorrecto");
            }
        } while (opcionElegida <= 0 || opcionElegida > 4);
    }
    private void Calcularresultados(){
        if (contadorRespuestas == 4){
            System.out.println("\nFelicitaciones has respondido todo correctamente.\n Has ganado "+ apuesta + " de plata");
            jugadorAC.setPlata(+apuesta);
            System.out.println("\nTu saldo es: " + jugadorAC.getPlata());
            System.out.println("\nGRACIAS POR JUGAR A LOS ACERTIJOS");
        }
        else{
            System.out.println("\nNo has logrado responder todo correctamente.\n Has perdido " + apuesta + " de plata");
            jugadorAC.setPlata(-apuesta);
            System.out.println("\nTu saldo es: " + jugadorAC.getPlata());
            System.out.println("\nGRACIAS POR JUGAR A LOS ACERTIJOS");
        }
    }
}


