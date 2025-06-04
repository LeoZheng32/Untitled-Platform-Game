public interface AnimationHandler {
    void animationCompleted(String animation);

    void jump(int currentFrame, String direction, boolean sprinting);

    void runAttacKMove();
}
