package app;

import api.Structures.java.exceptions.EmptyCollectionException;
import api.Structures.java.exceptions.MapException;
import api.Structures.java.exceptions.UnknownPathException;
import api.game.Game;
import java.util.Locale;
import java.util.Scanner;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
/**
 *
 * @author Ricar
 */
public class Index {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws EmptyCollectionException, MapException, UnknownPathException {
        int menuOption;
        Scanner scanner = new Scanner(System.in).useLocale(Locale.US);
        Game game = new Game();
        System.out.println("----Bem Vindo----");
        do {
            System.out.println("| 1 -> Gerar Mapa | 2 -> Importar Mapa | 3 -> Sair |");
            menuOption = scanner.nextInt();
            if (menuOption == 3) {
                System.exit(0);
            }
            game.inputMenu(menuOption);
        } while (menuOption < 1 || menuOption > 3);
        do {
            System.out.println(
                    "| 1 -> Jogar | 2 -> Gerar Novo Mapa | 3 -> Exportar Mapa | 4 -> Importar Mapa |  5 -> Sair |");
            menuOption = scanner.nextInt();
            game.inputGame(menuOption);
        } while (menuOption != 5);
        System.exit(0);

    }
}
