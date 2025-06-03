import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Animation implements ActionListener {

    private ArrayList<BufferedImage> frames;
    private Timer timer;
    private int currentFrame;
    private String animationType;
    private AnimationHandler handler;
    private String direction;
    private boolean sprint;

    public Animation(ArrayList<BufferedImage> frames, String animationType, int delay) {
        this.frames = frames;
        this.animationType = animationType;
        currentFrame = 0;
        timer = new Timer(delay, this);
        timer.start();
    }


    public Animation(ArrayList<BufferedImage> frames, String animationType, int delay, AnimationHandler handler) {
        this.frames = frames;
        this.animationType = animationType;
        this.handler = handler;
        currentFrame = 0;
        timer = new Timer(delay, this);
        timer.start();
    }

    public Animation(ArrayList<BufferedImage> frames, String animationType, int delay, AnimationHandler handler, String direction, boolean sprint) {
        this.frames = frames;
        this.animationType = animationType;
        this.handler = handler;
        this.direction = direction;
        this.sprint = sprint;
        currentFrame = 0;
        timer = new Timer(delay, this);
        timer.start();
    }

    public void endTimer() {
        timer.stop();
    }

    public String animationType() {
        return animationType;
    }

    public int getCurrentFrame() {
        return currentFrame;
    }

    public BufferedImage getActiveFrame() {
        return frames.get(currentFrame);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof Timer) {
            //This advances the animation to the next frame
            //It also uses modulus to reset the frame to the beginning after the last frame
            //In other words, this allows our animation to loop
            if (!animationType.equals("dead") && !animationType.contains("attack") && !animationType.equals("runAttack") && !animationType.equals("jump")) {
                currentFrame = (currentFrame + 1) % frames.size();
            } else {
                if (animationType.equals("dead")) {
                    GraphicsPanel.setCanMove(false);
                }
                if (animationType.equals("jump")) {
                    handler.jump(currentFrame, direction, sprint);
                }

                if (currentFrame != frames.size()-1) {
                    currentFrame = (currentFrame + 1);
                }
                else if (animationType.contains("attack") || animationType.equals("jump")) {
                    if (handler != null) {
                        handler.animationCompleted(animationType);
                    }
                }
            }
        }
    }

}
