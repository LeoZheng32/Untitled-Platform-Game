import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GraphicsPanel extends JPanel implements ActionListener, KeyListener, MouseListener {
    private static boolean canMove;
    private BufferedImage background;
    private boolean[] pressedKeys;
    private Timer timer;
    private Player user;

    public GraphicsPanel() {
        canMove = true;
        pressedKeys = new boolean[128];
        timer = new Timer(2, this);
        timer.start();
        try {
            background = ImageIO.read(new File("src/Resources/background.jpeg"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        user = new Player();
        addKeyListener(this);
        addMouseListener(this);
        setFocusable(true);
        setFocusable(true);
        requestFocusInWindow();

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, null);
        if (user.getCurrentAnimation().animationType().equals("dead")) {
            // 70 is the height of the character
            g.drawImage(user.getPlayerImage(), user.getxCoord(), 275 - user.getHeight() + 70, user.getWidth(), user.getHeight(), null);
        } else {
            g.drawImage(user.getPlayerImage(), user.getxCoord(), user.getyCoord(), user.getWidth(), user.getHeight(), null);

        }

        if (canMove) {
            // player moves left (A)
            if (pressedKeys[65]) {
                if (pressedKeys[16]) {
                    if (user.getCurrentAnimation().animationType() != null & !user.getCurrentAnimation().animationType().equals("run")) {
                        user.updateCurrentAnimation("run");
                    }
                } else {
                    if (user.getCurrentAnimation().animationType() != null & !user.getCurrentAnimation().animationType().equals("walk")) {
                        user.updateCurrentAnimation("walk");
                    }
                }
                user.faceLeft();
                user.moveLeft();
            }

            // player moves right (D)
            if (pressedKeys[68]) {
                if (pressedKeys[16]) {
                    if (user.getCurrentAnimation().animationType() != null & !user.getCurrentAnimation().animationType().equals("run")) {
                        user.updateCurrentAnimation("run");
                    }
                } else {
                    if (user.getCurrentAnimation().animationType() != null & !user.getCurrentAnimation().animationType().equals("walk")) {
                        user.updateCurrentAnimation("walk");
                    }
                }
                user.faceRight();
                user.moveRight();
            }

            // player moves up (W)
            if (pressedKeys[87]) {
                user.moveUp();
            }

            // player moves down (S)
            if (pressedKeys[83]) {
                user.moveDown();
            }
        }
    }

    public static void setCanMove(boolean ableToMove) {
        canMove = ableToMove;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        pressedKeys[key] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        pressedKeys[key] = false;
        if (canMove) {
            user.updateCurrentAnimation("idle");
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            user.updateCurrentAnimation("attackOne");
        } else if (e.getButton() == MouseEvent.BUTTON3) {

        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
