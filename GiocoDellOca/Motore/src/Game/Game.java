/**
 * Contains: Game Class.
 */
package Game;

import Board.Board;
import Cells.Cell;
import Cells.RegularCell;
import Cells.StopCell;
import Output.Output;
import Player.Player;

import java.util.LinkedList;
import java.util.List;

/**
 * The class which manages whole gameplay.
 */
public class Game {

    /**
     * The list of the players - by default 2.
     */
    private List<Player> players;

    /**
     * The game's board.
     */
    private Board board;

    /**
     * The board's size.
     */
    private int boardSize;

    /**
     * The status of the gameplay.
     */
    private boolean isGameFinished;

    /**
     * The Output object to create the output file.
     */
    private Output animation;

    /**
     * Constructor of the Game.
     *
     * @param inputFile the input file
     * @param outputFile the output file
     */
    public Game(String inputFile, String outputFile) {
        this.board = new Board();
        //Loads the board from the input file.
        this.board.loadBoard(inputFile);
        //Creates the output file.
        this.animation = new Output(outputFile);
        this.boardSize = board.getNumberOfCells() + 1;
        this.players = new LinkedList<Player>();
        this.isGameFinished = false;
    }

    /**
     * Checks the status of the game.
     *
     * @return the status of the gameplay
     */
    public boolean checkTheGameplayStatus() {
        return this.isGameFinished;
    }

    /**
     * Sets the status of the game.
     *
     * @param isGameFinished the status of the gameplay
     */
    public void setTheGameplayStatus(boolean isGameFinished) {
        this.isGameFinished = isGameFinished;
    }

    /**
     * Gets the current game board.
     *
     * @return the game's board
     */
    public Board getBoard() {
        return this.board;
    }

    /**
     * Adds a player to the game and assigns their position to the start cell.
     *
     * @param newPlayer the object of the player to be added
     */
    private void addPlayer(Player newPlayer) {
        this.players.add(newPlayer);
        this.getBoard().getCells()[0].setPlayer(newPlayer);
    }

    /**
     * Simulates one round of the gameplay.
     * Simulates a turn for each player in the player's list.
     */
    private void nextRound() {
        //Next round.
        oneTurn:
        for (Player player : this.players) {
            this.animation.printPlayerTurn(player);
            //Next turn for each player.
            //Stops after victory.
            if (nextTurn(player)) {
                break oneTurn;
            }
            this.animation.printEndTurn();
        }
    }

    /**
     * Simulates one turn of the round for each player.
     * Simulates the roll of the dice and executes moves according to its result.
     * Checks whether the game is finished.
     *
     * @param player the object of the player who is performing the turn
     * @return the signal for the nextRound function to continue or stop the loop
     */
    private boolean nextTurn(Player player) {
        int playerNumber = player.getNumber();
        //Checks if the player can move.
        if (player.canMove()) {
            //Rolls the dice.
            int dice = player.diceRoll();
            //Prints the dice roll result to the output file.
            this.animation.printDiceRoll(dice);
            //Execute the turn.
            //The index of the player before the turn.
            int beforeRollIndex = player.getCell().getIndex();
            //The index of the player after the roll of the dice.
            int afterRollIndex = beforeRollIndex + dice;
            //The index at the end of the turn.
            int finalIndex;
            //If the player is not out of board.
            if (afterRollIndex < boardSize) {
                finalIndex = executePlayerTurnWithin(player, dice, afterRollIndex);
            }
            //If the player is out of board.
            else if (afterRollIndex > boardSize) {
                finalIndex = executePlayerTurnOut(player, dice, beforeRollIndex);
            }
            //If the player has won.
            else {
                this.setTheGameplayStatus(true);
                this.animation.printVictory(player, afterRollIndex);
                return true;
            }
            //Assigns the player to the final cell.
            Cell finalCell = this.getBoard().getCell(finalIndex);
            player.setCell(finalCell);
            //If player entered the stop cell.
            if (finalCell instanceof StopCell) {
                player.setNumberOfTurnsToWait(((StopCell) finalCell).getNumberOfTurnsToWait());
            }
        }
        else {
            //Decrease turns to wait.
            player.decreaseTurnsToWait();
        }
        return false;
    }

