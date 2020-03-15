package Game.View;

import Game.UI.UISettings;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class TetrisFrame extends JFrame
{
    private JLabel statusBar;
    private TetrisBoard board;

    public TetrisFrame() {
        final BufferedImage tetrisBoardBG = UISettings.requestImage("src/resources/backgrounds/background-2.jpg");
        statusBar = new JLabel(" 0");
        board = new TetrisBoard(this) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(tetrisBoardBG, 0, 0, null);
            }
        };
    }

    public void init() {
        setLayout(new BorderLayout());
        statusBar.setFont(new Font("Lucida Console", Font.PLAIN, 16));
        statusBar.setForeground(Color.BLACK);
        statusBar.setOpaque(true);
        add(statusBar, BorderLayout.SOUTH);
        add(board, BorderLayout.CENTER);
        board.start();

        setSize(300, 400);
        setPreferredSize(new Dimension(300, 400));
        setLocationRelativeTo(null);
        setTitle("Tetris");
        setVisible(true);
        setResizable(false);
    }

    JLabel getStatusBar() {
        return statusBar;
    }
}
