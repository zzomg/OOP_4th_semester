package Game.Controller;

import Game.Model.Shape;
import Game.TetrisStartGame;
import Game.View.Observable;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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

    private List<Observable> observables = new ArrayList<>();

    public BoardController(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        currentPiece = new Shape();
        this.timer = new Timer();
        this.timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (isFallingFinished) {
                    isFallingFinished = false;
                    newPiece();
                } else {
                    oneLineDown();
                }
            }
        }, 0, 400);
        board = new Shape.Tetrominoes[boardWidth * boardHeight];

        clearBoard();
    }

    public void pauseTimer() {
        this.timer.cancel();
    }

    public void resumeTimer() {
        this.timer = new Timer();
        this.timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (isFallingFinished) {
                    isFallingFinished = false;
                    newPiece();
                } else {
                    oneLineDown();
                }
            }
        }, 0, 400);
    }

    public void addObserver(Observable obs) {
        this.observables.add(obs);
    }

    public void removeObserver(Observable obs) {
        this.observables.remove(obs);
    }

    public void updateStatus(String status) {
        for (Observable obs : this.observables) {
            obs.updateStatus(status);
        }
    }

    public void updateBoard() {
        for (Observable obs : this.observables) {
            obs.updateBoard();
        }
    }

    public void checkGameOver() {
        for (Observable obs : this.observables) {
            obs.checkGameOver();
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
    }

    public void pause() {
        if (!isStarted)
            return;

        isPaused = !isPaused;
        if (isPaused) {
            pauseTimer();
            updateStatus("paused");
        } else {
            resumeTimer();
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
            pauseTimer();
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
