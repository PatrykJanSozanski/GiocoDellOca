/**
 * Contains: Main Class.
 */
package Main;

import Game.Game;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * The class which is the entry point of the application.
 */
public class Main {
    /**
     * The main function.
     * Arguments to Main:
     * args[0] - the name of the input file
     * args[1] - the name of the output file
     * args[2...] - the names of the players
     *
     * Example of usage: java .../Main .../input.txt .../output.txt Name1 Name2 Name3 ...
     *
     * @param args the main function's arguments from the command line
     */
    public static void main(String[] args) {
        Game game = null;
        if(args.length >= 3) {
            game = getGameCommandLine(args);
            game.addPlayers(args);
            game.playGame();
        }
        else if(args.length == 2) {
            String[] gameArgs = getGameArgs(args, 2);
            game = getGamePlayers(gameArgs);
            game.addPlayers(gameArgs);
            game.playGame();
        }
        else {
            wrongParameters(args);
        }
    }

    /**
     * Creates a new array of the size specified by the number of players passed to the method.
     * Two first cells of the new array are equal to the parameters passed to the main function from the command line.
     *
     * @param args the main function's arguments from the command line
     * @param numberOfPlayers the number of players
     * @return the array of the game's parameters
     */
    private static String[] getGameArgs(String[] args, int numberOfPlayers) {
        String gameArgs[] = new String[2 + numberOfPlayers];
        gameArgs[0] = args[0];
        gameArgs[1] = args[1];
        return gameArgs;
    }

    /**
     * Prints an error message.
     *
     * @param args the main function's arguments from the command line
     */
    private static void wrongParameters(String[] args) {
        System.out.println("Incorrect parameters!");
        System.out.println("The number of parameters passed to the program is" + " " + args.length);
        System.out.println("The minimum number of parameters is 2");
        System.out.println("The minimum number of names is 0");
        System.out.println("Example of usage: java Main input.txt output.txt Name1 Name2 Name3 ...");
    }

    /**
     * Reads all the main function parameters from the command line and creates a new game for unspecified number of players.
     *
     * @param args the main function's arguments from the command line
     * @return the game object
     */
    private static Game getGameCommandLine(String[] args) {
        Game game;
        game = new Game(args[0], args[1]);
        System.out.println("Input file:" + " " + args[0]);
        System.out.println("Output file:" + " " + args[1]);
        System.out.println("Number of players:" + " " + (args.length - 2));
        System.out.println("Names:" + " ");
        for (int i = 2; i < args.length; i++) {
            System.out.println("    -> Player" + " " + (i - 1) + ":" + " " + args[i]);
        }
        return game;
    }

    /**
     * Reads the names of the input file and the output file.
     * Creates a new game asking for the players' names.
     * The number of the players depends on the size of the array gameArgs.
     *
     * @param gameArgs the array of the game's parameters
     * @return the game object
     */
    private static Game getGamePlayers(String[] gameArgs) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Game game;
        game = new Game(gameArgs[0], gameArgs[1]);
        System.out.println("Input file:" + " " + gameArgs[0]);
        System.out.println("Output file:" + " " + gameArgs[1]);
        System.out.println("Number of players:" + " " + (gameArgs.length - 2));
        System.out.println("Names:" + " ");
        //Asks for the players names.
        try {
            for (int i = 2; i < gameArgs.length; i++){
                gameArgs[i] = br.readLine();
            }
        } catch (Exception e) {
            System.out.println("Cannot load the names.");
            System.exit(0);
        }
        for (int i = 2; i < gameArgs.length; i++) {
            System.out.println("    -> Player" + " " + (i - 1) + ":" + " " + gameArgs[i]);
        }
        return game;
    }
}