package Game.UI;

import Game.View.TetrisBoard;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class UserInterface extends JFrame
{
    private static Container content;
    private static CardLayout cardLayout;
    
    public static final int recordTableSize = 10;
    public static Map<String, Integer> recordTable = new TreeMap<>();
    public static String playerName = null;

    final static BufferedImage bg1 = UISettings.requestImage("src/resources/backgrounds/background-1.jpg");
    final static BufferedImage bg2 = UISettings.requestImage("src/resources/backgrounds/background-2.jpg");

    public UserInterface()
    {
        content = getContentPane();
        cardLayout = new CardLayout();
        content.setLayout(cardLayout);
    }

    public static void reloadBoard()
    {
        TetrisBoard newBoard = new TetrisBoard() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bg2, 0, 0, null);
            }
        };
        content.add("tetris", newBoard);
        cardLayout.show(content, "tetris");
        newBoard.start();
    }

    public static void returnToMainMenu()
    {
        cardLayout.show(content, "main_menu");
    }

    public void setPanel_about(JPanel aboutGamePanel)
    {
        String aboutGameMessage = "Hello! This is a simple Tetris game.<br/>" +
                "Use arrow keys to control shapes:<br/>" +
                "→ to move right<br/>← to move left<br/>↓ to rotate right<br/>↑ to rotate left<br/>" +
                "Press D to speed up.<br/>" +
                "Press P to pause game.<br/>";
        JLabel aboutGameInfo = new JLabel("<html><div style='text-align: center;'>" + aboutGameMessage + "</div></html>");
        UISettings.setLabelStyle(aboutGameInfo);

        JButton goBackButton_about = new JButton("Go Back");
        UISettings.setButtonStyle(goBackButton_about);

        aboutGamePanel.setLayout(new BorderLayout());

        aboutGamePanel.add(aboutGameInfo);
        aboutGamePanel.add(goBackButton_about, BorderLayout.PAGE_END);

        goBackButton_about.addActionListener(actionEvent1 -> cardLayout.show(content, "main_menu"));
    }

    public void setPanel_records(JPanel recordTablePanel)
    {
        JLabel recordTableLabel = new JLabel("Record table:");
        UISettings.setLabelStyle(recordTableLabel);
        recordTableLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        recordTablePanel.setLayout(new BoxLayout(recordTablePanel, BoxLayout.Y_AXIS));

        recordTablePanel.add(Box.createRigidArea(new Dimension(0, 30)));
        recordTablePanel.add(recordTableLabel);
        recordTablePanel.add(Box.createVerticalGlue());
    }

    public void showRecordTable(JPanel recordTablePanel)
    {
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
                    + recordTable.get(player.getKey()));
            UISettings.setLabelStyle(playerRes);
            playerRes.setAlignmentX(Component.CENTER_ALIGNMENT);
            recordTablePanel.add(playerRes);
            ++playersShown;
        }
    }

    public void setPanel_play(JPanel playerNamePanel, TetrisBoard boardPanel)
    {
        JButton submitNameButton = new JButton("Submit");
        UISettings.setButtonStyle(submitNameButton);

        JButton startGameButton = new JButton("Start Game");
        UISettings.setButtonStyle(startGameButton);

        JButton goBackButton_play = new JButton("Go Back");
        UISettings.setButtonStyle(goBackButton_play);

        JLabel playerNameFieldLabel = new JLabel("Enter name:");
        UISettings.setLabelStyle(playerNameFieldLabel);

        JLabel playerNameSetInfoLabel = new JLabel("", JLabel.CENTER);
        UISettings.setLabelStyle(playerNameSetInfoLabel);

        JTextField playerNameField = new JTextField();
        playerNameField.setFont(new Font("Lucida Console", Font.PLAIN, 16));

        submitNameButton.addActionListener(actionEvent1 -> {
            String playerName = playerNameField.getText();
            playerNameSetInfoLabel.setText(String.format("Name %s submitted.", playerName));
            UserInterface.playerName = playerName;
            if(!recordTable.containsKey(playerName)) {
                recordTable.put(playerName, 0);
            }
        });

        startGameButton.addActionListener(actionEvent ->
        {
            cardLayout.show(content, "tetris");
            boardPanel.start();
        });

        goBackButton_play.addActionListener(actionEvent -> cardLayout.show(content, "main_menu"));

        playerNamePanel.add(playerNameFieldLabel);
        playerNamePanel.add(playerNameField);
        playerNamePanel.add(submitNameButton);
        playerNamePanel.add(playerNameSetInfoLabel);
        playerNamePanel.add(startGameButton);
        playerNamePanel.add(goBackButton_play);

        playerNamePanel.setLayout(new GridLayout(6, 1));
    }

    public static void sortRecordTable(Map<String, Integer> table, LinkedHashMap<String, Integer> tableSorted)
    {
        table.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> tableSorted.put(x.getKey(), x.getValue()));
    }

    public void setUI()
    {
        JButton playButton = new JButton("Play!", new ImageIcon("src/resources/icons/play-icon.png"));
        JButton recordTableButton = new JButton("Record Table", new ImageIcon("src/resources/icons/record-table-icon.png"));
        JButton aboutButton = new JButton("About", new ImageIcon("src/resources/icons/about-icon.png"));
        JButton quitButton = new JButton("Quit", new ImageIcon("src/resources/icons/quit-icon.png"));

        UISettings.setButtonStyle(playButton);
        UISettings.setButtonStyle(recordTableButton);
        UISettings.setButtonStyle(aboutButton);
        UISettings.setButtonStyle(quitButton);

        JPanel mainMenuPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bg1, 0, 0, null);
            }
        };

        TetrisBoard boardPanel = new TetrisBoard() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bg2, 0, 0, null);
            }
        };

        mainMenuPanel.add(playButton);
        mainMenuPanel.add(recordTableButton);
        mainMenuPanel.add(aboutButton);
        mainMenuPanel.add(quitButton);

