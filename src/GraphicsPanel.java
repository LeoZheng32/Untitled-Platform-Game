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
    private int currentCycle;
    private boolean walking;
    private boolean running;
    private boolean AD;
    private static boolean jump;
    private static boolean attacking;
    private static boolean right;

    public GraphicsPanel() {
        AD = false;
        right = true;
        walking = false;
        running = false;
        attacking = false;
        jump = false;
        currentCycle = 0;
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
        } else if (user.getCurrentAnimation().animationType().equals("attackOne")) {
            if (user.getCurrentAnimation().getCurrentFrame() == 4) {
                g.drawImage(user.getPlayerImage(), user.getxCoord(), user.getyCoord() - 20, user.getWidth(), user.getHeight(), null);
            } else {
                g.drawImage(user.getPlayerImage(), user.getxCoord(), user.getyCoord(), user.getWidth(), user.getHeight(), null);
            }
        } else if (user.getCurrentAnimation().animationType().equals("attackTwo")) {
            if (user.getCurrentAnimation().getCurrentFrame() == 0) {
                g.drawImage(user.getPlayerImage(), user.getxCoord(), user.getyCoord() - 22, user.getWidth(), user.getHeight(), null);
            } else if (user.getCurrentAnimation().getCurrentFrame() == 1) {
                g.drawImage(user.getPlayerImage(), user.getxCoord(), user.getyCoord() - 14, user.getWidth(), user.getHeight(), null);
            } else {
                g.drawImage(user.getPlayerImage(), user.getxCoord(), user.getyCoord(), user.getWidth(), user.getHeight(), null);
            }
        } else if (user.getCurrentAnimation().animationType().equals("attackThree")) {
            if (user.getCurrentAnimation().getCurrentFrame() == 2 && right) {
                g.drawImage(user.getPlayerImage(), user.getxCoord() + 65, user.getyCoord() + 4, user.getWidth(), user.getHeight(), null);
            } else if (user.getCurrentAnimation().getCurrentFrame() == 2 && !right) {
                g.drawImage(user.getPlayerImage(), user.getxCoord() - 65, user.getyCoord() + 4, user.getWidth(), user.getHeight(), null);
            } else if (user.getCurrentAnimation().getCurrentFrame() == 3 && right) {
                g.drawImage(user.getPlayerImage(), user.getxCoord() + 54, user.getyCoord() + 10, user.getWidth(), user.getHeight(), null);
            } else if ((user.getCurrentAnimation().getCurrentFrame() == 3) && !right) {
                g.drawImage(user.getPlayerImage(), user.getxCoord() - 54, user.getyCoord() + 10, user.getWidth(), user.getHeight(), null);
            } else {
                g.drawImage(user.getPlayerImage(), user.getxCoord(), user.getyCoord(), user.getWidth(), user.getHeight(), null);
            }
        } else {
            g.drawImage(user.getPlayerImage(), user.getxCoord(), user.getyCoord(), user.getWidth(), user.getHeight(), null);

        }

        if (canMove) {
            if (pressedKeys[65] && pressedKeys[68]) {
                AD = true;
            }
            // player moves left (A)
            if (pressedKeys[65] && !attacking && !AD) {
                right = false;
                if (pressedKeys[16]) {
                    if (user.getCurrentAnimation().animationType() != null && !user.getCurrentAnimation().animationType().equals("run")) {
                        user.updateCurrentAnimation("run");
                        walking = false;
                        running = true;
                    }
                } else {
                    if (user.getCurrentAnimation().animationType() != null && !user.getCurrentAnimation().animationType().equals("walk")) {
                        user.updateCurrentAnimation("walk");
                        walking = true;
                    }
                }
                user.faceLeft();
                user.moveLeft();
            }

            // player moves right (D)
            if (pressedKeys[68] && !attacking && !AD) {
                right = true;
                if (pressedKeys[16]) {
                    if (user.getCurrentAnimation().animationType() != null && !user.getCurrentAnimation().animationType().equals("run")) {
                        user.updateCurrentAnimation("run");
                        walking = false;
                        running = true;
                    }
                } else {
                    if (user.getCurrentAnimation().animationType() != null && !user.getCurrentAnimation().animationType().equals("walk")) {
                        user.updateCurrentAnimation("walk");
                        walking = true;
                    }
                }
                user.faceRight();
                user.moveRight();
            }

            if (AD) {
                walking = false;
                running = false;
                user.updateCurrentAnimation("idle");
            }
            // player jump (space)
            if (pressedKeys[32]) {
                if (!jump) {
                    user.updateCurrentAnimation("jump");
                    jump = true;
                }
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

        if (!(pressedKeys[65] && pressedKeys[68]) && AD) {
            AD = false;
            user.updateCurrentAnimation("walk");
        }

        if (key == 65 || key == 68) {
            walking = false;
            running = false;
        } else if (key == 16) {
            running = false;
        }

        if (!walking && attacking) {
            if (currentCycle == 0) {
                user.updateCurrentAnimation("attackOne");
            } else if (currentCycle == 1) {
                user.updateCurrentAnimation("attackTwo");
            } else {
                user.updateCurrentAnimation("attackThree");
            }
        }

        if (canMove && !walking && !running && !attacking && !jump) {
            user.updateCurrentAnimation("idle");
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (walking) {
            walking = false;
        }
        if (e.getButton() == MouseEvent.BUTTON1 && !attacking) {
            attacking = true;
//            if (running) {
//                user.updateCurrentAnimation("runAttack");
//            } else {
                if (currentCycle == 0) {
                    user.updateCurrentAnimation("attackOne");
                } else if (currentCycle == 1) {
                    user.updateCurrentAnimation("attackTwo");
                } else {
                    user.updateCurrentAnimation("attackThree");
                }
//            }
            currentCycle = (currentCycle + 1) % 3;
        } else if (e.getButton() == MouseEvent.BUTTON3) {

        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        walking = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        timer.start();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        timer.stop();
    }

    public static void finishedAttack(String animation) {
        if (animation.contains("attack")) {
            attacking = false;
        } else {
            jump = false;
        }
    }
}
