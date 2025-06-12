import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Player implements AnimationHandler {
    private int moveAMT = 1;
    private boolean facingRight;
    private int xCoord;
    private int yCoord;
    private int maxHealth = 100;
    private int currentHealth = 100;

    private Animation currentAnimation;
    private Animation idle;
    private Animation attackOne;
    private Animation attackTwo;
    private Animation attackThree;
    private Animation dead;
    private Animation defend;

    private static boolean finishedAnimation;

    public Player() {
        finishedAnimation = false;
        createAnimation("idle");
        facingRight = true;
        xCoord = 30;
        yCoord = 400;
    }

    public static void setFinishedAnimation() {
        finishedAnimation = true;
    }

    public Animation getCurrentAnimation() {return currentAnimation;}

    public void updateCurrentAnimation(String animation) {
        createAnimation(animation);
    }

    public void updateCurrentAnimation(String animation, String direction, boolean sprint) {
        createAnimation(animation, direction, sprint);
    }

    public void resetAnimation() {
        updateCurrentAnimation("idle");
    }

    //This function is changed from the previous version to let the player turn left and right
    //This version of the function, when combined with getWidth() and getHeight()
    //Allow the player to turn without needing separate images for left and right
    public int getxCoord() {
        if (facingRight) {
            return xCoord;
        } else {
            return (xCoord + (getPlayerImage().getWidth()));
        }
    }

    public int getyCoord() {
        return yCoord;
    }

    //These functions are newly added to let the player turn left and right
    //These functions when combined with the updated getxCoord()
    //Allow the player to turn without needing separate images for left and right
    public int getHeight() {
        return getPlayerImage().getHeight();
    }

    public int getWidth() {
        if (facingRight) {
            return getPlayerImage().getWidth();
        } else {
            return getPlayerImage().getWidth() * -1;
        }
    }

    public void faceRight() {
        facingRight = true;
    }

    public void faceLeft() {
        facingRight = false;
    }



    public void moveRight() {
        if (xCoord + moveAMT <= 900 - getWidth()) {
            xCoord += moveAMT;
        }
    }

    public void moveRight(boolean sprint) {
        if (sprint) {
            if (xCoord + moveAMT <= 890 - getWidth()) {
                xCoord += 25;
            }
        } else {
            if (xCoord + moveAMT <= 880 - getWidth()) {
                xCoord += 10;
            }
        }
    }

    public void moveLeft() {
        if (xCoord - moveAMT >= 0) {
            xCoord -= moveAMT;
        }
    }

    public void moveLeft(boolean sprint) {
        if (xCoord - moveAMT >= 0) {
            if (!sprint) {
                xCoord -= 10;
            } else {
                xCoord -= 25;
            }
        }
    }

    public void moveUp() {
        yCoord-= 25;
    }

    public void moveDown() {
        yCoord+= 25;
    }

    public BufferedImage getPlayerImage() {
        return currentAnimation.getActiveFrame();
    }

    public Rectangle playerRect() {
        int imageHeight = getPlayerImage().getHeight();
        int imageWidth = getPlayerImage().getWidth();
        Rectangle rect = new Rectangle(xCoord, yCoord, imageWidth, imageHeight);
        return rect;
    }

    private void createAnimation(String animationType) {
        if (animationType.equals("idle")) {
            endTimer();
            moveAMT = 1;
            currentAnimation = new Animation((new CreateSpriteFrames("src/Resources/idle00", 4)).frames(), "idle", 450);
        } else if (animationType.equals("attackOne")) {
            endTimer();
            currentAnimation = new Animation(new CreateSpriteFrames("src/Resources/attackOne00", 5).frames(), "attackOne", 110, this);
        } else if (animationType.equals("attackTwo")) {
            endTimer();
            currentAnimation = new Animation(new CreateSpriteFrames("src/Resources/attackTwo00", 4).frames(), "attackTwo", 110, this);
        } else if (animationType.equals("attackThree")) {
            endTimer();
            currentAnimation = new Animation(new CreateSpriteFrames("src/Resources/attackThree00", 4).frames(), "attackThree", 110, this);
        } else if (animationType.equals("runAttack")) {
            endTimer();
            currentAnimation = new Animation(new CreateSpriteFrames("src/Resources/runAttack00", 6).frames(), "runAttack", 110, this);
        } else if (animationType.equals("dead")) {
            endTimer();
            currentAnimation = new Animation(new CreateSpriteFrames("src/Resources/dead00", 6).frames(), "dead", 475);
        } else if (animationType.equals("walk")) {
            endTimer();
            moveAMT = 1;
            currentAnimation = new Animation(new CreateSpriteFrames("src/Resources/walk00", 8).frames(), "walk", 225);
        } else if (animationType.equals("run")) {
            endTimer();
            moveAMT = 2;
            currentAnimation = new Animation(new CreateSpriteFrames("src/Resources/run00", 7).frames(), "run", 350);
        } else if (animationType.equals("defend")) {
            endTimer();
            currentAnimation = new Animation(new CreateSpriteFrames("src/Resources/defend00", 5).frames(), "defend", 100);
        }
    }

    public void createAnimation(String animationType, String direction, boolean sprint) {
        if (direction.equals("left")) {
            endTimer();
            currentAnimation = new Animation(new CreateSpriteFrames("src/Resources/jump00", 6).frames(), animationType, 75, this, direction, sprint);
        } else if (direction.equals("right")) {
            endTimer();
            currentAnimation = new Animation(new CreateSpriteFrames("src/Resources/jump00", 6).frames(), animationType, 75, this, direction, sprint);
        } else {
            endTimer();
            currentAnimation = new Animation(new CreateSpriteFrames("src/Resources/jump00", 6).frames(), animationType, 75, this, direction, sprint);
        }
    }

    private void endTimer() {
        if (currentAnimation != null) {
            currentAnimation.endTimer();
        }
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public void takeDamage(int amount) {
        currentHealth -= amount;
        if (currentHealth < 0) currentHealth = 0;
    }

    public Rectangle getAttackHitbox() {
        int attackRange = 40;
        if (facingRight) {
            return new Rectangle(xCoord + getWidth(), yCoord, attackRange, getHeight());
        } else {
            return new Rectangle(xCoord - attackRange, yCoord, attackRange, getHeight());
        }
    }

    @Override
    public void animationCompleted(String animation) {
        endTimer();
        createAnimation("idle");
        GraphicsPanel.finishedAttack(animation);

    }

    @Override
    public void jump(int currentFrame, String direction, boolean sprint) {
        if (currentFrame < 3) {
            moveUp();
        }
        else {
            moveDown();
        }

        if (direction.equals("right")) {
            moveRight(sprint);
        }
        if (direction.equals("left")) {
            moveLeft(sprint);
        }
    }

    @Override
    public void runAttacKMove() {
        if (facingRight) {
            moveRight(true);
        } else {
            moveLeft(true);
        }
    }
}