/**
 * Contains: Board Class.
 */
package Board;

import Cells.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

/**
 * The class which represents the Board object and manages read operations from the input file.
 */
public class Board {

    /**
     * The number of the cells of the board.
     */
    private int numberOfCells;

    /**
     * The cells of the board.
     * There are 7 possible types of the cell:
     * StartCell, RegularCell, GoForwardCell, GoBackCell,TeleportCell, StopCell and DoubleThrowCell.
     */
    private Cell[] cells;

    /**
     * Loads the game board from the input text file.
     *
     * @param textFile the input file
     */
    public void loadBoard(String textFile) {
        try {
            FileReader boardInputFile = new FileReader("./" + textFile);
            Scanner boardInputFileReader = new Scanner(boardInputFile);
            String line = boardInputFileReader.nextLine();

            //Reads the input file and creates cells according to its content.
            interprateInputFile(boardInputFileReader, line);
        }
        //If the file cannot be found.
        catch (FileNotFoundException e) {
            System.out.println("Cannot find the input file.");
            System.exit(0);
        }
        //If any other problem occurs.
        catch (Exception e) {
            System.out.println("Cannot load the board from the input file.");
            System.exit(0);
        }
    }

    /**
     *  Reads the input file and creates cells according to its content.
     *
     * @param boardInputFileReader the Scanner object assigned to the input file
     * @param line one line of the input file
     */
    private void interprateInputFile(Scanner boardInputFileReader, String line) {

        //Checks the format of the input file.
        if (line.equals("OCA TAVOLA")) {
            while (boardInputFileReader.hasNextLine()) {
                line = boardInputFileReader.nextLine();

                //Loads the number of cells and creates default board.
                if (line.contains("CASELLE")) {
                    loadNumberOfCells(line);
                }

                //Skips all the comment lines.
                else if (line.contains("COMMENTO INIZIO")) {
                    skipsComments(boardInputFileReader, line);
                }

                //Creates a new cell of the type GoForwardCell.
                else if (line.contains("AVANTI")) {
                    createGoForwardCell(line);
                }

                //Creates a new cell of the type GoBackCell.
                else if (line.contains("INDIETRO")) {
                    createGoBackCell(line);
                }

                //Creates a new cell of the type DoubleThrowCell.
                else if (line.contains("DOPPIO")) {
                    createDoubleRollCell(line);
                }

                //Creates a new cell of the type StopCell.
                else if (line.contains("FERMO")) {
                    createStopCell(line);
                }

                //Creates a new cell of the type TeleportCell.
                else if (line.contains("VAI")) {
                    createTeleportCell(line);
                }
            }
        }
        //If the format of the input file is incorrect.
        else {
            System.out.println("Wrong input file format.");
            System.exit(0);
        }
    }

    /**
     * Creates a new cell of the type TeleportCell.
     *
     * @param line one line of the input file
     */
    private void createTeleportCell(String line) {
        String[] splitLine = line.split(" ");
        int index = Integer.parseInt(splitLine[0]);
        int destinationIndex = Integer.parseInt(splitLine[2]);
        String description = getDescription(splitLine, 3);
        this.cells[index] = new TeleportCell(index, destinationIndex, description);
    }

    /**
     * Creates a new cell of the type StopCell.
     *
     * @param line one line of the input file
     */
    private void createStopCell(String line) {
        String[] splitLine = line.split(" ");
        int index = Integer.parseInt(splitLine[0]);
        int turnsToWait = Integer.parseInt(splitLine[2]);
        String description = getDescription(splitLine, 3);
        this.cells[index] = new StopCell(index, turnsToWait, description);
    }

    /**
     * Creates a new cell of the type DoubleThrowCell.
     *
     * @param line one line of the input file
     */
    private void createDoubleRollCell(String line) {
        String[] splitLine = line.split(" ");
        int index = Integer.parseInt(splitLine[0]);
        String description = getDescription(splitLine, 2);
        this.cells[index] = new DoubleRollCell(index, description);
    }

    /**
     * Creates a new cell of the type GoBackCell.
     *
     * @param line one line of the input file
     */
    private void createGoBackCell(String line) {
        String[] splitLine = line.split(" ");
        int index = Integer.parseInt(splitLine[0]);
        int cellsBack = Integer.parseInt(splitLine[2]);
        String description = getDescription(splitLine, 3);
        this.cells[index] = new GoBackCell(index, cellsBack, description);
    }

    /**
     * Creates a new cell of the type GoForwardCell.
     *
     * @param line one line of the input file
     */
    private void createGoForwardCell(String line) {
        String[] splitLine = line.split(" ");
        int index = Integer.parseInt(splitLine[0]);
        int cellsForward = Integer.parseInt(splitLine[2]);
        String description = getDescription(splitLine, 3);
        this.cells[index] = new GoForwardCell(index, cellsForward, description);
    }

    /**
     * Skips all the comment lines in the input file.
     *
     * @param boardInputFileReader the Scanner object assigned to the input file
     * @param line one line of the input file
     */
    private void skipsComments(Scanner boardInputFileReader, String line) {
        while (!line.contains("COMMENTO FINE")) {
            line = boardInputFileReader.nextLine();
        }
    }

    /**
     * Gets description of the cell from the input file.
     *
     * @param splitLine one line of the input file as an array
     * @param offset the position of the start of the cell's description
     * @return the cell's description
     */
    private String getDescription(String[] splitLine, int offset) {
        String description = "";
        for (int i = offset; i < splitLine.length; ++i) {
            if (i != splitLine.length - 1) {
                description += splitLine[i] + " ";
            }
            else {
                description += splitLine[i];
            }
        }
        return description;
    }

    /**
     * Loads the number of the cells from the input file and calls the function responsible for creating the default board.
     *
     * @param line one line of the input file
     */
    private void loadNumberOfCells(String line) {
        String[] splitLine = line.split(" ");
        this.numberOfCells = Integer.parseInt(splitLine[1]);
        createDefaultBoard();
    }

    /**
     * Creates the default board which is the basis for the created board.
     */
    private void createDefaultBoard() {
        this.cells = new Cell[numberOfCells + 1];
        this.cells[0] = new StartCell(0);
        for (int i =1; i < numberOfCells + 1; ++i) {
            this.cells[i] = new RegularCell(i);
        }
    }

    /**
     * Gets all the cells of the board.
     *
     * @return the array of the board's cells
     */
    public Cell[] getCells() {
        return this.cells;
    }

    /**
     * Gets the cell for the given index.
     *
     * @param index the index of the cell
     * @return the cell assigned to the index
     */
    public Cell getCell(int index) {
        return this.cells[index];
    }

    /**
     * Gets the number of cells of the board.
     *
     * @return the number of the cells of the board
     */
    public int getNumberOfCells() {
        return this.numberOfCells;
    }
}