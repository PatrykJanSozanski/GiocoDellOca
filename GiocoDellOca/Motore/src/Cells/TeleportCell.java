/**
 * Contains: Cell Interface, StartCell Class, RegularCell Class, DoubleRollCell Class, GoBackCell Class, GoForwardCell Class, StopCell Class, TeleportCell Class.
 */
package Cells;

import Player.Player;

import java.util.List;

/**
 * The class which represents the TeleportCell object.
 */
public class TeleportCell implements Cell {

    /**
     * The index of the cell.
     */
    private int index;

    /**
     * The index of the destination cell.
     */
    private int destinationIndex;

    /**
     * The description of the cell.
     */
    private String description;

    /**
     * The player dealing with the cell.
     */
    private List<Player> players;

    /**
     * Constructor of the teleport cell.
     *
     * @param cellIndex the index of the cell
     * @param destinationIndex the index of the cell to which player has to go from this cell
     * @param cellDescription the description of the cell
     */
    public TeleportCell(int cellIndex, int destinationIndex, String cellDescription) {
        this.index = cellIndex;
        this.destinationIndex = destinationIndex;
        this.description = cellDescription;
        this.players = null;
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
     * For regular cells the returned value is not dependent from the result of the diceRoll method
     * and is equal to the index of the destination cell.
     *
     * @param diceRoll the result of the dice roll when the player reaches this cell
     * @return the index of the destination cell
     */
    @Override
    public int handleMove(int diceRoll) {
        return this.getDestinationIndex();
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
        for (int i = player.getCell().getIndex() + 1; i <= this.getIndex(); i++){
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
     * For teleport cells the returned value is not dependent from the result of the diceRoll.
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
        animationOutput += "SPOSTA";
        animationOutput += " ";
        animationOutput += this.getDestinationIndex();
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
     *
     * @param player the player arriving to the cell
     */
    @Override
    public void setPlayer(Player player) {
        this.players.add(player);
    }

    /**
     * Returns description of the cell.
     *
     * @return the cell's description
     */
    @Override
    public String getDescription() {
        return this.description;
    }

    /**
     * Returns the index of the destination cell.
     *
     * @return the final index of the player arriving to the cell
     */
    public int getDestinationIndex() {
        return this.destinationIndex;
    }
}