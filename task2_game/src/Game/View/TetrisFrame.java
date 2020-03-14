package Game.View;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class TetrisFrame extends JFrame
{
    private JLabel statusBar;
    private TetrisBoard board;

    public TetrisFrame() throws IOException {
        statusBar = new JLabel(" 0");
        board = new TetrisBoard(this);
    }

    public void init() {
        setLayout(new BorderLayout());
        add(statusBar, BorderLayout.SOUTH);
        add(board, BorderLayout.CENTER);
        board.start();
        setSize(300, 400);
        setPreferredSize(new Dimension(300, 400));
        setTitle("Tetris");
        //pack();
        setVisible(true);
        setResizable(false);
    }

    JLabel getStatusBar() {
        return statusBar;
    }
}