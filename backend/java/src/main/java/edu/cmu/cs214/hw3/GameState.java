package edu.cmu.cs214.hw3;

import java.awt.Point;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class GameState {
    private static final int ROW = 5;
    private static final int COLUMN = 5;
    private static final int FIELD = 25;
    private static final int EMPTY = 0;
    private static final int ONE_LEVEL = 1;
    private static final int TWO_LEVEL = 2;
    private static final int THREE_LEVEL = 3;
    private static final int DOME = 4;
    private final boolean finishChooseCard;
    private final int playerACard;
    private final int playerBCard;
    private final Card[] cards;
    private final Player currPlayer;
    private final int currWorker;
    private final boolean finishInitPos;
    private final String action;
    private final String winner;
    private final Cell[] cells;

    public GameState(boolean finishChooseCard, int playerACard, int playerBCard, Card[] cards, Player currPlayer,
                     int currWorker, boolean finishInitPos, String action, String winner, Cell[] cells) {
        this.finishChooseCard = finishChooseCard;
        this.playerACard = playerACard;
        this.playerBCard = playerBCard;
        this.cards = cards;
        this.currPlayer = currPlayer;
        this.currWorker = currWorker;
        this.finishInitPos = finishInitPos;
        this.action = action;
        this.winner = winner;
        this.cells = cells;
    }

    /**
     * Retrieves game state for current action.
     *
     * @param game Current game.
     * @return Game state.
     */
    public static GameState forGame(Game game) {
        boolean finishChooseCard = getFinishChooseCard(game);
        int playerACard = getPlayerACard(game);
        int playerBCard = getPlayerBCard(game);
        Card[] cards = getCards(game);
        Player currPlayer = getCurrPlayer(game);
        int currWorker = getCurrWorker(game);
        boolean finishInitPos = getFinishInitPos(game);
        String action = getAction(game);
        String winner = getWinner(game);
        Cell[] cells = getCells(game);
        return new GameState(finishChooseCard, playerACard, playerBCard, cards, currPlayer, currWorker, finishInitPos, action, winner, cells);
    }

    @Override
    public String toString() {
        if (this.winner == null) {
            return "{ \"finishchoosecard\": " + this.finishChooseCard + "," +
                    "\"playeracard\": " + this.playerACard + "," +
                    "\"playerbcard\": " + this.playerBCard + "," +
                    "\"cards\": " + Arrays.toString(this.cards) + "," +
                    "\"currplayer\": \"" + this.currPlayer.getPlayerId() + "\"," +
                    "\"currworker\": " + this.currWorker + "," +
                    "\"finishinitpos\": " + this.finishInitPos + "," +
                    "\"action\": \"" + this.action + "\"," +
                    "\"cells\": " + Arrays.toString(this.cells) + "}";
        }
        return "{ \"finishchoosecard\": " + this.finishChooseCard + "," +
                "\"playeracard\": " + this.playerACard + "," +
                "\"playerbcard\": " + this.playerBCard + "," +
                "\"cards\": " + Arrays.toString(this.cards) + "," +
                "\"currplayer\": \"" + this.currPlayer.getPlayerId() + "\"," +
                "\"currworker\": " + this.currWorker + "," +
                "\"finishinitpos\": " + this.finishInitPos + "," +
                "\"action\": \"" + this.action + "\"," +
                "\"winner\": \"" + this.winner + "\"," +
                "\"cells\": " + Arrays.toString(this.cells) + "}";
    }

    private static boolean getFinishChooseCard(Game game) {
        return game.finishChooseGodCards();
    }

    private static int getPlayerACard(Game game) {
        Map<String, Integer> map = game.getPlayerIdCardIndexMap();
        if (map.containsKey("A")) {
            return map.get("A");
        }
        return -1;
    }

    private static int getPlayerBCard(Game game) {
        Map<String, Integer> map = game.getPlayerIdCardIndexMap();
        if (map.containsKey("B")) {
            return map.get("B");
        }
        return -1;
    }

    private static Card[] getCards(Game game) {
        Card[] cards = new Card[Game.CARD_NUMBER];
        Map<Integer, String> cardPowerMap = game.getCardPowerMap();
        for (int i = 0; i < Game.CARD_NUMBER; i++) {
            String text = cardPowerMap.get(i);
            String clazz = "";
            String link = "";
            if (!game.finishChooseGodCards() && (!game.getChosenGodCards().contains(i) || i==0)) {
                clazz = "playable";
                link = "/choosegodcard?i=" + i;
            } else {
                clazz = "occupied";
            }
            cards[i] = new Card(text, clazz, link);
        }
        return cards;
    }

    private static Player getCurrPlayer(Game game) {
        return game.getCurrPlayer();
    }

    private static int getCurrWorker(Game game) {
        return game.getCurrWorker().getWorkerId();
    }

    private static boolean getFinishInitPos(Game game) {
        return game.allWorkersInited();
    }

    private static String getAction(Game game) {
        if (!game.finishChooseGodCards() || !game.allWorkersInited()) {
            return "";
        }
        return game.getCurrPlayer().getGodCard().getAction();
    }

    private static String getWinner(Game game) {
        if (game.getWinner() == null) {
            return null;
        }
        return game.getWinner().getPlayerId();
    }

    private static Cell[] getCells(Game game) {
        Cell[] cells = new Cell[FIELD];
        Grid grid = game.getGrid();
        for (int x = 0; x < ROW; x++) {
            for (int y = 0; y < COLUMN; y++) {
                String text = "";
                String clazz = "";
                String link = "play?x=" + x + "&y=" + y;
                if (!game.finishChooseGodCards()) {
                    clazz = "occupied";
                    link = "";
                } else {
                    if (grid.getFieldHeight(x, y) == EMPTY) {
                        clazz = "playable";
                    } else if (grid.getFieldHeight(x, y) == ONE_LEVEL) {
                        text = "[]";
                        clazz = "playable";
                    } else if (grid.getFieldHeight(x, y) == TWO_LEVEL) {
                        text = "[[]]";
                        clazz = "playable";
                    } else if (grid.getFieldHeight(x, y) == THREE_LEVEL) {
                        text = "[[[]]]";
                        clazz = "playable";
                    } else if (grid.getFieldHeight(x, y) == DOME) {
                        text = "[[[O]]]";
                        clazz = "occupied";
                        link = "";
                    }
                    if (grid.getFieldWorker(x, y) != null) {
                        Worker worker = grid.getFieldWorker(x, y);
                        if (grid.getFieldHeight(x, y) == EMPTY) {
                            text = worker.getPlayerId() + worker.getWorkerId();
                        } else if (grid.getFieldHeight(x, y) == ONE_LEVEL) {
                            text = "[" + worker.getPlayerId() + worker.getWorkerId() + "]";
                        } else if (grid.getFieldHeight(x, y) == TWO_LEVEL) {
                            text = "[[" + worker.getPlayerId() + worker.getWorkerId() + "]]";
                        } else if (grid.getFieldHeight(x, y) == THREE_LEVEL) {
                            text = "[[[" + worker.getPlayerId() + worker.getWorkerId() + "]]]";
                        }
                        clazz = "occupied";
                        link = "";
                    }
                    if (game.finishChooseGodCards() && game.allWorkersInited()) {
                        Player currPlayer = game.getCurrPlayer();
                        GodCard godCard = currPlayer.getGodCard();
                        Worker currWorker = game.getCurrWorker();
                        Set<Point> validPos = godCard.getValidPositions(currWorker);
                        if (validPos.contains(new Point(x, y))) {
                            if (currPlayer.getPlayerId().equals("A")) {
                                clazz = "valida";
                            } else if (currPlayer.getPlayerId().equals("B")) {
                                clazz = "validb";
                            }
                            link = "play?x=" + x + "&y=" + y;
                        }
                    }
                    if (game.getWinner() != null) {
                        link = "";
                    }
                }
                cells[ROW * y + x] = new Cell(text, clazz, link);
            }
        }
        return cells;
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

class Card {
    private final String text;
    private final String clazz;
    private final String link;

    Card(String text, String clazz, String link) {
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
