package edu.cmu.cs214.hw3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {
    private static final int NON_GOD = 0;
    private static final int DEMETER = 1;
    private static final int MINOTAUR = 2;
    private static final int PAN = 3;
    private final Grid grid;
    private final Player playerA;
    private final Player playerB;
    private Player winner;
    private Player currPlayer;
    private Worker currWorker;
    private List<Integer> chosenGodCards;
    private Map<Player, GodCard> playerCardMap;

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
        this.currWorker = null;
        this.chosenGodCards = new ArrayList<>();
        this.playerCardMap = new HashMap<>();
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

    /**
     * Changes current player.
     */
    private void changePlayer() {
        if (this.currPlayer == this.playerA) {
            this.currPlayer = this.playerB;
        } else if (this.currPlayer == this.playerB) {
            this.currPlayer = this.playerA;
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
    private void createCardForPlayer(Player player, int x) {
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
                card = null;
                System.err.println("No this god card.");
                return;
        }
        player.setGodCard(card);
        this.playerCardMap.put(player, card);
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

        if (this.chosenGodCards.size() == 0) {
            this.chosenGodCards.add(i);
            return this;
        }

        if (this.chosenGodCards.size() == 1) {
            if (this.chosenGodCards.get(0) != 0 && this.chosenGodCards.get(0) == i) {
                System.err.println("This god card has been chosen!");
                return this;
            }
            this.chosenGodCards.add(i);
            changePlayer();
            return this;
        }

        // first player has picked two god cards, the other player selects one of them
        int secondPlayerCard = i;
        int firstPlayerCard;
        if (this.chosenGodCards.get(0) == secondPlayerCard) {
            firstPlayerCard = this.chosenGodCards.get(1);
        } else if (this.chosenGodCards.get(1) == secondPlayerCard) {
            firstPlayerCard = this.chosenGodCards.get(0);
        } else {
            firstPlayerCard = -1;
            System.err.println("Cannot choose this god card!");
            return this;
        }
        createCardForPlayer(this.currPlayer, secondPlayerCard);
        changePlayer();
        createCardForPlayer(this.currPlayer, firstPlayerCard);
        return this;
    }

    /**
     * Checks if all players' workers have initial positions.
     *
     * @return {@code true} if all players' workers have initial positions.
     */
    private boolean allWorkersInited() {
        return this.playerA.allWorkersInited() && this.playerB.allWorkersInited();
    }

    /**
     * Picks starting position for a worker and update current player.
     *
     * @param workerId ID of worker chosen to set starting position.
     * @param x Row index of starting position.
     * @param y Column index of starting position.
     */
    private void initAllWorkersPositions(int workerId, int x, int y) {
        if (this.currPlayer.getWorker(workerId).hasInitPosition()) {
            System.err.println("Worker " + workerId + " for Player " + this.currPlayer.getPlayerId()
                    + " already has starting position!");
            return;
        }

        this.currPlayer.initWorkerPosition(workerId, x, y);
        if (this.currPlayer.allWorkersInited()) {
            changePlayer();
        }
    }

    /**
     * Changes turn to another player.
     */
    public void newTurn() {
        if (this.currPlayer == this.playerA && !this.currPlayer.getGodCard().getMyTurn()) {
            this.currPlayer = this.playerB;
        } else if (this.currPlayer == this.playerB && !this.currPlayer.getGodCard().getMyTurn()) {
            this.currPlayer = this.playerA;
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

        newTurn();
        return this;
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
}
