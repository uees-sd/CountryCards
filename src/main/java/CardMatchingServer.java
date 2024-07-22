/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author josev
 */
import java.net.*;
import java.io.*;
import static java.lang.System.out;
import java.util.concurrent.ConcurrentHashMap;
import Player.Player;

public class CardMatchingServer {
    private ServerSocket serverSocket;
    private ConcurrentHashMap<String, Player> players;
    private int currentPlayerIndex;

    public CardMatchingServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Server started on port " + port);

        players = new ConcurrentHashMap<>();
        currentPlayerIndex = 0;
    }

    public void start() throws IOException {
        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected");

            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            // Handle incoming connections and process data sent by the client
            String input = in.readLine();
            if (input != null) {
                System.out.println("Received from client: " + input);
                // Process the input data and update the game state
                // ...
                if (input.startsWith("PLAYER ")) {
                    String playerName = input.substring(7);
                    players.put(playerName, new Player(playerName, 0));
                    System.out.println("Player " + playerName + " connected");
                } else if (input.startsWith("FLIP ")) {
                    int cardId = Integer.parseInt(input.substring(5));
                    // Update the game state based on the flipped card
                    // ...
                    // Increment the player's movement count
                    String playerName = getPlayerNameFromSocket(clientSocket);
                    players.get(playerName).incrementMovements();
                    // Send game state updates to all connected clients
                    sendGameStateUpdate();
                }
            }
        }
    }

    private void sendGameStateUpdate() {
        for (Player player : players.values()) {
            out.println("GAME_STATE " + player.getName() + " " + player.getMovements());
        }
        // Check for the winner and send the winner's information to all clients
        Player winner = getWinner();
        if (winner != null) {
            out.println("WINNER " + winner.getName() + " " + winner.getMovements());
        }
    }

    private Player getWinner() {
        Player winner = null;
        int minMovements = Integer.MAX_VALUE;
        for (Player player : players.values()) {
            if (player.getMovements() < minMovements) {
                minMovements = player.getMovements();
                winner = player;
            }
        }
        return winner;
    }

    private String getPlayerNameFromSocket(Socket socket) {
        // Implement logic to get the player name from the socket
        // ...
        return null;
    }

    public static void main(String[] args) throws IOException {
        CardMatchingServer server = new CardMatchingServer(8080);
        server.start();
    }
}

