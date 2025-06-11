import java.awt.*;
import java.awt.image.BufferedImage;

public class Enemy implements AnimationHandler {
    private int xCoord;
    private int yCoord;
    private int moveSpeed;
    private boolean facingRight;
    private Animation currentAnimation;
    private Player player;
    private boolean defending;

    private int attackRange = 80;
    private int maxHealth = 100;
    private int currentHealth = 100;

    public Enemy(Player player) {
        this.player = player;
        xCoord = 800;  // start on the right
        yCoord = 400;
        moveSpeed = 2;
        facingRight = false;
        defending = false;

        createAnimation("idle");
    }

    public void update() {
        int playerX = player.getxCoord();
        int distance = Math.abs(playerX - xCoord);

        if (distance > attackRange) {
            moveTowardPlayer(playerX);
            createAnimation("walk");
        } else {
            createAnimation("attack");
        }
    }

    public void moveTowardPlayer(int playerX) {
        if (xCoord > playerX) {
            xCoord -= moveSpeed;
            facingRight = false;
        } else {
            xCoord += moveSpeed;
            facingRight = true;
        }
    }

    public void getHit() {
        defending = true;
        createAnimation("defend");
        currentHealth -= 10;
    }

    public BufferedImage getImage() {
        return currentAnimation.getActiveFrame();
    }

    public int getxCoord() {
        return facingRight ? xCoord : xCoord + getImage().getWidth();
    }

    public int getyCoord() {
        return yCoord;
    }

    public int getWidth() {
        return facingRight ? getImage().getWidth() : -getImage().getWidth();
    }

    public int getHeight() {
        return getImage().getHeight();
    }

    public Rectangle enemyRect() {
        return new Rectangle(xCoord, yCoord, getImage().getWidth(), getImage().getHeight());
    }

    private void createAnimation(String type) {
        if (currentAnimation != null && currentAnimation.animationType().equals(type)) return;

        switch (type) {
            case "walk":
                currentAnimation = new Animation(new CreateSpriteFrames("src/Resources/enemyWalk00", 6).frames(), "walk", 250);
                break;
            case "attack":
                currentAnimation = new Animation(new CreateSpriteFrames("src/Resources/enemyAttack00", 5).frames(), "attack", 150, this);
                break;
            case "idle":
                currentAnimation = new Animation(new CreateSpriteFrames("src/Resources/enemyIdle00", 4).frames(), "idle", 400);
                break;
            case "defend":
                currentAnimation = new Animation(new CreateSpriteFrames("src/Resources/enemyDefend00", 4).frames(), "defend", 120, this);
                break;
        }
    }

    public void draw(Graphics g) {
        BufferedImage image = getImage();
        int drawX = facingRight ? xCoord : xCoord + image.getWidth();
        int drawWidth = facingRight ? image.getWidth() : -image.getWidth();
        g.drawImage(image, drawX, yCoord, drawWidth, image.getHeight(), null);
    }

    @Override
    public void animationCompleted(String animationName) {
        if (animationName.equals("attack") || animationName.equals("defend")) {
            createAnimation("idle");
            defending = false;
        }
    }

    @Override
    public void jump(int currentFrame, String direction, boolean sprint) {
        // Enemy does not jump in this version
    }

    @Override
    public void runAttacKMove() {
        // Enemy doesn't perform run-attacks in this version
    }

    public int getHealth() {
        return currentHealth;
    }

    public int getMaxHealth() {
        return maxHealth;
    }
}