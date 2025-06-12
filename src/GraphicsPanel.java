import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GraphicsPanel extends JPanel implements ActionListener, KeyListener, MouseListener, EnemySpawnHandler {
    private JLabel title;
    private JButton play, controls, backButton, exitButton;
    private BufferedImage currentBackground, background, menuBackground;
    private int enemiesDefeated;
    ArrayList<Enemy> enemies = new ArrayList<>();
    private boolean[] pressedKeys;
    private Timer timer;
    private Player user;
    private Enemy enemy;
    private Portal portal;
    private int currentCycle;
    private boolean isPlay;
    private static boolean walking, running, AD, defend, right, jump, attacking, attackRun, canMove, dead;
    private boolean damageDealt;

    public GraphicsPanel() {
        right = true;
        currentCycle = 0;
        canMove = true;
        pressedKeys = new boolean[128];
        timer = new Timer(2, this);
        timer.start();

        try {
            background = ImageIO.read(new File("src/Resources/background.jpeg"));
            menuBackground = ImageIO.read(new File("src/Resources/gameMenuBackground.jpeg"));
            currentBackground = menuBackground;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        user = new Player();
        enemy = new Enemy(user, this);
        portal = new Portal();
        addKeyListener(this);
        addMouseListener(this);
        setFocusable(true);
        setFocusable(true);

        setLayout(null);

        loadMenu();

        requestFocusInWindow();

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(currentBackground, 0, 0, null);
        if (isPlay) {
            g.drawImage(portal.getImage(), portal.getxCoord(), portal.getyCoord(), null);
            if (enemy != null) {
                enemy.update();
                enemy.draw(g);
            }
            drawHealthBar(g);
            drawKillFeed(g);
            if (!attacking) {
                damageDealt = false;
            }

            if (user.getCurrentHealth() <= 0 && !dead) {
                dead = true;
                canMove = false;
                attacking = false;
                running = false;
                walking = false;
                defend = false;
                jump = false;
                attackRun = false;
                user.updateCurrentAnimation("dead");
            }

            if (user.getCurrentAnimation().animationType().equals("dead")) {
                g.drawImage(user.getPlayerImage(), user.getxCoord(), user.getyCoord() - user.getHeight() + 130, user.getWidth(), user.getHeight(), null);

            } else if (user.getCurrentAnimation().animationType().equals("attackOne")) {
                if (user.getCurrentAnimation().getCurrentFrame() == 4 && right) {
                    g.drawImage(user.getPlayerImage(), user.getxCoord(), user.getyCoord() - 20, user.getWidth(), user.getHeight(), null);
                    if (user.getAttackHitbox().intersects(enemy.getHitbox())) {
                        if (!damageDealt) {
                            enemy.takeDamage(20);
                            damageDealt = true;
                        }
                    }
                } else if (user.getCurrentAnimation().getCurrentFrame() == 4 && !right) {
                    g.drawImage(user.getPlayerImage(), user.getxCoord() - 20, user.getyCoord() - 20, user.getWidth(), user.getHeight(), null);
                    if (!damageDealt) {
                        enemy.takeDamage(20);
                        damageDealt = true;
                    }
                }
                else {
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
                    if (!damageDealt) {
                        enemy.takeDamage(20);
                        damageDealt = true;
                    }
                }
            }

            else if (user.getCurrentAnimation().animationType().equals("attackThree")) {
                if (user.getCurrentAnimation().getCurrentFrame() == 2 && right) {
                    g.drawImage(user.getPlayerImage(), user.getxCoord() + 100, user.getyCoord() + 4, user.getWidth(), user.getHeight(), null);
                    if (!damageDealt) {
                        enemy.takeDamage(20);
                        damageDealt = true;
                    }
                } else if (user.getCurrentAnimation().getCurrentFrame() == 2 && !right) {
                    g.drawImage(user.getPlayerImage(), user.getxCoord() - 65, user.getyCoord() + 4, user.getWidth(), user.getHeight(), null);
                    if (!damageDealt) {
                        enemy.takeDamage(20);
                        damageDealt = true;
                    }
                } else if (user.getCurrentAnimation().getCurrentFrame() == 3 && right) {
                    g.drawImage(user.getPlayerImage(), user.getxCoord() + 54, user.getyCoord() + 10, user.getWidth(), user.getHeight(), null);
                } else if ((user.getCurrentAnimation().getCurrentFrame() == 3) && !right) {
                    g.drawImage(user.getPlayerImage(), user.getxCoord() - 54, user.getyCoord() + 10, user.getWidth(), user.getHeight(), null);
                } else {
                    g.drawImage(user.getPlayerImage(), user.getxCoord(), user.getyCoord(), user.getWidth(), user.getHeight(), null);
                }
            }
            else if (user.getCurrentAnimation().animationType().equals("runAttack")) {
                int currentFrame = user.getCurrentAnimation().getCurrentFrame();
                if (!damageDealt) {
                    enemy.takeDamage(20);
                    damageDealt = true;
                }
                if (currentFrame == 0 && right) {
                    g.drawImage(user.getPlayerImage(), user.getxCoord() + 30, user.getyCoord() - 30, user.getWidth(), user.getHeight(), null);
                } else if (currentFrame == 0) {
                    g.drawImage(user.getPlayerImage(), user.getxCoord() - 30, user.getyCoord() - 30, user.getWidth(), user.getHeight(), null);
                } else if (currentFrame == 1 && right) {
                    g.drawImage(user.getPlayerImage(), user.getxCoord() + 30, user.getyCoord() - 15, user.getWidth(), user.getHeight(), null);
                } else if (currentFrame == 1) {
                    g.drawImage(user.getPlayerImage(), user.getxCoord() - 30, user.getyCoord() - 15, user.getWidth(), user.getHeight(), null);
                }
                else if (currentFrame == 2 && right) {
                    g.drawImage(user.getPlayerImage(), user.getxCoord() + 30, user.getyCoord() - 10, user.getWidth(), user.getHeight(), null);
                } else if (currentFrame == 2) {
                    g.drawImage(user.getPlayerImage(), user.getxCoord() - 30, user.getyCoord() - 10, user.getWidth(), user.getHeight(), null);
                } else if (currentFrame == 3 && right) {
                    g.drawImage(user.getPlayerImage(), user.getxCoord() + 20, user.getyCoord() - 5, user.getWidth(), user.getHeight(), null);
                } else if (currentFrame == 3) {
                    g.drawImage(user.getPlayerImage(), user.getxCoord() - 20, user.getyCoord() - 5, user.getWidth(), user.getHeight(), null);
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

            if (!dead && canMove) {
                if (attackRun && (!pressedKeys[65] && !pressedKeys[68])) {
                    attackRun = false;
                    attacking = false;
                    user.updateCurrentAnimation("idle");
                }

                // player jump (space)
                if (pressedKeys[32] && !jump) {
                    if (!AD) {
                        if (pressedKeys[65]) {
                            user.faceLeft();
                            user.updateCurrentAnimation("jump", "left", pressedKeys[16]);
                            jump = true;
                        } else if (pressedKeys[68]) {
                            user.faceRight();
                            user.updateCurrentAnimation("jump", "right", pressedKeys[16]);
                            jump = true;
                        } else {
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

                if (AD && !jump && !attacking && !attackRun) {
                    walking = false;
                    running = false;
                    user.updateCurrentAnimation("idle");
                }
            }
            if (dead) {
                loadEndMenu();
            }
        }
    }

    public static boolean getDefending() {
        return defend;
    }

    public void loadMenu() {
        title = new JLabel("Endless Onslaught");
        title.setFont(new Font("Monospaced", Font.BOLD, 60));
        title.setForeground(Color.BLACK);
        title.setBounds(125, 65, 650, 65);
        title.setOpaque(true);
        title.setBackground(new Color(255, 255, 255, 180));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title);

        play = new JButton("Play");
        play.setBounds(360, 160, 150, 60);
        play.addActionListener(this);
        play.setFont(new Font("Monospaced", Font.BOLD, 40));
        play.setForeground(Color.BLACK);
        play.setContentAreaFilled(false);
        play.setBorderPainted(true);
        play.setFocusPainted(false);
        play.setOpaque(true);
        play.setBackground(new Color(255, 255, 255, 180));
        add(play);


        controls = new JButton("Control");
        controls.setBounds(310, 250, 250, 60);
        controls.addActionListener(this);
        controls.setFont(new Font("Monospaced", Font.BOLD, 40));
        controls.setForeground(Color.BLACK);
        controls.setContentAreaFilled(false);
        controls.setBorderPainted(true);
        controls.setFocusPainted(false);
        controls.setOpaque(true);
        controls.setBackground(new Color(255, 255, 255, 180));
        add(controls);
    }

    public void loadControls() {
        this.removeAll();

        backButton = new JButton("Back");
        backButton.setBounds(20, 20, 100, 40);
        backButton.setFont(new Font("Monospaced", Font.PLAIN, 20));
        backButton.setForeground(Color.BLACK);
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(true);
        backButton.setFocusPainted(false);
        backButton.setOpaque(true);
        backButton.setBackground(new Color(255, 255, 255, 180));
        backButton.addActionListener(this);
        add(backButton);

        JLabel subHeading = new JLabel("Controls");
        subHeading.setFont(new Font("Monospaced", Font.BOLD, 60));
        subHeading.setForeground(Color.BLACK);
        subHeading.setBounds(290, 45, 310, 65);
        subHeading.setHorizontalAlignment(SwingConstants.CENTER);
        subHeading.setOpaque(true);
        subHeading.setBackground(new Color(255, 255, 255, 180));

        JLabel controls = new JLabel("<html>" +
                "Move Left: A<br>" +
                "Move Right: D<br>" +
                "Run: Sprint Movement Key<br>" +
                "Jump: Space<br>" +
                "Attack: Left Click<br>" +
                "Running Attack: Movement Key + Shift + Left<br>" +
                "Defend: Right Click" +
                "</html>");
        controls.setFont(new Font("Monospaced", Font.BOLD, 30));
        controls.setForeground(Color.BLACK);
        controls.setBounds(45, 140, 800, 330);
        controls.setOpaque(true);
        controls.setBackground(new Color(255, 255, 255, 180));
        controls.setHorizontalAlignment(SwingConstants.CENTER);
        add(subHeading);
        add(controls);
        repaint();
    }

    public void loadEndMenu() {
        this.removeAll();

        JLabel gameOverLabel = new JLabel("GAME OVER");
        gameOverLabel.setFont(new Font("Monospaced", Font.BOLD, 70));
        gameOverLabel.setForeground(Color.RED);
        gameOverLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gameOverLabel.setBounds(150, 80, 500, 80);
        gameOverLabel.setOpaque(true);
        gameOverLabel.setBackground(new Color(0, 0, 0, 180));
        add(gameOverLabel);

        JLabel killsLabel = new JLabel("Total Kills: " + enemiesDefeated);
        killsLabel.setFont(new Font("Monospaced", Font.BOLD, 40));
        killsLabel.setForeground(Color.WHITE);
        killsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        killsLabel.setBounds(200, 200, 400, 60);
        killsLabel.setOpaque(true);
        killsLabel.setBackground(new Color(0, 0, 0, 180));
        add(killsLabel);
    }

    private void drawHealthBar(Graphics g) {
        int barWidth = 200;
        int barHeight = 25;
        int x = 20;
        int y = 20;

        double healthPercent = user.getCurrentHealth() / (double) user.getMaxHealth();

        g.setColor(Color.DARK_GRAY);
        g.fillRect(x, y, barWidth, barHeight);

        int scaledWidth = (int) (healthPercent * barWidth);
        g.setColor(Color.RED);
        g.fillRect(x, y, scaledWidth, barHeight);

        g.setColor(Color.BLACK);
        g.drawRect(x, y, barWidth, barHeight);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        String percentage = (int)(healthPercent * 100) + "%";
        g.drawString("HP: " + percentage, x + 5, y + 17);
    }

    private void drawKillFeed(Graphics g) {
        int x = getWidth() - 220;
        int y = 20;
        int width = 200;
        int height = 30;

        g.setFont(new Font("Monospaced", Font.BOLD, 16));
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRoundRect(x, y, width, height, 10, 10);

        g.setColor(Color.WHITE);
        g.drawString("Kills: " + enemiesDefeated, x + 15, y + 20);
    }

    public static void setCanMove(boolean a) {
        canMove = a;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == play) {
            removeAll();
            currentBackground = background;
            isPlay = true;
        } else if (source == controls) {
            loadControls();
        }

        if (source == backButton) {
            removeAll();
            loadMenu();
        }
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (dead) return;
        int key = e.getKeyCode();
        pressedKeys[key] = true;

        if (pressedKeys[65] && pressedKeys[68]) {
            AD = true;
        } else {
            AD = false;
        }
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

        if (canMove && !walking && !running && !attacking && !jump && !dead) {
            user.updateCurrentAnimation("idle");
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
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
        if (e.getButton() == MouseEvent.BUTTON3 && user.getCurrentAnimation().animationType().equals("defend")) {
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
        if (animation.equals("runAttack")) {
            attackRun = false;
            attacking = false;
            resetBoolean();
        } else if (animation.contains("attack")) {
            attacking = false;
        } else {
            jump = false;
        }
    }

    @Override
    public void spawnNewEnemy() {
        enemy = new Enemy(user, this);
        enemiesDefeated++;
    }
}
