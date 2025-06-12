import javax.swing.JFrame;

public class Frame {
    JFrame frame;

    public Frame() {
        frame = new JFrame("Endless Onslaught");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        GraphicsPanel displayPanel = new GraphicsPanel();

        frame.add(displayPanel);

        frame.setVisible(true);
    }
}
