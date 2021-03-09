/**
 * Contains: Cell Interface, StartCell Class, RegularCell Class, DoubleRollCell Class, GoBackCell Class, GoForwardCell Class, StopCell Class, TeleportCell Class.
 */
package Cells;

import Player.Player;

import java.util.List;

/**
 * The interface which represents the Cell object.
 */
public interface Cell {

    /**
     * Returns the index of the cell.
     *
     * @return the index of the cell
     */
    int getIndex();

    /**
     * Returns the index of the cell in which the turn of the player terminates.
     *
     * @param diceRoll the result of the dice roll when the player reaches this cell
     * @return the index of the destination cell
     */
    int handleMove(int diceRoll);

    /**
     * Creates and returns a string that later is printed in the output file.
     * Covers cases when a player arrives to specific cell having rolled the dice
     * and its result does not exceed the length of the board.
     *
     * @param player the player arriving to the cell
     * @return the animation string for the output file
     */
    String getPrimaryAnimationWithin(Player player);

    /**
     * Creates and returns a string that later is printed in the output file.
     * Covers cases when a player arrives to specific cell having rolled the dice
     * and its result exceeds the length of the board.
     *
     * @param player the player arriving to the cell
     * @param boardSize the size of the game's board
     * @return the animation string for the output file
     */
    String getPrimaryAnimationOut(Player player, int boardSize);

    /**
     * Creates and returns a string that later is printed in the output file.
     * Covers cases when a player arrives to another cell being delegated from the specific cell.
     *
     * @param player the player arriving to the cell
     * @param diceRoll the result of the dice roll when the player reaches this cell
     * @return the animation string for the output file
     */
    String getSecondaryAnimation(Player player, int diceRoll);

    /**
     * Gets the players in the cell.
     *
     * @return the players currently staying in the cell
     */
    List<Player> getPlayers();

    /**
     * Sets the player in the cell.
     *
     * @param player the player arriving to the cell
     */
    void setPlayer(Player player);

    /**
     * Returns description of the cell.
     *
     * @return the cell's description
     */
    String getDescription();
}