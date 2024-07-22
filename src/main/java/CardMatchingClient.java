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
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Player.Player;

public class CardMatchingClient {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String playerName;
    private ConcurrentHashMap<String, Player> players;
    private int movements;

    public CardMatchingClient() {
        JFrame frame = new JFrame("Geo-Memory");
        frame.setSize(400, 300); // Set the size of the frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Set the default close operation

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon image = new ImageIcon("img/background.png"); // Replace with your image path
                g.drawImage(image.getImage(), 0, 0, getWidth(), getHeight(), null);
            }
        };
        panel.setLayout(new FlowLayout()); // Set the layout of the panel

        JLabel label = new JLabel("Bienvenidos");
        label.setFont(new Font("Arial", Font.BOLD, 24));
        JLabel label2 = new JLabel("A nuestro juego de Geografía");
        label2.setFont(new Font("Arial", Font.BOLD, 24));// Set the font of the label
    
        JTextField nameField = new JTextField(20);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String playerName = nameField.getText();
                if (!playerName.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Bienvenido, " + playerName + "!");
                    CardMatchingClient.this.playerName = playerName;
                    frame.dispose(); // Close the welcome frame
                    startGame(); // Start the game
                } else {
                    JOptionPane.showMessageDialog(frame, "Por favor, ingrese el nombre del jugador.");
                }
            }
        });

        panel.add(label);
        panel.add(label2);
        panel.add(new JLabel("Ingrese su nombre:"));
        panel.add(nameField);
        panel.add(submitButton);

        frame.getContentPane().add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void startGame() {
        try {
            socket = new Socket("localhost", 8080);
            System.out.println("Connected to server");

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            new CardMatchingGame();

            players = new ConcurrentHashMap<>();
            movements = 0;

            // Send player information to the server
            out.println("PLAYER " + playerName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendCardFlip(int cardId) throws IOException {
        out.println("FLIP " + cardId);
        movements++;
    }

    public void receiveGameState() throws IOException {
        String input = in.readLine();
        if (input != null) {
            System.out.println("Received from server: " + input);
            // Update the game state based on the server's response
            //...
            String[] parts = input.split(" ");
            if (parts[0].equals("GAME_STATE")) {
                // Update the players' scores and game state
                for (int i = 1; i < parts.length; i += 2) {
                    String playerName = parts[i];
                    int playerMovements = Integer.parseInt(parts[i + 1]);
                    players.put(playerName, new Player(playerName, playerMovements));
                }
            } else if (parts[0].equals("WINNER")) {
                // Display the winner's name and movements
                String winnerName = parts[1];
                int winnerMovements = Integer.parseInt(parts[2]);
                System.out.println("Winner: " + winnerName + " with " + winnerMovements + " movements");
                // Update the UI to display the winner
            }
        }
    }

    public static void main(String[] args) {
        new CardMatchingClient();
    }
}