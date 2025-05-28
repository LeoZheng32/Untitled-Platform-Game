import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class CreateSpriteFrames {
    private String name;
    private int totalSprites;

    public CreateSpriteFrames(String name, int totalSprites) {
        this.name = name;
        this.totalSprites = totalSprites;
    }

    public ArrayList<BufferedImage> frames() {
        ArrayList<BufferedImage> frames = new ArrayList<>();
        for (int i = 0; i < totalSprites; i++) {
            String filename = name + i + ".png";
            try {
                frames.add(ImageIO.read(new File(filename)));
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        return frames;
    }
}
