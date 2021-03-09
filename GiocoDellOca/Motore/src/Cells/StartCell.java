/**
 * Contains: Cell Interface, StartCell Class, RegularCell Class, DoubleRollCell Class, GoBackCell Class, GoForwardCell Class, StopCell Class, TeleportCell Class.
 */
package Cells;

import Player.Player;

import java.util.LinkedList;
import java.util.List;

/**
 * The class which represents the StartCell object.
 */
public class StartCell implements Cell {
    /**
     * The index of the cell.
     */
    private int index;

    /**
     * The player dealing with the cell.
     */
    private List<Player> players;

    /**
     * The description of the cell.
     */
    private String description;

    /**
     * Constructor of the start cell.
     *
     * @param cellIndex the index of the cell
     */
    public StartCell(int cellIndex) {
        this.index = cellIndex;
        this.players = new LinkedList<Player>();
        this.description = "The cell" + " " + this.getIndex() + " " + "is empty";
    }

    /**
     * Returns the index of this cell.
     *
     * @return the index of the cell
     */
    @Override
    public int getIndex() {
        return this.index;
    }

    /**
     * Returns the index of the cell in which the turn of the player terminates.
     * Returns 0 which is the index of the start cell.
     *
     * @param diceRoll the result of the dice roll when the player reaches this cell
     * @return the index of the destination cell
     */
    @Override
    public int handleMove(int diceRoll) {
        return this.index;
    }

    /**
     * Creates and returns a string that later is printed in the output file.
     * Covers cases when a player arrives to specific cell having rolled the dice
     * and its result does not exceed the length of the board.
     *
     * @param player the player arriving to the cell
     * @return the animation string for the output file
     */
    @Override
    public String getPrimaryAnimationWithin(Player player) {
        String animationOutput = "";
        animationOutput += "SPOSTA";
        for (int i = player.getCell().getIndex() + 1; i <= this.getIndex(); i++) {
            animationOutput += " ";
            animationOutput += i;
        }
        animationOutput += "\n";
        return animationOutput;
    }

    /**
     * Creates and returns a string that later is printed in the output file.
     * Covers cases when a player arrives to specific cell having rolled the dice
     * and its result exceeds the length of the board.
     *
     * @param player the player arriving to the cell
     * @param boardSize the size of the game's board
     * @return the animation string for the output file
     */
    @Override
    public String getPrimaryAnimationOut(Player player, int boardSize) {
        String animationOutput = "";
        animationOutput += "SPOSTA";
        for (int i = player.getCell().getIndex() + 1; i <= boardSize; i++) {
            animationOutput += " ";
            animationOutput += i;
        }
        for (int i = boardSize - 1; i >= this.getIndex(); i--) {
            animationOutput += " ";
            animationOutput += i;
        }
        animationOutput += "\n";
        return animationOutput;
    }

    /**
     * Creates and returns a string that later is printed in the output file.
     * Returns cells' descriptions and animations strings for non-regular cells.
     * Covers cases when a player arrives to another cell being delegated from the specific cell.
     * For the start cell there is no special action.
     *
     * @param player the player arriving to the cell
     * @param diceRoll the result of the dice roll when the player reaches this cell
     * @return the animation string for the output file
     */
    @Override
    public String getSecondaryAnimation(Player player, int diceRoll) {
        String animationOutput = "";
        animationOutput += "MESSAGGIO" + " " + this.getDescription();
        animationOutput += "\n";
        return animationOutput;
    }

    /**
     * Gets the players.
     *
     * @return the players currently staying in the cell
     */
    @Override
    public List<Player> getPlayers() {
        return this.players;
    }

    /**
     * Sets the player in this cell.
     * Since it is the start cell, all the players are set in this cell at the beginning.
     *
     * @param player the player arriving to the cell
     */
    @Override
    public void setPlayer(Player player) {
        if(!this.getPlayers().contains(player)) {
            this.getPlayers().add(player);
            player.setCell(this);
        }
    }

    /**
     * Returns description of the cell.
     *
     * @return the cell's description
     */
    @Override
    public String getDescription() {
        return description;
    }
}