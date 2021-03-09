/**
 * Contains: Player Class.
 */
package Player;

import Cells.Cell;

/**
 * The class which represents the Player object.
 */
public class Player {

    /**
     * The current cell of the player.
     */
    private Cell currentCell;

    /**
     * The name of the player.
     */
    private String name;

    /**
     * The player's number.
     */
    private int number;

    /**
     * The number of turns that player has to wait.
     */
    private int numberOfTurnsToWait;

    /**
     * The number of players.
     */
    static public int numberOfPlayers = 0;

    /**
     * The constructor of the player.
     *
     * @param name the name of the player
     */
    public Player (String name) {
        this.numberOfPlayers++;
        this.name = name;
        this.number = numberOfPlayers;
        this.numberOfTurnsToWait = 0;
        this.currentCell = null;
    }

    /**
     * Returns true if player can move.
     *
     * @return the capability of performing move
     */
    public boolean canMove() {
        return this.getNumberOfTurnsToWait() == 0;
    }

    /**
     * Returns the name of the player.
     *
     * @return the name of the player
     */
    @Override
    public String toString() {
        return this.name;
    }

    /**
     * Returns the current cell of the player.
     *
     * @return the current position of the player
     */
    public Cell getCell() {
        return this.currentCell;
    }

    /**
     * Sets the cell of the player.
     *
     * @param cell the new position of the player
     */
    public void setCell(Cell cell) {
        this.currentCell = cell;
    }

    /**
     * Result of the dice roll.
     *
     * @return random integer number of the range [1,6]
     */
    public int diceRoll() {
        return ((int) (Math.random()*10000) % 6)+ 1;
    }

    /**
     * Gets the player's number.
     * @return the number of the player
     */
    public int getNumber() {
        return this.number;
    }

    /**
     * Sets the number of turns to wait.
     *
     * @param numberOfTurns the number of turns to wait
     */
    public void setNumberOfTurnsToWait(int numberOfTurns) {
        this.numberOfTurnsToWait = numberOfTurns;
    }

    /**
     * Gets the number of turns to wait.
     *
     * @return the number of turns to wait
     */
    private int getNumberOfTurnsToWait() {
        return this.numberOfTurnsToWait;
    }

    /**
     * Decreases the number of the turns that player has to wait before leaving the cell.
     */
    public void decreaseTurnsToWait() {
        this.setNumberOfTurnsToWait(this.getNumberOfTurnsToWait() - 1);
    }
}