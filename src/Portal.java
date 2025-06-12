import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class Portal {
    private int xCoord, yCoord;
    private Animation portalAnimation;
    public Portal() {
        xCoord = 750;
        yCoord = 350;
        portalAnimation = new Animation(new CreateSpriteFrames("src/Resources/portal00", 6).frames(), "portal", 250);
    }

    public int getxCoord() {
        return xCoord;
    }

    public int getyCoord() {
        return yCoord;
    }

    public BufferedImage getImage() {
        return portalAnimation.getActiveFrame();
    }


}
