package edu.cmu.cs214.hw3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

public class Game {
    public static final int NON_GOD = 0;
    public static final int DEMETER = 1;
    public static final int MINOTAUR = 2;
    public static final int PAN = 3;
    public static final int CARD_NUMBER = 4;
    public static final String[] POWERS = {"NonGod: Play without god card.",
            "Demeter: Your worker may build one additional time, but not on the same space. (Click " +
                    "on the worker's current location to skip the optional second build.)",
            "Minotaur: Your worker may move into an opponent Worker's space, if their Worker can " +
                    "be forced one space straight backwards to an unoccupied space at any level.",
            "Pan: You also win if your Worker moves down two or more levels."};
    private final Grid grid;
    private final Player playerA;
    private final Player playerB;
    private Player winner;
    private Player currPlayer;
    private Worker currWorker;
    private List<Integer> chosenGodCards;
    private Map<Player, GodCard> playerCardMap;
    private Map<String, Integer> playerIdCardIndexMap;

    public Game() {
        this(new Grid());
    }

    public Game(Grid grid) {
        this(grid, new Player(grid, "A"), new Player(grid, "B"));
    }

    /**
     * Creates a new {@link Game} instance.
     *
     * @param grid Game grid.
     * @param playerA ID of the first player.
     * @param playerB ID of the second player.
     */
    public Game(Grid grid, Player playerA, Player playerB) {
        this.grid = grid;
        this.playerA = playerA;
        this.playerB = playerB;
        this.winner = null;
        this.currPlayer = playerA;
        this.currWorker = playerA.getWorker(0);
        this.chosenGodCards = new ArrayList<>();
        this.playerCardMap = new HashMap<>();
        this.playerIdCardIndexMap = new HashMap<>();
    }

    public Grid getGrid() {
        return this.grid;
    }

    public Player getCurrPlayer() {
        return this.currPlayer;
    }

    public Worker getCurrWorker() {
        return this.currWorker;
    }

    public List<Integer> getChosenGodCards() {
        return this.chosenGodCards;
    }

    public Map<String, Integer> getPlayerIdCardIndexMap() {
        return this.playerIdCardIndexMap;
    }

    public Map<Integer, String> getCardPowerMap() {
        Map<Integer, String> cardPowerMap = new HashMap<>();
        for (int i = 0; i < CARD_NUMBER; i++) {
            cardPowerMap.put(i, POWERS[i]);
        }
        return new HashMap<>(cardPowerMap);
    }

    /**
     * Changes current player.
     */
    public void changePlayer() {
        if (this.currPlayer == this.playerA) {
            this.currPlayer = this.playerB;
            this.currWorker = this.currPlayer.getWorker(0);
        } else if (this.currPlayer == this.playerB) {
            this.currPlayer = this.playerA;
            this.currWorker = this.currPlayer.getWorker(0);
        }
    }

    /**
     * Checks if all players have chosen god cards.
     *
     * @return {@code true} if all players have chosen god cards.
     */
    public boolean finishChooseGodCards() {
        return this.playerCardMap.containsKey(this.playerA)
                && this.playerCardMap.containsKey(this.playerB);
    }

    /**
     * Creates god card for the player.
     *
     * @param player Player.
     * @param x God card's index.
     */
    public void createCardForPlayer(Player player, int x) {
        GodCard card;
        switch (x) {
            case NON_GOD:
                card = new NonGodCard(this.grid, player);
                break;
            case DEMETER:
                card = new Demeter(this.grid, player);
                break;
            case MINOTAUR:
                card = new Minotaur(this.grid, player);
                break;
            case PAN:
                card = new Pan(this.grid, player);
                break;
            default:
                System.err.println("No this god card!");
                return;
        }
        player.setGodCard(card);
        this.playerCardMap.put(player, card);
        this.playerIdCardIndexMap.put(player.getPlayerId(), x);
    }

    /**
     * Chooses god cards for two players.
     * First player picks two god cards, the other player selects one of them.
     *
     * @param i God card's index.
     * @return This Game
     */
    public Game chooseGodCard(int i) {
        if (this.finishChooseGodCards()) {
            return this;
        }

        if (this.chosenGodCards.contains(i) && i != 0) {
            System.err.println("This god card has been chosen!");
            return this;
        }

        this.chosenGodCards.add(i);
        this.createCardForPlayer(this.currPlayer, i);
        this.changePlayer();
        return this;
    }

    /**
     * Selects worker of current player.
     *
     * @param workerId Worker ID.
     * @return This game.
     */
    public Game selectWorker(int workerId) {
        this.currWorker = this.currPlayer.getWorker(workerId);
        return this;
    }

    /**
     * Checks if all players' workers have initial positions.
     *
     * @return {@code true} if all players' workers have initial positions.
     */
    public boolean allWorkersInited() {
        return this.playerA.allWorkersInited() && this.playerB.allWorkersInited();
    }

    /**
     * Picks starting position for a worker and update current player.
     *
     * @param workerId ID of worker chosen to set starting position.
     * @param x Row index of starting position.
     * @param y Column index of starting position.
     */
    public void initAllWorkersPositions(int workerId, int x, int y) {
        if (this.currPlayer.getWorker(workerId).hasInitPosition()) {
            System.err.println("Worker " + workerId + " for Player " + this.currPlayer.getPlayerId()
                    + " already has starting position!");
            return;
        }

        this.currPlayer.initWorkerPosition(workerId, x, y);
        if (workerId == 0) {
            this.currWorker = this.currPlayer.getWorker(1);
        } else if (workerId == 1) {
            this.currWorker = this.currPlayer.getWorker(0);
        }
        if (this.currPlayer.allWorkersInited()) {
            changePlayer();
        }
    }

    /**
     * Checks whether the current player wins the game.
     *
     * @return {@code true} if player has a worker on top of a level-3 tower.
     */
    public Player getWinner() {
        if (!this.finishChooseGodCards() || !this.allWorkersInited()) {
            return null;
        }

        int gameState = this.currPlayer.getGodCard().getState();
        if (gameState == 1) {
            this.winner = this.currPlayer;
        } else if (gameState == 0) {
            if (this.currPlayer == this.playerA) {
                this.winner = this.playerB;
            } else if (this.currPlayer == this.playerB) {
                this.winner = this.playerA;
            }
        }
        return this.winner;
    }

    /**
     * Changes turn to another player.
     */
    public void newTurn() {
        if (this.currPlayer == this.playerA && !this.currPlayer.getGodCard().getMyTurn()) {
            this.currPlayer = this.playerB;
            this.currWorker = this.currPlayer.getWorker(0);
        } else if (this.currPlayer == this.playerB && !this.currPlayer.getGodCard().getMyTurn()) {
            this.currPlayer = this.playerA;
            this.currWorker = this.currPlayer.getWorker(0);
        }
    }

    /**
     * Each player takes turns to play the game.
     *
     * @param x X coordinate of target position.
     * @param y Y coordinate of target position.
     * @return This game.
     */
    public Game play(int x, int y) {
        if (!this.allWorkersInited()) {
            initAllWorkersPositions(this.currWorker.getWorkerId(), x, y);
            return this;
        }

        if (!this.currPlayer.getGodCard().execute(this.currWorker, x, y)) {
            return this;
        }

        if (this.getWinner() != null) {
            return this;
        }

        newTurn();
        return this;
    }
}
