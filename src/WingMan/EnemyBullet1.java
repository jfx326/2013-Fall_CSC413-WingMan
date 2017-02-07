package WingMan;


import java.awt.*;
import java.awt.image.*;
import java.util.ArrayList;

public class EnemyBullet1 {

    private Image img;
    private int x, y, w, h, expY, postBoomTime = 0;
    private boolean boom = false, gone = false;
    private ImageObserver obs;
    private ArrayList<Image> expimg = new ArrayList();

    EnemyBullet1(Image img, int x, int y) {
        this.img = img;
        this.x = x;
        this.y = y;
        w = img.getHeight(obs);
        h = img.getWidth(obs);

        for (int i = 1; i <= 6; i++) { // add explosion image sequence to list
            expimg.add(WingMan.r.getSprite("Resources/explosion1_" + i + ".png"));
        }
    }

    public void draw(Graphics g, ImageObserver obs) {
        this.obs = obs;
        if (!boom) {
            g.drawImage(img, x, y, obs);
        } else if (postBoomTime == 29) {
            gone = true;
        } else if (boom) {
            postBoomTime++;
            g.drawImage(expimg.get(postBoomTime / 5), x, expY, obs);
        }
    }

    public void update() {
        if (!boom && !gone) {
            if (y >= 560) { // offscreen - remove bullet
                y = -320;
                gone = true;
            } else if (WingMan.p1.collide(x, y, w, h) && !WingMan.p1.isDestroyed() && !WingMan.p1.isDead()) {
                WingMan.gameEvents.setValue("P1HitBulletS"); // hits p1
                destroy(); // bullet destroyed
            } else if (WingMan.p2.collide(x, y, w, h) && !WingMan.p2.isDestroyed() && !WingMan.p2.isDead()) {
                WingMan.gameEvents.setValue("P2HitBulletS"); // hits p2
                destroy(); // bullet destroyed
            } else {
                y += 5; // straight, downward direction
            }
        }
    }

    public void destroy() {
        expY = y;
        y = -320;
        boom = true;
    }

    public boolean isDestroyed() {
        return gone;
    }
}