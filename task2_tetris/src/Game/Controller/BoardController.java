package Game.Controller;

import Game.Model.Shape;
import Game.TetrisStartGame;
import Game.View.TetrisBoard;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BoardController {
    public int boardWidth;
    public int boardHeight;
    private boolean isFallingFinished = false;
    private boolean isStarted = false;
    private boolean isPaused = false;

    private int numLinesRemoved = 0;
    public int currentX = 0;
    public int currentY = 0;
    private Timer timer;

    public Shape currentPiece;
    private Shape.Tetrominoes[] board;

    private List<TetrisBoard> observables = new ArrayList<>();

    public BoardController(int boardWidth, int boardHeight, TetrisBoard tetrisBoard) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        currentPiece = new Shape();
        timer = new Timer(400, tetrisBoard);
        timer.start();
        board = new Shape.Tetrominoes[boardWidth * boardHeight];

        clearBoard();
    }

    public void addObserver(TetrisBoard observable) {
        this.observables.add(observable);
    }

    public void removeObserver(TetrisBoard observable) {
        this.observables.remove(observable);
    }

    public void updateStatus(String status) {
        for (TetrisBoard observable : this.observables) {
            observable.updateStatus(status);
        }
    }

    public void updateBoard() {
        for (TetrisBoard observable : this.observables) {
            observable.updateBoard();
        }
    }

    public void checkGameOver() {
        for (TetrisBoard observable : this.observables) {
            observable.checkGameOver();
        }
    }

    public void gameAction() {
        if (isFallingFinished) {
            isFallingFinished = false;
            newPiece();
        } else {
            oneLineDown();
        }
    }

    public boolean isStarted() {
        return isStarted;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public boolean isCurrentPieceNoShaped() {
        return currentPiece.getPieceShape() == Shape.Tetrominoes.NoShape;
    }

    public void start() {
        if (isPaused)
            return;
        isStarted = true;
        isFallingFinished = false;
        numLinesRemoved = 0;
        clearBoard();
        newPiece();
        timer.start();
    }

    public void pause() {
        if (!isStarted)
            return;

        isPaused = !isPaused;
        if (isPaused) {
            timer.stop();
            updateStatus("paused");
        } else {
            timer.start();
            updateStatus(String.valueOf(numLinesRemoved));
        }
        updateBoard();
    }

    public void oneLineDown()
    {
        if (!tryMove(currentPiece, currentX, currentY - 1))
            pieceDropped();
    }

    private void clearBoard()
    {
        for (int i = 0; i < boardHeight * boardWidth; ++i)
            board[i] = Shape.Tetrominoes.NoShape;
    }

    public void dropDown()
    {
        int newY = currentY;
        while (newY > 0) {
            if (!tryMove(currentPiece, currentX, newY - 1))
                break;
            --newY;
        }
        pieceDropped();
    }

    public Shape.Tetrominoes shapeAt(int x, int y) {
        return board[(y * boardWidth) + x];
    }

    private void newPiece()
    {
        currentPiece.setRandomShape();
        currentX = boardWidth / 2 + 1;
        currentY = boardHeight - 1 + currentPiece.minY();

        if (!tryMove(currentPiece, currentX, currentY)) {
            currentPiece.setPieceShape(Shape.Tetrominoes.NoShape);
            timer.stop();
            isStarted = false;
            updateStatus("game over");
            updateBoard();
            clearBoard();
            checkGameOver();
        }
    }

    private boolean tryMove(Shape newPiece, int newX, int newY)
    {
        for (int i = 0; i < 4; ++i) {
            int x = newX + newPiece.x(i);
            int y = newY - newPiece.y(i);
            if (x < 0 || x >= boardWidth || y < 0 || y >= boardHeight)
                return false;
            if (shapeAt(x, y) != Shape.Tetrominoes.NoShape)
                return false;
        }

        currentPiece = newPiece;
        currentX = newX;
        currentY = newY;
        updateBoard();
        return true;
    }

    private void pieceDropped()
    {
        for (int i = 0; i < 4; ++i) {
            int x = currentX + currentPiece.x(i);
            int y = currentY - currentPiece.y(i);
            board[(y * boardWidth) + x] = currentPiece.getPieceShape();
        }

        removeFullLines();

        if (!isFallingFinished)
            newPiece();
    }

    private void removeFullLines()
    {
        int numFullLines = 0;

        for (int i = boardHeight - 1; i >= 0; --i) {
            boolean lineIsFull = true;

            for (int j = 0; j < boardWidth; ++j) {
                if (shapeAt(j, i) == Shape.Tetrominoes.NoShape) {
                    lineIsFull = false;
                    break;
                }
            }

            if (lineIsFull) {
                ++numFullLines;
                for (int k = i; k < boardHeight - 1; ++k) {
                    for (int j = 0; j < boardWidth; ++j)
                        board[(k * boardWidth) + j] = shapeAt(j, k + 1);
                }
            }
        }

        if (numFullLines > 0) {
            numLinesRemoved += numFullLines;
            updateStatus(String.valueOf(numLinesRemoved));
            if (TetrisStartGame.playerName != null) {
                boolean playerExist = TetrisStartGame.recordTable.containsKey(TetrisStartGame.playerName);
                if (playerExist && numLinesRemoved > TetrisStartGame.recordTable.get(TetrisStartGame.playerName)) {
                    TetrisStartGame.recordTable.put(TetrisStartGame.playerName, numLinesRemoved);
                }
            }
            isFallingFinished = true;
            currentPiece.setPieceShape(Shape.Tetrominoes.NoShape);
            updateBoard();
        }
    }

    public void moveLeft() {
        tryMove(currentPiece, currentX - 1, currentY);
    }
    public void moveRight() {
        tryMove(currentPiece, currentX + 1, currentY);
    }
    public void rotateLeft() {
        tryMove(currentPiece.rotateLeft(), currentX, currentY);
    }
    public void rotateRight() {
        tryMove(currentPiece.rotateRight(), currentX, currentY);
    }
}