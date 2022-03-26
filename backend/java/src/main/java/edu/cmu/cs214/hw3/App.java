package edu.cmu.cs214.hw3;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import fi.iki.elonen.NanoHTTPD;

public class App extends NanoHTTPD {

    public static void main(String[] args) {
        try {
            new App();
        } catch (IOException ioe) {
            System.err.println("Couldn't start server:");
            ioe.printStackTrace();
        }
    }

    private Game game;

    public App() throws IOException {
        super(8080);

        this.game = new Game();

        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        System.out.println("Running!");
    }

    @Override
    public Response serve(IHTTPSession session) {
        String uri = session.getUri();
        Map<String, List<String>> params = session.getParameters();
        switch (uri) {
            case "/newgame":
                this.game = new Game();
                break;
            case "/choosegodcard":
                this.game = this.game.chooseGodCard(Integer.parseInt(params.get("i").get(0)));
                break;
            case "/play":
                this.game = this.game.play(Integer.parseInt(params.get("x").get(0)), Integer.parseInt(params.get("y").get(0)));
                break;
            case "/undo":
//            this.game = this.game.undo();
                break;
        }

        GameState gamePlay = GameState.forGame(game);
        return newFixedLengthResponse(gamePlay.toString());
    }
}