///////////////////////////////////////////////////////////////////////////

        JPanel playerNamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bg1, 0, 0, null);
            }
        };
        setPanel_play(playerNamePanel, boardPanel);
        playButton.addActionListener(actionEvent -> cardLayout.show(content, "play"));

///////////////////////////////////////////////////////////////////////////

        JPanel aboutGamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bg1, 0, 0, null);
            }
        };
        setPanel_about(aboutGamePanel);
        aboutButton.addActionListener(actionEvent -> cardLayout.show(content, "about"));

///////////////////////////////////////////////////////////////////////////

        JPanel recordTablePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bg2, 0, 0, null);
            }
        };
        setPanel_records(recordTablePanel);
        recordTableButton.addActionListener(actionEvent ->
        {
            JButton goBackButton_records = new JButton("Go Back");
            UISettings.setButtonStyle(goBackButton_records);
            goBackButton_records.setAlignmentX(Component.CENTER_ALIGNMENT);
            goBackButton_records.addActionListener(actionEvent1 -> cardLayout.show(content, "main_menu"));

            showRecordTable(recordTablePanel);

            recordTablePanel.add(Box.createVerticalGlue());
            recordTablePanel.add(goBackButton_records);
            recordTablePanel.add(Box.createRigidArea(new Dimension(0, 30)));
            cardLayout.show(content, "records");
        });

///////////////////////////////////////////////////////////////////////////

        quitButton.addActionListener(actionEvent -> {
            setVisible(false);
            dispose();
            System.exit(0);
        });

        mainMenuPanel.setLayout(new GridLayout(4, 1));

        content.add("main_menu", mainMenuPanel);
        content.add("play", playerNamePanel);
        content.add("about", aboutGamePanel);
        content.add("records", recordTablePanel);
        content.add("tetris", boardPanel);
    }
}
