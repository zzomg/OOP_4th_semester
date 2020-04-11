package Game;

import Game.UI.UserInterface;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class TetrisStartGame
{
    public static void main(String[] args) {
        UserInterface ui = new UserInterface();
        ui.setUI();
        ui.setDefaultCloseOperation(EXIT_ON_CLOSE);
        ui.setLocationRelativeTo(null);
        ui.setSize(300,400);
        ui.setVisible(true);
        ui.setResizable(false);
    }
}