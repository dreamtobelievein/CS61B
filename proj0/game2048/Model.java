package game2048;

import java.util.Formatter;
import java.util.Observable;


/** The state of a game of 2048.
 *  @author Jibing Yang
 */
public class Model extends Observable {
    /** Current contents of the board. */
    private Board board;
    /** Current score. */
    private int score;
    /** Maximum score so far.  Updated when game ends. */
    private int maxScore;
    /** True iff game is ended. */
    private boolean gameOver;

    /* Coordinate System: column C, row R of the board (where row 0,
     * column 0 is the lower-left corner of the board) will correspond
     * to board.tile(c, r).  Be careful! It works like (x, y) coordinates.
     */

    /** Largest piece value. */
    public static final int MAX_PIECE = 2048;

    /** A new 2048 game on a board of size SIZE with no pieces
     *  and score 0. */
    public Model(int size) {
        board = new Board(size);
        score = maxScore = 0;
        gameOver = false;
    }

    /** A new 2048 game where RAWVALUES contain the values of the tiles
     * (0 if null). VALUES is indexed by (row, col) with (0, 0) corresponding
     * to the bottom-left corner. Used for testing purposes. */
    public Model(int[][] rawValues, int score, int maxScore, boolean gameOver) {
        int size = rawValues.length;
        board = new Board(rawValues, score);
        this.score = score;
        this.maxScore = maxScore;
        this.gameOver = gameOver;
    }

    /** Return the current Tile at (COL, ROW), where 0 <= ROW < size(),
     *  0 <= COL < size(). Returns null if there is no tile there.
     *  Used for testing. Should be deprecated and removed.
     *  */
    public Tile tile(int col, int row) {
        return board.tile(col, row);
    }

    /** Return the number of squares on one side of the board.
     *  Used for testing. Should be deprecated and removed. */
    public int size() {
        return board.size();
    }

    /** Return true iff the game is over (there are no moves, or
     *  there is a tile with value 2048 on the board). */
    public boolean gameOver() {
        checkGameOver();
        if (gameOver) {
            maxScore = Math.max(score, maxScore);
        }
        return gameOver;
    }

    /** Return the current score. */
    public int score() {
        return score;
    }

    /** Return the current maximum game score (updated at end of game). */
    public int maxScore() {
        return maxScore;
    }

    /** Clear the board to empty and reset the score. */
    public void clear() {
        score = 0;
        gameOver = false;
        board.clear();
        setChanged();
    }

    /** Add TILE to the board. There must be no Tile currently at the
     *  same position. */
    public void addTile(Tile tile) {
        board.addTile(tile);
        checkGameOver();
        setChanged();
    }

    /** Tilt the board toward SIDE. Return true iff this changes the board.
     *
     * 1. If two Tile objects are adjacent in the direction of motion and have
     *    the same value, they are merged into one Tile of twice the original
     *    value and that new value is added to the score instance variable
     * 2. A tile that is the result of a merge will not merge again on that
     *    tilt. So each move, every tile will only ever be part of at most one
     *    merge (perhaps zero).
     * 3. When three adjacent tiles in the direction of motion have the same
     *    value, then the leading two tiles in the direction of motion merge,
     *    and the trailing tile does not.
     *
     *    The method works as follows: first use the board.setViewingPerspective(side)
     *    method to set the board as if it's facing north, then run the moveCol
     *    method on each column, finally, set the board in its initial perspective
     *    using board.setViewingPerspective(Side.NORTH).
     */
    public boolean tilt(Side side) {
        boolean changed;
        changed = false;

        board.setViewingPerspective(side);
        int size = board.size();
        for (int col = 0; col < size; col++) {
            changed = moveCol(changed, size, col);
        }
        board.setViewingPerspective(Side.NORTH);

        checkGameOver();
        if (changed) {
            setChanged();
        }
        return changed;
    }

    /** This method does the majority of work in tilt.
     *  On any given col, from up to bottom it tracks
     *  down each tile and moves them one by one to
     *  their supposed position. I use a boolean canMerge
     *  to indicate whether the current tile can merge
     *  with the last one. With that known, I can set
     *  the destination of this tile to be the position
     *  of the last tile or one row under it.
     *
     *  The instance variable score is updated during the
     *  run of this method, if merges happen.
     *
     *  Returns true if there's any change to the board.
     */
    private boolean moveCol(boolean changed, int size, int col) {
        int dest = size - 1;
        int row = findTileInRow(board, col, dest);
        boolean canMerge = false;
        while (row >= 0) {
            Tile t = board.tile(col, row);
            int thisValue = t.value();
            if (canMerge && board.tile(col, dest + 1).value() == thisValue) {
                dest++;
            }

            boolean mergeHappens = false;
            if (dest != row) {
                mergeHappens = board.move(col, dest, t);
                changed = true;
                if (mergeHappens) {
                    score += thisValue * 2;
                }
            }
            canMerge = !mergeHappens;
            dest--;
            row = findTileInRow(board, col, dest);
        }
        return changed;
    }

