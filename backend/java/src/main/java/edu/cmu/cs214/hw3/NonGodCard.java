package edu.cmu.cs214.hw3;

public class NonGodCard extends GodCard {
    private static final String POWER = "Default mode: Play without god card.";

    public NonGodCard(Grid grid, Player player) {
        super(grid, player);
    }

    @Override
    public String getPower() {
        return POWER;
    }
}
