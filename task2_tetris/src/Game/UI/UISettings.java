package Game.UI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class UISettings
{
    public static BufferedImage requestImage(String imagePath)
    {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public static void setFrameSettings(JFrame frame, int closeOp)
    {
        frame.setDefaultCloseOperation(closeOp);
        frame.setLocationRelativeTo(null);
        frame.setSize(300,400);
        frame.setVisible(true);
        frame.setResizable(false);
    }

    public static void setLabelStyle(JLabel label)
    {
        label.setFont(new Font("Lucida Console", Font.PLAIN, 16));
        label.setForeground(Color.WHITE);
    }

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
}
