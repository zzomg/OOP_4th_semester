package Game.View;

public interface Observable {
    void updateBoard();
    void updateStatus(String status);
    void checkGameOver();
}
