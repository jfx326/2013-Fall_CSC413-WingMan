package WingMan;


import java.awt.*;
import java.awt.image.*;
import java.util.Random;

public class PowerUp {

    private Image img;
    private int x, y, w, h;
    private boolean power; // triple bullet is true; health is false
    private ImageObserver obs;
    private Random gen;

    PowerUp(Image img, int x, int y, boolean power, Random gen) {
        this.img = img;
        this.x = x;
        this.y = y;
        this.power = power;
        this.gen = gen;
        w = img.getHeight(obs);
        h = img.getWidth(obs);
    }

    public void draw(Graphics g, ImageObserver obs) {
        this.obs = obs;
        g.drawImage(img, x, y, obs);
    }

    public void update() {
        y += 1; // slow downward direction
        
        // respawn power-up if offscreen; p1, p2 acquires it
        if (y >= 560) {
            x = gen.nextInt(600);
            if (power) { // triple bullets appear more often
                y = -3200;
            } else { // health packs don't
                y = -4800;
            }
        } else if (WingMan.p1.collide(x, y, w, h)) {
            x = gen.nextInt(600);
            if (power) {
                WingMan.gameEvents.setValue("P1GotPowerUp");
                y = -3200;
            } else {
                WingMan.gameEvents.setValue("P1GotHealth");
                y = -4800;
            }
        } else if (WingMan.p2.collide(x, y, w, h)) {
            x = gen.nextInt(600);
            if (power) {
                WingMan.gameEvents.setValue("P2GotPowerUp");
                y = -3200;
            } else {
                WingMan.gameEvents.setValue("P2GotHealth");
                y = -4800;
            }
        }
    }
    
    public void reset() {
        x = gen.nextInt(600);
        if (power) {
            y = -3200;
        } else {
            y = -4800;
        }
    }
}