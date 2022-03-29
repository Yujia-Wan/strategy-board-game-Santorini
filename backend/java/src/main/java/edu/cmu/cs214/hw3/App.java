package edu.cmu.cs214.hw3;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import fi.iki.elonen.NanoHTTPD;

public class App extends NanoHTTPD {
    private static final int PORT = 8080;

    public static void main(String[] args) {
        try {
            new App();
        } catch (IOException ioe) {
            System.err.println("Couldn't start server:\n" + ioe);
        }
    }

    private Game game;

    public App() throws IOException {
        super(PORT);

        this.game = new Game();

        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        System.out.println("Running!");
    }

    @Override
    public Response serve(IHTTPSession session) {
        String uri = session.getUri();
        Map<String, List<String>> params = session.getParameters();
        switch (uri) {
            case "/newgame" -> {
                this.game = new Game();
            }
            case "/choosegodcard" -> {
                this.game = this.game.chooseGodCard(Integer.parseInt(params.get("i").get(0)));
            }
            case "/selectworker" -> {
                this.game = this.game.selectWorker(Integer.parseInt(params.get("i").get(0)));
            }
            case "/play" -> {
                this.game = this.game.play(Integer.parseInt(params.get("x").get(0)), Integer.parseInt(params.get("y").get(0)));
            }
            default -> {
            }
        }

        GameState gamePlay = GameState.forGame(game);
        return newFixedLengthResponse(gamePlay.toString());
    }
}
