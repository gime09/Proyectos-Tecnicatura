/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ruleta;

import solucionCoin.Juego;
import solucionCoin.Jugador;
import solucionCoin.Utilidades;
import java.util.Scanner;
import java.util.Random;

public class Ruleta {

    private double plata;
    private boolean apuesta;
    private Jugador jugadorRuleta;

    public Ruleta(Jugador jugadorRuleta){
        this.jugadorRuleta = jugadorRuleta;
    }

    public void apostar(){
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        // Inicializar juego
        System.out.println("Bienvenido al juego de la ruleta");
        System.out.println("Saldo :$" + jugadorRuleta.getPlata()); //prueba
        while(jugadorRuleta.getPlata()<=0){
            System.out.println("Ingrese el dinero para poder jugar: ");
            double plataIngresada = scanner.nextDouble();
            scanner.nextLine(); // limpiar el buffer
            if (plataIngresada <0){
                System.out.println("Ingrese un monto valido");
            }else {
                jugadorRuleta.setPlata(plataIngresada);
            }
        }


        apuesta = true;

        while (apuesta && jugadorRuleta.getPlata()> 0) {
            System.out.println("Menu de recomendaciones");
            System.out.println("   1. Apuesta simple");
            System.out.println("   2. Apuesta por color");
            System.out.println("   3. Apuesta por docena");
            System.out.println("   4. Salir");

            System.out.println("Elija una apuesta segun el menu");
            int apuesta1 = scanner.nextInt();

            int numeroAleatorio = random.nextInt(36) + 1;
            String colorSalio = obtenerColor(numeroAleatorio);
            int numeroApostado;

            switch (apuesta1) {
                case 1:
                    System.out.println("Usted eligio apuesta simple");
                    do {
                        System.out.println("Ingrese el numero que desea apostar entre 1 y 36: ");
                        numeroApostado = scanner.nextInt();
                        scanner.nextLine(); // limpiar el buffer
                    }while (numeroApostado >=36 && numeroApostado <0 );

                    System.out.println("Cuanto es el dinero a apostar?: ");
                    double plataApostado = scanner.nextDouble();
                    scanner.nextLine(); // limpiar el buffer

                    if (plataApostado <= jugadorRuleta.getPlata()) {
                        if (numeroApostado == numeroAleatorio) {
                            System.out.println("Usted gano!!!");
                            jugadorRuleta.setPlata(plataApostado*35);
                        } else {
                            System.out.println("Perdio!!");
                            jugadorRuleta.setPlata(-plataApostado);
                            apuesta = false;
                        }
                    } else {
                        System.out.println("No puede ingresar lo que no tiene");
                    }
                    break;

                case 2:
                    System.out.println("Usted eligio apuesta por color");
                    System.out.println("Ingrese el color que quiere elegir (rojo/negro/verde): ");
                    String colorApostado = scanner.next();
                    System.out.println("Cuanto es el dinero a apostar?: ");
                    plataApostado = scanner.nextDouble();

                    if (colorSalio.equals(colorApostado)) {
                        System.out.println("Usted gano!!!");
//                        plata += plataApostado;
                        jugadorRuleta.setPlata(plataApostado);

                    } else {
                        System.out.println("Perdio!!");
                        jugadorRuleta.setPlata(-plataApostado);
//                        plata -= plataApostado;
                    }
                    break;

                case 3:
                    System.out.println("Las docenas a apostar");
                    System.out.println("   1. Primera docena (1-12)");
                    System.out.println("   2. Segunda docena (13-24)");
                    System.out.println("   3. Tercera docena (25-36)");
                    System.out.println("   4. Volver al menu principal");

                    System.out.println("Elija la docena a apostar: ");
                    int docenaApostada = scanner.nextInt();

                    switch (docenaApostada) {
                        case 1:
                            System.out.println("Usted eligio la primera docena");
                            System.out.println("Cuanto es el dinero a apostar?: ");
                            plataApostado = scanner.nextDouble();

                            if (numeroAleatorio <= 12) {
                                System.out.println("Usted gano!!!");
//                                plata += plataApostado * 2;
                                jugadorRuleta.setPlata(plataApostado * 2);
                            } else {
                                System.out.println("Perdio!!");
//                                plata -= plataApostado;
                                jugadorRuleta.setPlata(-plataApostado);
                            }
                            break;

                        case 2:
                            System.out.println("Usted eligio la segunda docena");
                            System.out.println("Cuanto es el dinero a apostar?: ");
                            plataApostado = scanner.nextDouble();

                            if (numeroAleatorio >= 13 && numeroAleatorio <= 24) {
                                System.out.println("Usted gano!!!");
//                                plata += plataApostado * 2;
                                jugadorRuleta.setPlata(plataApostado*2);
                            } else {
                                System.out.println("Perdio!!");
//                                plata -= plataApostado;
                                jugadorRuleta.setPlata(-plataApostado);
                            }
                            break;

                        case 3:
                            System.out.println("Usted eligio la tercera docena");
                            System.out.println("Cuanto es el dinero a apostar?: ");
                            plataApostado = scanner.nextDouble();

                            if (numeroAleatorio >= 25 && numeroAleatorio <= 36) {
                                System.out.println("Usted gano!!!");
//                                plata += plataApostado * 2;
                                jugadorRuleta.setPlata(plataApostado*2);
                            } else {
                                System.out.println("Perdio!!");
//                                plata -= plataApostado;
                                jugadorRuleta.setPlata(-plataApostado);
                            }
                            break;

                        case 4:
                            System.out.println("##### Saliendo #####");
                            apuesta = false;
                            break;

                        default:
                            System.out.println("opcion no valida");
                    }
                    break;

                case 4:
                    System.out.println("Gracias por jugar");
                    apuesta = false;
                    break;

                default:
                    System.out.println("Opcion no valida");
            }

            System.out.println("Su plata actual es: $" + jugadorRuleta.getPlata());
        }
    }


    static String obtenerColor(int numero) {
        int[] rojos = {1, 3, 5, 7, 9, 12, 14, 16, 18, 21, 23, 25, 27, 30, 32, 34, 36};
        int[] negros = {2, 4, 6, 8, 10, 11, 13, 15, 17, 19, 20, 22, 24, 26, 28, 29, 31, 33, 35};

        for (int rojo : rojos) {
            if (numero == rojo) {
                return "rojo";
            }
        }

        for (int negro : negros) {
            if (numero == negro) {
                return "negro";
            }
        }

        return "verde";
    }
}

