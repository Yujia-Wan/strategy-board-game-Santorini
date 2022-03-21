package edu.cmu.cs214.hw3;

public class Minotaur extends GodCard {

    // Minotaur: Your worker may move into an opponent Worker's space, if their Worker can
    // be forced one space straight backwards to an unoccupied space at any level.
    public Minotaur(Grid grid, Player player) {
        super(grid, player);
    }
}
