package Game;

import Game.UI.UISettings;
import Game.View.TetrisFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;

public class TetrisStartGame
{
    public static final int recordTableSize = 10;
    public static Map<String, Integer> recordTable = new TreeMap<>();
    public static String playerName = null;

    public static void sortRecordTable(Map<String, Integer> table, LinkedHashMap<String, Integer> tableSorted)
    {
        table.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> tableSorted.put(x.getKey(), x.getValue()));
    }

    public static void main(String[] args)
    {
        JFrame mainMenuFrame = new JFrame("Tetris - The Game");
        UISettings.setFrameBackground(mainMenuFrame, "src/resources/backgrounds/background-1.jpg");

        JButton playButton = new JButton("Play!", new ImageIcon("src/resources/icons/play-icon.png"));
        JButton recordTableButton = new JButton("Record Table", new ImageIcon("src/resources/icons/record-table-icon.png"));
        JButton aboutButton = new JButton("About", new ImageIcon("src/resources/icons/about-icon.png"));
        JButton quitButton = new JButton("Quit", new ImageIcon("src/resources/icons/quit-icon.png"));

        UISettings.setButtonStyle(playButton);
        UISettings.setButtonStyle(recordTableButton);
        UISettings.setButtonStyle(aboutButton);
        UISettings.setButtonStyle(quitButton);

        mainMenuFrame.add(playButton);
        mainMenuFrame.add(recordTableButton);
        mainMenuFrame.add(aboutButton);
        mainMenuFrame.add(quitButton);

        playButton.addActionListener(actionEvent -> {
            JFrame playerNameFrame = new JFrame("Enter player:");

            JButton submitNameButton = new JButton("Submit");
            UISettings.setButtonStyle(submitNameButton);

            JButton startGameButton = new JButton("Start Game");
            UISettings.setButtonStyle(startGameButton);

            JButton goBackButton = new JButton("Go Back");
            UISettings.setButtonStyle(goBackButton);

            JLabel playerNameFieldLabel = new JLabel("Enter name:");
            UISettings.setLabelStyle(playerNameFieldLabel);

            JLabel playerNameSetInfoLabel = new JLabel("", JLabel.CENTER);
            UISettings.setLabelStyle(playerNameSetInfoLabel);

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
                TetrisFrame game = new TetrisFrame();
                game.init();
            });

            goBackButton.addActionListener(actionEvent1 -> {
                playerNameFrame.setVisible(false);
                playerNameFrame.dispose();
            });

            UISettings.setFrameBackground(playerNameFrame, "src/resources/backgrounds/background-1.jpg");

            playerNameFrame.add(playerNameFieldLabel);
            playerNameFrame.add(playerNameField);
            playerNameFrame.add(submitNameButton);
            playerNameFrame.add(playerNameSetInfoLabel);
            playerNameFrame.add(startGameButton);
            playerNameFrame.add(goBackButton);

            playerNameFrame.setLayout(new GridLayout(6, 1));
            UISettings.setFrameSettings(playerNameFrame, WindowConstants.DISPOSE_ON_CLOSE);
        });

        aboutButton.addActionListener(actionEvent -> {
            JFrame aboutGameFrame = new JFrame("About");
            String aboutGameMessage = "Hello! This is a simple Tetris game.<br/>" +
                    "Use arrow keys to control shapes:<br/>" +
                    "→ to move right<br/>← to move left<br/>↓ to rotate right<br/>↑ to rotate left<br/>" +
                    "Press D to speed up.<br/>" +
                    "Press P to pause game.<br/>";
            JLabel aboutGameInfo = new JLabel("<html><div style='text-align: center;'>" + aboutGameMessage + "</div></html>");
            UISettings.setLabelStyle(aboutGameInfo);

            JButton goBackButton = new JButton("Go Back");
            UISettings.setButtonStyle(goBackButton);

            UISettings.setFrameBackground(aboutGameFrame, "src/resources/backgrounds/background-1.jpg");
            aboutGameFrame.setLayout(new BorderLayout());

            aboutGameFrame.add(aboutGameInfo);
            aboutGameFrame.add(goBackButton, BorderLayout.PAGE_END);

            goBackButton.addActionListener(actionEvent1 -> {
                aboutGameFrame.setVisible(false);
                aboutGameFrame.dispose();
            });

            UISettings.setFrameSettings(aboutGameFrame, WindowConstants.DISPOSE_ON_CLOSE);
        });

        recordTableButton.addActionListener(actionEvent -> {
            JFrame recordTableFrame = new JFrame("Record table");

            JButton goBackButton = new JButton("Go Back");
            UISettings.setButtonStyle(goBackButton);
            goBackButton.setAlignmentX(Component.CENTER_ALIGNMENT);

            goBackButton.addActionListener(actionEvent1 -> {
                recordTableFrame.setVisible(false);
                recordTableFrame.dispose();
            });

            final BufferedImage recordTableBG = UISettings.requestImage("src/resources/backgrounds/background-2.jpg");
            JPanel recordTablePanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(recordTableBG, 0, 0, null);
                }
            };
            JLabel recordTableLabel = new JLabel("Record table:");
            UISettings.setLabelStyle(recordTableLabel);
            recordTableLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            recordTablePanel.setLayout(new BoxLayout(recordTablePanel, BoxLayout.Y_AXIS));

            recordTablePanel.add(Box.createRigidArea(new Dimension(0, 30)));
            recordTablePanel.add(recordTableLabel);
            recordTablePanel.add(Box.createVerticalGlue());
            int playersShown = 0;

            LinkedHashMap<String, Integer> recordTableSorted = new LinkedHashMap<>();
            sortRecordTable(recordTable, recordTableSorted);

            for(Map.Entry<String, Integer> player : recordTableSorted.entrySet())
            {
                if(playersShown > recordTableSize) {
                    break;
                }
                JLabel playerRes = new JLabel(player.getKey()
                        + " | "
                        + String.valueOf(recordTable.get(player.getKey())));
                UISettings.setLabelStyle(playerRes);
                playerRes.setAlignmentX(Component.CENTER_ALIGNMENT);
                recordTablePanel.add(playerRes);
                ++playersShown;
            }
            recordTablePanel.add(Box.createVerticalGlue());
            recordTablePanel.add(goBackButton);
            recordTablePanel.add(Box.createRigidArea(new Dimension(0, 30)));

            recordTableFrame.add(recordTablePanel);

            UISettings.setFrameSettings(recordTableFrame, WindowConstants.DISPOSE_ON_CLOSE);
        });

        quitButton.addActionListener(actionEvent -> {
            mainMenuFrame.setVisible(false);
            mainMenuFrame.dispose();
            System.exit(0);
        });

        mainMenuFrame.setLayout(new GridLayout(4, 1));
        UISettings.setFrameSettings(mainMenuFrame, WindowConstants.EXIT_ON_CLOSE);
    }
}
