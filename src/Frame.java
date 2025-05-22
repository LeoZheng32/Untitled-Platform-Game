import javax.swing.JFrame;

public class Frame {
    JFrame frame;
    public Frame() {
        frame = new JFrame("Untitled Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        frame.setVisible(true);

    }
}
