package edu.cmu.cs214.hw3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {
    private static final int WIN_HEIGHT = 3;
    private static final String[] GOD_CARDS = {"NonGod: Play game without god cards.",
            "Demeter: Your worker may build one additional time, but not on the same space.",
            "Minotaur: Your worker may move into an opponent Worker's space, if their Worker can " +
                    "be forced one space straight backwards to an unoccupied space at any level.",
            "Pan: You also win if your Worker moves down two or more levels."};
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


    private boolean allWorkersInited() {
        return this.playerA.allWorkersInited() && this.playerB.allWorkersInited();
    }

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


    private void updatePlayerForChooseCard() {
        if (this.currPlayer == this.playerA) {
            this.currPlayer = playerB;
        } else if (this.currPlayer == this.playerB) {
            this.currPlayer = playerA;
        }
    }

    private GodCard createGodCard(Player player, int x) {
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
        return card;
    }

    public Game chooseGodCards(int i) {
        // first player picks two god cards, the other player selects one of them, first player picks starting player
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
            updatePlayerForChooseCard();
            return this;
        }

        int playerBCard = i;
        int playerACard;
        if (this.chosenGodCards.get(0) == i) {
            playerACard = this.chosenGodCards.get(1);
        } else {
            playerACard = this.chosenGodCards.get(0);
        }

        createGodCard(this.playerA, playerACard);
        createGodCard(this.playerB, playerBCard);
        return this;
    }


    private boolean readyForPlay() {
        // have chosen god cards and init worker positions
        return this.playerCardMap.containsKey(this.playerA) && this.playerCardMap.containsKey(this.playerB)
                && this.allWorkersInited();
    }

    /**
     * Checks whether the current player wins the game.
     *
     * @return {@code true} if player has a worker on top of a level-3 tower.
     */
    public Player getWinner() {
        if (!this.readyForPlay()) {
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
