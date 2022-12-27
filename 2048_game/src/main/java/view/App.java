package view;

import javax.swing.JFrame;

import model.Game;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

/**
 *
 *
 * @author g54915
 */
public class App {

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setTitle("G54915 - 2048");
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setSize(1000, 750);
        frame.setResizable(true);
        frame.add(new Game());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
