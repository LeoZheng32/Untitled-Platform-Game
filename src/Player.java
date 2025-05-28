import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Player {
    private int moveAMT = 1;
    private BufferedImage right;
    private BufferedImage left;
    private boolean facingRight;
    private int xCoord;
    private int yCoord;
    private Animation idleAnimation;

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

    public void moveLeft() {
        if (xCoord - moveAMT >= 0) {
            xCoord -= moveAMT;
        }
    }

    public void moveUp() {
        if (yCoord - moveAMT >= 0) {
            yCoord -= moveAMT;
        }
    }

    public void moveDown() {
        if (yCoord + moveAMT <= 400) {
            yCoord += moveAMT;
        }
    }

    public BufferedImage getPlayerImage() {
        return currentAnimation.getActiveFrame();
    }

    // we use a "bounding Rectangle" for detecting collision
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
            currentAnimation = new Animation(new CreateSpriteFrames("src/Resources/attackOne00", 5).frames(), "attackOne", 100);
            if (finishedAnimation) {
                endTimer();
                updateCurrentAnimation("idle");
            }
        } else if (animationType.equals("attackTwo")) {
            endTimer();
            currentAnimation = new Animation(new CreateSpriteFrames("src/Resources/attackTwo00", 4).frames(), "attackTwo", 100);
        } else if (animationType.equals("attackThree")) {
            endTimer();
            currentAnimation = new Animation(new CreateSpriteFrames("src/Resources/attackThree00", 4).frames(), "attackThree", 100);
        } else if (animationType.equals("dead")) {
            endTimer();
            currentAnimation = new Animation(new CreateSpriteFrames("src/Resources/dead00", 6).frames(), "dead", 500);
        } else if (animationType.equals("walk")) {
            endTimer();
            moveAMT = 1;
            currentAnimation = new Animation(new CreateSpriteFrames("src/Resources/walk00", 8).frames(), "walk", 250);
        } else if (animationType.equals("run")) {
            endTimer();
            moveAMT = 2;
            currentAnimation = new Animation(new CreateSpriteFrames("src/Resources/run00", 7).frames(), "run", 350);
        }
        else if (animationType.equals("defend")) {
            endTimer();
            currentAnimation = new Animation(new CreateSpriteFrames("src/Resources/defend00", 5).frames(), "defend", 100);
        }
    }

    private void endTimer() {
        if (currentAnimation != null) {
            currentAnimation.endTimer();
        }
    }

}