    /** Helper function to find the next tile, starting from position
     *  "row" and counting down to 0. Returns -1 if the method doesn't
     *  find one.
     */
    private static int findTileInRow(Board b, int col, int row) {
        while (row >= 0) {
            if (b.tile(col, row) != null) {
                return row;
            }
            row--;
        }
        return row;
    }

    /** Checks if the game is over and sets the gameOver variable
     *  appropriately.
     */
    private void checkGameOver() {
        gameOver = checkGameOver(board);
    }

    /** Determine whether game is over. */
    private static boolean checkGameOver(Board b) {
        return maxTileExists(b) || !atLeastOneMoveExists(b);
    }

    /** Returns true if at least one space on the Board is empty.
     *  Empty spaces are stored as null.
     */
    public static boolean emptySpaceExists(Board b) {
        int size = b.size();
        for (int col = 0; col < size; col++) {
            for (int row = 0; row < size; row++) {
                if (b.tile(col, row) == null) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns true if any tile is equal to the maximum valid value.
     * Maximum valid value is given by MAX_PIECE. Note that
     * given a Tile object t, we get its value with t.value().
     */
    public static boolean maxTileExists(Board b) {
        int size = b.size();
        for (int col = 0; col < size; col++) {
            for (int row = 0; row < size; row++) {
                Tile t = b.tile(col, row);
                /** Need to check the tile for non-empty value first.
                 *  Otherwise will get an error accessing its value. */
                if (t != null && t.value() == MAX_PIECE) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns true if there are any valid moves on the board.
     * There are two ways that there can be valid moves:
     * 1. There is at least one empty space on the board.
     * 2. There are two adjacent tiles with the same value.
     *
     * Use the emptySpaceExists(b) method above to check the
     * first condition. Write a new method mergeableTilesExist(b)
     * to check the second condition.
     */
    public static boolean atLeastOneMoveExists(Board b) {
        return emptySpaceExists(b) || mergeableTilesExist(b);
    }

    /**
     * Returns true if any two adjacent tiles with the same
     * value exists. Note that mergeableTilesExist(b) assumes
     * all the tiles are non-empty.
     *
     * To make this algorithm more efficient, do checks on
     * every other tile, instead of every tile. Because the
     * check cases for the leftover ones are already covered
     * by the ones adjacent to them. In this case, the chosen
     * tile has a property of col % 2 == row % 2.
     */
    private static boolean mergeableTilesExist(Board b) {
        int size = b.size();
        for (int col = 0; col < size; col++) {
            // Filter for (col % 2 == row % 2).
            int filter = col % 2;
            for (int row = 0; row < size; row++) {
                if (row % 2 == filter && adjacentTilesIdentical(b, col, row)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns true if any of the four tiles adjacent to this
     * one has the same value.
     */
    private static boolean adjacentTilesIdentical(Board b, int col, int row) {
        return inboundAndEqual(b, col, row, col - 1, row)
                || inboundAndEqual(b, col, row, col + 1, row)
                || inboundAndEqual(b, col, row, col, row - 1)
                || inboundAndEqual(b, col, row, col, row + 1);
    }

    /**
     * Returns true if the other tile is inbound and
     * has the same value.
     */
    private static boolean inboundAndEqual(Board b, int col, int row, int otherCol, int otherRow) {
        int size = b.size();
        if (otherCol < 0 || otherCol >= size) {
            return false;
        }
        if (otherRow < 0 || otherRow >= size) {
            return false;
        }
        return b.tile(col, row).value() == b.tile(otherCol, otherRow).value();
    }

    @Override
     /** Returns the model as a string, used for debugging. */
    public String toString() {
        Formatter out = new Formatter();
        out.format("%n[%n");
        for (int row = size() - 1; row >= 0; row -= 1) {
            for (int col = 0; col < size(); col += 1) {
                if (tile(col, row) == null) {
                    out.format("|    ");
                } else {
                    out.format("|%4d", tile(col, row).value());
                }
            }
            out.format("|%n");
        }
        String over = gameOver() ? "over" : "not over";
        out.format("] %d (max: %d) (game is %s) %n", score(), maxScore(), over);
        return out.toString();
    }

    @Override
    /** Returns whether two models are equal. */
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (getClass() != o.getClass()) {
            return false;
        } else {
            return toString().equals(o.toString());
        }
    }

    @Override
    /** Returns hash code of Model’s string. */
    public int hashCode() {
        return toString().hashCode();
    }
}
