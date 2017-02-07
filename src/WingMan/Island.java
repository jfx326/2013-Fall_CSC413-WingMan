package WingMan;


import java.awt.*;
import java.awt.image.*;
import java.util.Random;

public class Island {

    private Image img;
    private int x, y;
    private Random gen;

    Island(Image img, int x, int y, Random gen) {
        this.img = img;
        this.x = x;
        this.y = y;
        this.gen = gen;
    }

    public void update(int w, int h) {
        y += 1;
        if (y >= h) { // respawn in new x pos. once offscreen
            y = -100;
            x = Math.abs(gen.nextInt() % (w - 30));
        }
    }

    public void draw(Graphics g, ImageObserver obs) {
        g.drawImage(img, x, y, obs);
    }
}