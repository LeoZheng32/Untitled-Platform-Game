import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Enemy implements AnimationHandler {
    private EnemySpawnHandler spawnHandler;
    private int xCoord;
    private int yCoord;
    private boolean isDead;
    private boolean facingRight;
    private Animation currentAnimation;
    private Player player;
    private boolean animationCompleted;
    private boolean hasDealtDamage = false;
    private boolean hasSpawnedReplacement = false;
    private int attackRange = 80;
    private int maxHealth = 100;
    private int currentHealth = 100;

    public Enemy(Player player, EnemySpawnHandler spawnHandler) {
        animationCompleted = true;
        this.player = player;
        this.spawnHandler = spawnHandler;
        xCoord = 800;
        yCoord = 400;
        facingRight = false;
        createAnimation("idle");
    }

    public void update() {
        if (!isDead) {
            int playerX = player.getxCoord();
            int distance = Math.abs(playerX - xCoord);

            int playerCenterX = player.getxCoord() + player.getWidth() / 2;
            facingRight = xCoord < playerCenterX;

            if (isAnimation("attack")) {
                int currentFrame = currentAnimation.getCurrentFrame();

                if ((currentFrame == 4 || currentFrame == 8) && !hasDealtDamage && distance <= attackRange) {
                    if (!GraphicsPanel.getDefending()) {
                        player.takeDamage(10);
                    }
                    hasDealtDamage = true;
                }

                if (!animationCompleted) {
                    return;
                }
            }

            if (animationCompleted) {
                hasDealtDamage = false;
            }
            if (distance > attackRange) {
                if (!isAnimation("walk")) {
                    createAnimation("walk");
                }
                moveTowardPlayer(playerX);
            } else {
                if (!isAnimation("attack")) {
                    createAnimation("attack");
                    animationCompleted = false;
                }
            }
        }
    }

    public void moveTowardPlayer(int playerX) {
        if (xCoord > playerX + attackRange / 2) {
            moveLeft();
        } else if (xCoord < playerX - attackRange / 2) {
            moveRight();
        }
    }

    public void moveRight() {
        xCoord+=2;
        facingRight = true;
    }

    public void moveLeft() {
        xCoord-=2;
        facingRight = false;
    }

    public void takeDamage(int amount) {
        currentHealth -= amount;
        if (currentHealth <= 0) {
            isDead = true;
            createAnimation("dead");
        }
    }

    public BufferedImage getImage() {
        return currentAnimation.getActiveFrame();
    }

    public int getWidth() {
        return facingRight ? getImage().getWidth() : -getImage().getWidth();
    }

    public int getHeight() {
        return getImage().getHeight();
    }

    public Rectangle getHitbox() {
        return new Rectangle(xCoord, yCoord, getImage().getWidth(), getImage().getHeight());
    }

    private void createAnimation(String type) {
        if (type.equals("walk")) {
            endTimer();
            animationCompleted = false;
            currentAnimation = new Animation(new CreateSpriteFrames("src/Resources/mobWalk00", 4).frames(), "walk", 250);
        } else if (type.equals("attack")) {
            endTimer();
            currentAnimation = new Animation(new CreateSpriteFrames("src/Resources/attack00", 8).frames(), "attack", 150, this);
        } else if (type.equals("idle")) {
            endTimer();
            currentAnimation = new Animation(new CreateSpriteFrames("src/Resources/mobIdle00", 4).frames(), "idle", 400);
        } else if (type.equals("dead")) {
            if (!currentAnimation.animationType().equals("Dead")) {
                endTimer();
                currentAnimation = new Animation(new CreateSpriteFrames("src/Resources/mobDead00", 7).frames(), "Dead", 200, this);
            }
        }
    }

    public void draw(Graphics g) {
        BufferedImage image = getImage();
        int frame = currentAnimation.getCurrentFrame();

        if (isAnimation("dead")) {
            g.drawImage(image, xCoord, yCoord, getWidth(), getHeight(), null);
        }

        else if (isAnimation("attack")) {
            if (frame == 5 && facingRight) {
                g.drawImage(image, xCoord + 20, yCoord - 30, getWidth(), getHeight(), null);
            } else if (frame == 5) {
                g.drawImage(image, xCoord - 20, yCoord - 30, getWidth(), getHeight(), null);
            } else {
                g.drawImage(image, xCoord, yCoord, getWidth(), getHeight(), null);
            }
        }

        else if (isAnimation("walk")) {
            g.drawImage(image, xCoord, yCoord, getWidth(), getHeight(), null);
        }

        else if (isAnimation("idle")) {
            g.drawImage(image, xCoord, yCoord, getWidth(), getHeight(), null);
        }

        else {
            g.drawImage(image, xCoord, yCoord, getWidth(), getHeight(), null);
        }
    }

    @Override
    public void animationCompleted(String animationName) {
        if (animationName.equals("attack") || animationName.equals("defend")) {
            createAnimation("idle");
            animationCompleted = true;
        } else if (animationName.equals("Dead") && !hasSpawnedReplacement) {
            animationCompleted = true;
            hasSpawnedReplacement = true;
            if (spawnHandler != null) {
                spawnHandler.spawnNewEnemy();
            }
        }
    }

    @Override
    public void jump(int currentFrame, String direction, boolean sprint) {}

    @Override
    public void runAttacKMove() {}

    public int getHealth() {
        return currentHealth;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    private void endTimer() {
        if (currentAnimation != null) {
            currentAnimation.endTimer();
        }
    }

    private boolean isAnimation(String name) {
        return currentAnimation != null && currentAnimation.animationType().equals(name);
    }
}