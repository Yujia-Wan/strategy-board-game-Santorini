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
        this(grid, new Player("A", grid), new Player("B", grid));
    }

    /**
     * Creates a new {@link Game} instance.
     *
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
        return grid;
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
    public boolean finishChosenGodCards() {
        return this.playerCardMap.containsKey(this.playerA)
                && this.playerCardMap.containsKey(this.playerB);
    }

    /**
     * Creates god card for the player.
     *
     * @param player Player.
     * @param x God card's index.
     */
    private void createGodCard(Player player, int x) {
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
                System.out.println("No this god card.");
        }
        player.addGodCard(card);
        this.playerCardMap.put(player, card);
    }

    /**
     * Chooses god cards for a game.
     *
     * @param i God card's index.
     * @return This Game
     */
    public Game chooseGodCards(int i) {
        // first player picks two god cards, the other player selects one of them,
        // first player picks starting player
        if (this.chosenGodCards.size() == 0) {
            this.chosenGodCards.add(i);
            return this;
        }

        if (this.chosenGodCards.size() == 1) {
            if (this.chosenGodCards.get(0) == i) {
                System.out.println("This god card has been chosen!");
                return this;
            }
            this.chosenGodCards.add(i);
            changePlayer();
            return this;
        }

        // first player has picked two god cards
        // the other player selects one of them
        // secondPlayerCard is i
        int firstPlayerCard;
        if (i == this.chosenGodCards.get(0)) {
            firstPlayerCard = this.chosenGodCards.get(1);
        } else if (i == this.chosenGodCards.get(1)) {
            firstPlayerCard = this.chosenGodCards.get(0);
        } else {
            firstPlayerCard = -1;
            System.out.println("Cannot choose this god card!");
        }
        createGodCard(this.currPlayer, i);
        changePlayer();
        createGodCard(this.currPlayer, firstPlayerCard);
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
    private void initWorkerPosition(int workerId, int x, int y) {
        if (this.currPlayer.getWorker(workerId).hasInitPosition()) {
            return;
        }

        this.currPlayer.initWorkerPosition(workerId, x, y);

        if (this.currPlayer.allWorkersInited()) {
            if (this.currPlayer == this.playerA) {
                this.currPlayer = this.playerB;
            } else if (this.currPlayer == this.playerB) {
                this.currPlayer = this.playerA;
            }
        }
    }

    /**
     * Changes turn to another player.
     */
    public void newTurn() {
        if (this.currPlayer == this.playerA && !this.playerA.getGodCard().isMyTurn()) {
            this.currPlayer = this.playerB;
        } else if (this.currPlayer == this.playerB && !this.playerB.getGodCard().isMyTurn()) {
            this.currPlayer = this.playerA;
        }
    }

    /**
     * Each player takes turns to play the game.
     *
     * @param x X coordinate of target position.
     * @param y Y coordinate of target position.
     */
    public Game play(int x, int y) {
        if (!this.allWorkersInited()) {
            initWorkerPosition(this.currWorker.getWorkerId(), x, y);
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
        if (!this.finishChosenGodCards() || !this.allWorkersInited()) {
            return null;
        }

        int gameState = this.currPlayer.getGodCard().getGameState();
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
