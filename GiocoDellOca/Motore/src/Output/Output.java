/**
 * Contains: Output Class.
 */
package Output;

import Player.Player;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * The class which manages write operations to the output file.
 */
public class Output {

    /**
     * The output file.
     */
    private FileWriter gameOutputFile;

    /**
     * The Buffered Writer for the output file.
     */
    private BufferedWriter bufferedWriter;

    /**
     * The constructor of the output.
     * Creates the output file.
     *
     * @param textFile the output file
     */
    public Output(String textFile) {
        try {
            this.gameOutputFile = new FileWriter("./" + textFile);
            this.bufferedWriter = new BufferedWriter(this.gameOutputFile);
        } catch (IOException e) {
            System.out.println("Cannot create the output file.");
            System.exit(0);
        }
    }

    /**
     * Prints initial command to the output file.
     */
    public void begin() {
        try {
            this.bufferedWriter.write("OCA GIOCO");
            this.bufferedWriter.newLine();
        } catch (IOException e) {
            System.out.println("Cannot write the result to the output file.");
            System.exit(0);
        }
    }

    /**
     * Closes the output file.
     */
    public void finish() {
        try {
            this.bufferedWriter.close();
        } catch (IOException e) {
            System.out.println("Cannot write the result to the output file.");
            System.exit(0);
        }
    }

    /**
     * Prints the number of cells to the output file.
     *
     * @param numberOfCells the number of the cells in the game's board
     */
    public void printNumberOfCells(int numberOfCells) {
        try {
            this.bufferedWriter.write("CASELLE" + " " + numberOfCells);
            this.bufferedWriter.newLine();
        } catch (IOException e) {
            System.out.println("Cannot write the result to the output file.");
            System.exit(0);
        }
    }

    /**
     * Defines players in the output file.
     *
     * @param players the list of the players
     */
    public void printPlayers(List<Player> players) {
        for (Player player : players) {
            try {
                this.bufferedWriter.write("GIOCATORE"+ " " + player.getNumber() + " " + player.toString());
                this.bufferedWriter.newLine();
            } catch (IOException e) {
                System.out.println("Cannot write the result to the output file.");
                System.exit(0);
            }
        }
    }

    /**
     * Prints "end of the turn" message to the output file.
     */
    public void printEndTurn() {
        try {
            this.bufferedWriter.write("MESSAGGIO" + " " + "Fine turno");
            this.bufferedWriter.newLine();
        } catch (IOException e) {
            System.out.println("Cannot write the result to the output file.");
            System.exit(0);
        }
    }

    /**
     * Prints start of the turn command to the output file.
     *
     * @param player the object of the player who is performing the turn
     */
    public void printPlayerTurn(Player player) {
        try {
            this.bufferedWriter.write("TURNO" + " " + player.getNumber());
            this.bufferedWriter.newLine();
        } catch (IOException e) {
            System.out.println("Cannot write the result to the output file.");
            System.exit(0);
        }
    }

    /**
     * Prints dice roll result to the output file.
     *
     * @param diceRoll the result of the dice roll
     */
    public void printDiceRoll(int diceRoll) {
        try {
            this.bufferedWriter.write("DADO" + " " + diceRoll);
            this.bufferedWriter.newLine();
        } catch (IOException e) {
            System.out.println("Cannot write the result to the output file.");
            System.exit(0);
        }
    }

    /**
     * Prints player's move animation command to the output file.
     *
     * @param animation the animation's string originating in a cell
     */
    public void printAnimation(String animation) {
        try {
            this.bufferedWriter.write(animation);
        } catch (IOException e) {
            System.out.println("Cannot write the result to the output file.");
            System.exit(0);
        }
    }

    /**
     * Prints the final player's move animation command, and the victory command to the output file.
     *
     * @param player the object of the player who is performing the turn
     * @param afterRollIndex the index of the cell where the player arrived after the dice roll, in this case the final cell
     */
    public void printVictory(Player player, int afterRollIndex) {
        try {
            this.bufferedWriter.write("SPOSTA");
            for (int i = player.getCell().getIndex() + 1; i <= afterRollIndex; i++){
                this.bufferedWriter.write(" ");
                this.bufferedWriter.write(Integer.toString(i));
            }
            this.bufferedWriter.newLine();
            this.bufferedWriter.write("MESSAGGIO " + player.toString() + " " + "vince!");
            this.bufferedWriter.newLine();
            this.bufferedWriter.write("VINCE");
        } catch (IOException e) {
            System.out.println("Cannot write the result to the output file.");
            System.exit(0);
        }
    }
}