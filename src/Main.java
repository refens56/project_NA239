import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Main extends JFrame {
    public BufferedImage pers;
    Main(){
        try {
            pers = ImageIO.read(new File("H:/пнг.png"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        setSize(800,600);
        setVisible(true);
    }
    public void paint(Graphics g){

        super.paint(g);
        g.drawImage(pers, 100, 100, this);
    }
    public static void main(String[] args){
        new Main();
    }

}