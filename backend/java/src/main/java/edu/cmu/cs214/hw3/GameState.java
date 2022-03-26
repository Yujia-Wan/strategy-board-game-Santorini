package edu.cmu.cs214.hw3;

import java.util.Arrays;

public class GameState {
    private static final int ROW = 5;
    private static final int COLUMN = 5;
    private final Cell[] cells;
    private final Player winner;
    private final String turn;

    public GameState(Cell[] cells, Player winner, String turn) {
        this.cells = cells;
        this.winner = winner;
        this.turn = turn;
    }

    public static GameState forGame(Game game) {
        Cell[] cells = getCells(game);
        Player winner = getWinner(game);
        String turn = getTurn(game);
        return new GameState(cells, winner, turn);
    }

    @Override
    public String toString() {
        if (this.winner == null) {
            return "{ \"cells\": " + Arrays.toString(this.cells) + "," +
                    "\"turn\": " + this.turn + "}";
        }
        return "{ \"cells\": " + Arrays.toString(this.cells) + "," +
                "\"turn\": " + this.turn + "," +
                "\"winner\": " + this.winner.getPlayerId() + "}";
    }

    public static Cell[] getCells(Game game) {
        Cell cells[] = new Cell[25];
        Grid grid = game.getGrid();
        for (int x = 0; x <= ROW; x++) {
            for (int y = 0; y <= COLUMN; y++) {
                String text = "";
                String clazz = "";
                String link = "";
                if (grid.getFieldHeight(x, y) == 0) {

                } else if (grid.getFieldHeight(x, y) == 1) {

                } else if (grid.getFieldHeight(x, y) == 2) {

                } else if (grid.getFieldHeight(x, y) == 3) {

                } else if (grid.getFieldHeight(x, y) == 4) {

                }
            }
        }
        return null;
    }

    public static Player getWinner(Game game) {
        return game.getWinner();
    }

    public static String getTurn(Game game) {
        return game.getCurrPlayer().getPlayerId();
    }
}

class Cell {
    private final String text;
    private final String clazz;
    private final String link;

    Cell(String text, String clazz, String link) {
        this.text = text;
        this.clazz = clazz;
        this.link = link;
    }

    public String getText() {
        return this.text;
    }

    public String getClazz() {
        return this.clazz;
    }

    public String getLink() {
        return this.link;
    }

    @Override
    public String toString() {
        return "{ \"text\": \"" + this.text + "\"," +
                " \"clazz\": \"" + this.clazz + "\"," +
                " \"link\": \"" + this.link + "\"}";
    }
}
