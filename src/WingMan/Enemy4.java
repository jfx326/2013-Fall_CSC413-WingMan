package WingMan;


import java.awt.*;
import java.awt.image.*;
import java.util.ArrayList;

public class Enemy4 {

    private Image img;
    private int x, y, w, h, type, expX, expY, distance = 0, health = 4, postBoomTime = 0;
    private boolean boom = false, dead = false, cycle = true;
    private ImageObserver obs;
    private ArrayList<Image> expimg = new ArrayList();

    Enemy4(Image img, int x, int y, int type) {
        this.img = img;
        this.x = x;
        this.y = y;
        this.type = type;
        w = img.getHeight(obs);
        h = img.getWidth(obs);

        for (int i = 1; i <= 6; i++) { // add explosion image sequence to list
            expimg.add(WingMan.r.getSprite("Resources/explosion2_" + i + ".png"));
        }
    }

    public void draw(Graphics g, ImageObserver obs) {
        this.obs = obs;
        if (!boom) {
            g.drawImage(img, x, y, obs);
        } else if (postBoomTime == 29) {
            dead = true;
        } else {
            postBoomTime++;
            g.drawImage(expimg.get(postBoomTime / 5), expX, expY, obs);
        }
    }

    public void update() {
        // collisions w/ players
        if (!boom) {
            if (WingMan.p1.collide(x, y, w, h) && !WingMan.p1.isDestroyed() && !WingMan.p1.isDead()) {
                WingMan.gameEvents.setValue("P1HitEnemy");
                destroy();
                if (health <= 0) {
                    WingMan.p1.setScore(250); // p1 gets 250 points
                }
            } else if (WingMan.p2.collide(x, y, w, h) && !WingMan.p2.isDestroyed() && !WingMan.p2.isDead()) {
                WingMan.gameEvents.setValue("P2HitEnemy");
                destroy();
                if (health <= 0) {
                    WingMan.p2.setScore(250); // p2 gets 250 points
                }
            }

            // move up, move to one side, then side to side
            if ((type == 0 || type == 4) && y > 20) { // move up
                y -= 1;
            } else if ((type == 1 || type == 5) && y > 60) {
                y -= 1;
            } else if ((type == 2 || type == 6) && y > 100) {
                y -= 1;
            } else if ((type == 3 || type == 7) && y > 140) {
                y -= 1;
            } else {
                if (x < 300) { // on left side, so left-right
                    if (cycle) { // left
                        x -= 2;
                        distance -= 2;
                        if (distance <= -240) {
                            cycle = false;
                        }
                    } else { // right
                        x += 2;
                        distance += 2;
                        if (distance >= 0) {
                            cycle = true;
                        }
                    }
                } else { // on right side, so right-left
                    if (cycle) { // right
                        x += 2;
                        distance += 2;
                        if (distance >= 240) {
                            cycle = false;
                        }
                    } else { // left
                        x -= 2;
                        distance -= 2;
                        if (distance <= 0) {
                            cycle = true;
                        }
                    }
                }
            }
            
            if (type <= 3) { // types 0-3 move right before side to side
                if (y > 180 && y < 300) { // move right
                    x += 2;
                }
            } else { // types 4-7 move left before side to side
                if (y > 180 && y < 300) { // move left
                    x -= 2;
                }
            }
        }
    }

    public boolean collide(int x, int y, int w, int h) {
        if ((y + h > this.y) && (y < this.y + this.h)) {
            if ((x + w > this.x) && (x < this.x + this.w)) {
                return true;
            }
        }
        return false;
    }

    public void damage() {
        health--;
        if (health <= 0) {
            expX = x;
            expY = y;
            boom = true;
            x = y = 640;
            WingMan.gs.playSound("Resources/snd_explosion2.wav");
        } else {
            WingMan.gs.playSound("Resources/snd_explosion1.wav");
        }
    }

    private void destroy() {
        health = 0;
        expX = x;
        expY = y;
        boom = true;
        x = y = 640;
        WingMan.gs.playSound("Resources/snd_explosion2.wav");
    }

    public boolean isDestroyed() {
        return boom;
    }

    public boolean isDead() {
        return dead;
    }
}