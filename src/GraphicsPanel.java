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
    private static boolean walking, running, AD, defend;
    private static boolean right, jump, attacking, attackRun;

    public GraphicsPanel() {
        right = true;
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
        }

        else if (user.getCurrentAnimation().animationType().equals("attackTwo")) {
            if (user.getCurrentAnimation().getCurrentFrame() == 0) {
                g.drawImage(user.getPlayerImage(), user.getxCoord(), user.getyCoord() - 22, user.getWidth(), user.getHeight(), null);
            } else if (user.getCurrentAnimation().getCurrentFrame() == 1) {
                g.drawImage(user.getPlayerImage(), user.getxCoord(), user.getyCoord() - 14, user.getWidth(), user.getHeight(), null);
            } else {
                g.drawImage(user.getPlayerImage(), user.getxCoord(), user.getyCoord(), user.getWidth(), user.getHeight(), null);
            }
        }

        else if (user.getCurrentAnimation().animationType().equals("attackThree")) {
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
        }

        else if (user.getCurrentAnimation().animationType().equals("jump")) {
            if (!right) {
                if (user.getCurrentAnimation().getCurrentFrame() == 3) {
                    g.drawImage(user.getPlayerImage(), user.getxCoord() - 57, user.getyCoord(), user.getWidth(), user.getHeight(), null);
                } else if (user.getCurrentAnimation().getCurrentFrame() == 4) {
                    g.drawImage(user.getPlayerImage(), user.getxCoord() - 48, user.getyCoord(), user.getWidth(), user.getHeight(), null);
                } else if (user.getCurrentAnimation().getCurrentFrame() == 5) {
                    g.drawImage(user.getPlayerImage(), user.getxCoord() - 42, user.getyCoord(), user.getWidth(), user.getHeight(), null);
                } else {
                    g.drawImage(user.getPlayerImage(), user.getxCoord(), user.getyCoord(), user.getWidth(), user.getHeight(), null);
                }
            } else {
                g.drawImage(user.getPlayerImage(), user.getxCoord(), user.getyCoord(), user.getWidth(), user.getHeight(), null);
            }
        }

        else {
            g.drawImage(user.getPlayerImage(), user.getxCoord(), user.getyCoord(), user.getWidth(), user.getHeight(), null);

        }

        if (canMove) {
            if (pressedKeys[65] && pressedKeys[68]) {
                AD = true;
            }

            // player jump (space)
            if (pressedKeys[32] && !jump) {
                if (!AD) {
                    if (pressedKeys[65]) {
                        user.faceLeft();
                        user.updateCurrentAnimation("jump", "left", pressedKeys[16]);
                        jump = true;
                    }

                    else if (pressedKeys[68]) {
                        user.faceRight();
                        user.updateCurrentAnimation("jump", "right", pressedKeys[16]);
                        jump = true;
                    }

                    else {
                        user.updateCurrentAnimation("jump", "still", true);
                        jump = true;
                    }
                }

            }

            // player moves left (A)
            if (pressedKeys[65] && !attacking && !AD && !jump) {
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
            if (pressedKeys[68] && !attacking && !AD && !jump) {
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

            if (AD && !jump) {
                walking = false;
                running = false;
                user.updateCurrentAnimation("idle");
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

        if (!(pressedKeys[65] && pressedKeys[68]) && AD && !jump) {
            AD = false;
            user.updateCurrentAnimation("walk");
        }

        if (key == 65 || key == 68) {
            walking = false;
            running = false;
        } else if (key == 16) {
            running = false;
            //if added walking true then it does the attack on a/d release
        }

        if (!walking && attacking && !attackRun) {
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
//        unsure if this does anything
//        if (walking) {
//            walking = false;
//        }
        if (e.getButton() == MouseEvent.BUTTON1 && !attacking && !jump && !defend) {
            attacking = true;
            if (running) {
                attackRun = true;
                user.updateCurrentAnimation("runAttack");
            } else {
                if (currentCycle == 0) {
                    user.updateCurrentAnimation("attackOne");
                } else if (currentCycle == 1) {
                    user.updateCurrentAnimation("attackTwo");
                } else {
                    user.updateCurrentAnimation("attackThree");
                }
                currentCycle = (currentCycle + 1) % 3;
            }
        } else if (e.getButton() == MouseEvent.BUTTON3 && !walking && !running && !jump) {
            defend = true;
            user.updateCurrentAnimation("defend");
        }
    }

    public static void resetBoolean() {
        walking = false;
        running = false;
        AD = false;
        defend = false;
        jump = false;
        attacking = false;
        attackRun = false;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (user.getCurrentAnimation().animationType().equals("defend")) {
            defend = false;
            user.updateCurrentAnimation("idle");
        }
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
        } else if (animation.equals("runAttack")) {
            attackRun = false;
            resetBoolean();
        }
        else {
            jump = false;
        }
    }
}
