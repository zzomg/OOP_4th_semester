package Game.View;

import Game.Controller.BoardController;
import Game.UI.UISettings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class TetrisBoard extends JPanel implements Observable
{
    private final int BOARD_WIDTH = 15;
    private final int BOARD_HEIGHT = 16;

    private TetrisFrame tetrisFrame;
    private JLabel statusBar;
    private BoardController controller;

    TetrisBoard(TetrisFrame parent) {
        setFocusable(true);
        controller = new BoardController(BOARD_WIDTH, BOARD_HEIGHT);
        statusBar = parent.getStatusBar();
        tetrisFrame = parent;
        addKeyListener(new TAdapter());
    }

    void start() {
        controller.addObserver(this);
        controller.start();
    }

    public void paint(Graphics g) {
        super.paint(g);
        paint(g, getSize().getWidth(), getSize().getHeight());
    }

    private int squareWidth() { return (int) getSize().getWidth() / BOARD_WIDTH; }
    private int squareHeight() { return (int) getSize().getHeight() / BOARD_HEIGHT; }

    public void drawSquare(Graphics g, int x, int y, Game.Model.Shape.Tetrominoes shape)
    {
        Color[] colors = {
                new Color(0, 0, 0),
                new Color(204, 102, 102),
                new Color(102, 204, 102),
                new Color(102, 102, 204),
                new Color(204, 204, 102),
                new Color(204, 102, 204),
                new Color(102, 204, 204),
                new Color(218, 170, 0)
        };

        Color color = colors[shape.ordinal()];

        g.setColor(color);
        g.fillRect(x + 1, y + 1, squareWidth() - 2, squareHeight() - 2);

        g.setColor(color.brighter());
        g.drawLine(x, y + squareHeight() - 1, x, y);
        g.drawLine(x, y, x + squareWidth() - 1, y);

        g.setColor(color.darker());
        g.drawLine(x + 1, y + squareHeight() - 1,
                x + squareWidth() - 1, y + squareHeight() - 1);
        g.drawLine(x + squareWidth() - 1, y + squareHeight() - 1,
                x + squareWidth() - 1, y + 1);
    }

    public void setStatusText(String text) {
        statusBar.setText(text);
    }

    @Override
    public void checkGameOver() {
        if(statusBar.getText().equals("game over")) {
            JButton restartBtn = new JButton("Restart");
            JButton backToMainMenuBtn = new JButton("Back to main menu");

            UISettings.setButtonStyle(restartBtn);
            UISettings.setButtonStyle(backToMainMenuBtn);

            restartBtn.addActionListener(actionEvent -> {
                restartBtn.setVisible(false);
                backToMainMenuBtn.setVisible(false);
                tetrisFrame.setVisible(false);
                tetrisFrame.dispose();
                tetrisFrame = new TetrisFrame();
                controller.removeObserver(this);
                tetrisFrame.init();
            });

            backToMainMenuBtn.addActionListener(actionEvent -> {
                tetrisFrame.setVisible(false);
                tetrisFrame.dispose();
            });

            this.add(restartBtn);
            this.add(backToMainMenuBtn);
        }
    }

    @Override
    public void updateBoard() {
        repaint();
    }

    @Override
    public void updateStatus(String status) {
        setStatusText(status);
    }

    private class TAdapter extends KeyAdapter {

        public void keyPressed(KeyEvent e) {

            if (!controller.isStarted() || controller.isCurrentPieceNoShaped()) {
                return;
            }

            int keycode = e.getKeyCode();

            if (keycode == 'p' || keycode == 'P') {
                controller.pause();
                return;
            }

            if (controller.isPaused())
                return;

            switch (keycode) {
                case KeyEvent.VK_LEFT:
                    controller.moveLeft();
                    break;
                case KeyEvent.VK_RIGHT:
                    controller.moveRight();
                    break;
                case KeyEvent.VK_DOWN:
                    controller.rotateRight();
                    break;
                case KeyEvent.VK_UP:
                    controller.rotateLeft();
                    break;
                case KeyEvent.VK_SPACE:
                    controller.dropDown();
                    break;
                case 'd':
                    controller.oneLineDown();
                    break;
                case 'D':
                    controller.oneLineDown();
                    break;
            }
        }

    }

    public void paint(Graphics g, double width, double height)
    {
        int squareWidth = (int) width / controller.boardWidth;
        int squareHeight = (int) height / controller.boardHeight;
        int boardTop = (int) height - controller.boardHeight * squareHeight;

        for (int i = 0; i < controller.boardHeight; ++i) {
            for (int j = 0; j < controller.boardWidth; ++j) {
                Game.Model.Shape.Tetrominoes shape = controller.shapeAt(j, controller.boardHeight - i - 1);
                if (shape != Game.Model.Shape.Tetrominoes.NoShape)
                    drawSquare(g, j * squareWidth,
                            boardTop + i * squareHeight, shape);
            }
        }

        if (controller.currentPiece.getPieceShape() != Game.Model.Shape.Tetrominoes.NoShape) {
            for (int i = 0; i < 4; ++i) {
                int x = controller.currentX + controller.currentPiece.x(i);
                int y = controller.currentY - controller.currentPiece.y(i);
                drawSquare(g, x * squareWidth,
                        boardTop + (controller.boardHeight - y - 1) * squareHeight,
                        controller.currentPiece.getPieceShape());
            }
        }
    }
}