    /**
     * Executes the player's move and print its result when the dice result leads the player out of the board.
     *
     * @param player the object of the player who is performing the turn
     * @param dice the result of the dice roll
     * @param beforeRollIndex the index of the cell where the player started the turn
     * @return the index of the destination cell
     */
    private int executePlayerTurnOut(Player player, int dice, int beforeRollIndex) {
        int auxIndex;
        int finalIndex;
        auxIndex = boardSize - (dice - (boardSize - beforeRollIndex));
        finalIndex = this.getBoard().getCell(auxIndex).handleMove(dice);
        //Prints the player's move animation based on the dice roll result.
        String cellPrimaryAnimation = this.getBoard().getCell(auxIndex).getPrimaryAnimationOut(player, boardSize);
        this.animation.printAnimation(cellPrimaryAnimation);
        //Prints the player's move animation based on the cell of arrival.
        String cellSecondaryAnimation = this.getBoard().getCell(auxIndex).getSecondaryAnimation(player, dice);
        this.animation.printAnimation(cellSecondaryAnimation);
        //If the destination cell is not a regular cell or stop cell.
        if (auxIndex != finalIndex) {
            cellSecondaryAnimation = this.getBoard().getCell(finalIndex).getSecondaryAnimation(player, dice);
            this.animation.printAnimation(cellSecondaryAnimation);
        }
        //Repeats as long as the destination cell is not a regular cell or stop cell.
        while (!(this.getBoard().getCell(finalIndex) instanceof RegularCell) && !(this.getBoard().getCell(finalIndex) instanceof StopCell)) {
            finalIndex = this.getBoard().getCell(finalIndex).handleMove(dice);
            cellSecondaryAnimation = this.getBoard().getCell(finalIndex).getSecondaryAnimation(player, dice);
            this.animation.printAnimation(cellSecondaryAnimation);
        }
        return finalIndex;
    }

    /**
     * Executes the player's move and print its result when the dice result does not lead the player out of the board.
     *
     * @param player the object of the player who is performing the turn
     * @param dice the result of the dice roll
     * @param afterRollIndex the index of the cell where the player arrived after the dice roll
     * @return the index of the destination cell
     */
    private int executePlayerTurnWithin(Player player, int dice, int afterRollIndex) {
        int finalIndex;
        finalIndex = this.getBoard().getCell(afterRollIndex).handleMove(dice);
        //Prints the player's move animation based on the dice roll result.
        String cellPrimaryAnimation = this.getBoard().getCell(afterRollIndex).getPrimaryAnimationWithin(player);
        this.animation.printAnimation(cellPrimaryAnimation);
        //Prints the player's move animation based on the cell of arrival.
        String cellSecondaryAnimation = this.getBoard().getCell(afterRollIndex).getSecondaryAnimation(player, dice);
        this.animation.printAnimation(cellSecondaryAnimation);
        //If the destination cell is not a regular cell or stop cell.
        if (afterRollIndex != finalIndex) {
            cellSecondaryAnimation = this.getBoard().getCell(finalIndex).getSecondaryAnimation(player, dice);
            this.animation.printAnimation(cellSecondaryAnimation);
        }
        //Repeats as long as the destination cell is not a regular cell or stop cell.
        while (!(this.getBoard().getCell(finalIndex) instanceof RegularCell) && !(this.getBoard().getCell(finalIndex) instanceof StopCell)) {
            finalIndex = this.getBoard().getCell(finalIndex).handleMove(dice);
            cellSecondaryAnimation = this.getBoard().getCell(finalIndex).getSecondaryAnimation(player, dice);
            this.animation.printAnimation(cellSecondaryAnimation);
        }
        return finalIndex;
    }

    /**
     * Adds the players to the game.
     *
     * @param args the main function's arguments from the command line
     */
    public void addPlayers(String[] args) {
        List<String> names = new LinkedList<>();
        for (int i = 2; i < args.length; i++) {
            names.add(args[i]);
        }
        for (String name : names) {
            Player newPlayer = new Player(name);
            this.addPlayer(newPlayer);
        }
    }

    /**
     * Starts the game.
     */
    public void playGame() {
        this.animation.begin();
        this.animation.printNumberOfCells(this.boardSize - 1);
        this.animation.printPlayers(this.players);
        while (!this.checkTheGameplayStatus()) {
            this.nextRound();
        }
        this.animation.finish();
    }
}