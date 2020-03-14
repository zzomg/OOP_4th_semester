package Game;

import Game.View.TetrisFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

public class TetrisStartGame
{
    public static Map<String, Integer> recordTable = new TreeMap();
    public static String playerName;

    public static void setFrameBackground(JFrame frame, String bgPath)
    {
        BufferedImage bg = null;
        try {
            bg = ImageIO.read(new File(bgPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        frame.setContentPane(new ImagePanel(bg));
    }

    public static void setButtonStyle(JButton button)
    {
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Lucida Console", Font.BOLD, 16));
    }

    public static void main(String[] args)
    {
        JButton playButton = new JButton("Play!", new ImageIcon("src/resources/icons/play-icon.png"));
        JButton recordTableButton = new JButton("Record Table", new ImageIcon("src/resources/icons/record-table-icon.png"));
        JButton aboutButton = new JButton("About", new ImageIcon("src/resources/icons/about-icon.png"));
        JButton quitButton = new JButton("Quit", new ImageIcon("src/resources/icons/quit-icon.png"));

        setButtonStyle(playButton);
        setButtonStyle(recordTableButton);
        setButtonStyle(aboutButton);
        setButtonStyle(quitButton);

        JFrame mainMenuFrame = new JFrame("Tetris - The Game");

        setFrameBackground(mainMenuFrame, "src/resources/backgrounds/background-1.jpg");

        GridLayout mainMenuLayout = new GridLayout(4, 1);
        mainMenuFrame.setLayout(mainMenuLayout);

        mainMenuFrame.add(playButton);
        mainMenuFrame.add(recordTableButton);
        mainMenuFrame.add(aboutButton);
        mainMenuFrame.add(quitButton);

        playButton.addActionListener(actionEvent -> {
            JFrame playerNameFrame = new JFrame("Enter player:");

            JButton submitNameButton = new JButton("Submit");
            setButtonStyle(submitNameButton);

            JButton startGameButton = new JButton("Start Game");
            setButtonStyle(startGameButton);

            JButton goBackButton = new JButton("Go Back");
            setButtonStyle(goBackButton);

            JLabel playerNameFieldLabel = new JLabel("Enter name:");
            playerNameFieldLabel.setFont(new Font("Lucida Console", Font.PLAIN, 16));
            playerNameFieldLabel.setForeground(Color.WHITE);

            JLabel playerNameSetInfoLabel = new JLabel("", JLabel.CENTER);
            playerNameSetInfoLabel.setFont(new Font("Lucida Console", Font.PLAIN, 16));
            playerNameSetInfoLabel.setForeground(Color.WHITE);

            JTextField playerNameField = new JTextField();
            playerNameField.setFont(new Font("Lucida Console", Font.PLAIN, 16));

            submitNameButton.addActionListener(actionEvent1 -> {
                playerNameSetInfoLabel.setText("Name has been submitted.");
                String playerName = playerNameField.getText();
                TetrisStartGame.playerName = playerName;
                if(!recordTable.containsKey(playerName)) {
                    recordTable.put(playerName, 0);
                }
            });

            startGameButton.addActionListener(actionEvent1 -> {
                TetrisFrame game = null;
                try {
                    game = new TetrisFrame();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                assert game != null;
                game.init();
            });

            goBackButton.addActionListener(actionEvent1 -> {
                playerNameFrame.setVisible(false);
                playerNameFrame.dispose();
            });

            setFrameBackground(playerNameFrame, "src/resources/backgrounds/background-1.jpg");

            playerNameFrame.add(playerNameFieldLabel);
            playerNameFrame.add(playerNameField);
            playerNameFrame.add(submitNameButton);
            playerNameFrame.add(playerNameSetInfoLabel);
            playerNameFrame.add(startGameButton);
            playerNameFrame.add(goBackButton);

            playerNameFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            playerNameFrame.setLocationRelativeTo(null);
            playerNameFrame.setSize(300,400);
            playerNameFrame.setLayout(new GridLayout(6, 1));
            playerNameFrame.setVisible(true);
            playerNameFrame.setResizable(false);
        });

        aboutButton.addActionListener(actionEvent -> {
            JFrame aboutGameFrame = new JFrame("About");
            String aboutGameMessage = "Hello! This is a simple Tetris game.<br/>" +
                    "Use arrow keys to control shapes:<br/>" +
                    "→ to move right<br/>← to move left<br/>↓ to rotate right<br/>↑ to rotate left<br/>" +
                    "Press D to speed up.<br/>" +
                    "Press P to pause game.<br/>";
            JLabel aboutGameInfo = new JLabel("<html><div style='text-align: center;'>" + aboutGameMessage + "</div></html>");
            aboutGameInfo.setFont(new Font("Lucida Console", Font.PLAIN, 16));
            aboutGameInfo.setForeground(Color.WHITE);

            JButton goBackButton = new JButton("Go Back");
            setButtonStyle(goBackButton);

            setFrameBackground(aboutGameFrame, "src/resources/backgrounds/background-1.jpg");

            BorderLayout aboutLayout = new BorderLayout();
            aboutGameFrame.setLayout(aboutLayout);

            aboutGameFrame.add(aboutGameInfo);
            aboutGameFrame.add(goBackButton, BorderLayout.PAGE_END);

            goBackButton.addActionListener(actionEvent1 -> {
                aboutGameFrame.setVisible(false);
                aboutGameFrame.dispose();
            });

            aboutGameFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            aboutGameFrame.setLocationRelativeTo(null);
            aboutGameFrame.setSize(300,400);
            aboutGameFrame.setVisible(true);
            aboutGameFrame.setResizable(false);
        });

        recordTableButton.addActionListener(actionEvent -> {
            JFrame recordTableFrame = new JFrame("Record table");
            setFrameBackground(recordTableFrame, "src/resources/backgrounds/background-1.jpg");

            JButton goBackButton = new JButton("Go Back");
            setButtonStyle(goBackButton);

            goBackButton.addActionListener(actionEvent1 -> {
                recordTableFrame.setVisible(false);
                recordTableFrame.dispose();
            });

            for(Map.Entry<String, Integer> player : recordTable.entrySet())
            {
                //  TODO: use JPanel probably
                JLabel playerName = new JLabel(player.getKey());
                JLabel playerRes = new JLabel(String.valueOf(recordTable.get(player.getKey())));
                recordTableFrame.add(playerName);
                recordTableFrame.add(playerRes);
                playerName.setFont(new Font("Lucida Console", Font.PLAIN, 16));
                playerName.setForeground(Color.WHITE);
                playerRes.setFont(new Font("Lucida Console", Font.PLAIN, 16));
                playerRes.setForeground(Color.WHITE);
            }
            recordTableFrame.add(goBackButton);

            recordTableFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            recordTableFrame.setLocationRelativeTo(null);
            recordTableFrame.setSize(300,400);
            recordTableFrame.setLayout(new GridLayout(TetrisStartGame.recordTable.size(), 2));
            recordTableFrame.setVisible(true);
            recordTableFrame.setResizable(false);
        });

        quitButton.addActionListener(actionEvent -> {
            mainMenuFrame.setVisible(false);
            mainMenuFrame.dispose();
            System.exit(0);
        });

        mainMenuFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainMenuFrame.setLocationRelativeTo(null);
        mainMenuFrame.setSize(300,400);
        mainMenuFrame.setVisible(true);
        mainMenuFrame.setResizable(false);
    }
}
