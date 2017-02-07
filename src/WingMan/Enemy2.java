package WingMan;


import java.awt.*;
import java.awt.image.*;
import java.util.ArrayList;

public class Enemy2 {

    private Image img, // enemy 2 image
            b1, b2; // images of small (b1) and big (b2) bullets
    private int x, y, w, h, // pos., width, height
            type, // determines movement patern
            expX, expY, // explosion pos.
            distance = 0, // travel dist. for movement pattern
            health = 4,
            postBoomTime = 0, // for explosion sequence
            fireTime = 0; // rate of fire
    private boolean boom = false, // exploded?
            dead = false, // enemy down?
            cycle = true; // went back and forth already?
    private ImageObserver obs;
    private ArrayList<Image> expimg = new ArrayList();

    Enemy2(Image img, int x, int y, int type) {
        this.img = img;
        this.x = x;
        this.y = y;
        this.type = type;
        w = img.getHeight(obs);
        h = img.getWidth(obs);

        b1 = WingMan.r.getSprite("Resources/enemyBulletS.png");
        b2 = WingMan.r.getSprite("Resources/enemyBulletB.png");

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
        if (!boom) {
            // collisions w/ players
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

            // movement patterns
            if (type < 4) { // move right, then alternatively down and up
                if (x < 300) { // move right
                    if (type == 0 && x < 60) {
                        x += 2;
                    } else if (type == 1 && x < 120) {
                        x += 2;
                    } else if (type == 2 && x < 180) {
                        x += 2;
                    } else if (type == 3 && x < 240) {
                        x += 2;
                    } else { // down and up
                        if (cycle) { // down
                            y += 2;
                            distance += 2;
                            if (distance >= 80) {
                                cycle = false;
                            }
                        } else { // up
                            y -= 2;
                            distance -= 2;
                            if (distance <= 0) {
                                cycle = true;
                            }
                        }
                    }
                } else { // move left, then alternatively down and up
                    if (type == 0 && x > 540) { // move left
                        x -= 2;
                    } else if (type == 1 && x > 480) {
                        x -= 2;
                    } else if (type == 2 && x > 420) {
                        x -= 2;
                    } else if (type == 3 && x > 360) {
                        x -= 2;
                    } else { // down and up
                        if (cycle) { // down
                            y += 2;
                            distance += 2;
                            if (distance >= 80) {
                                cycle = false;
                            }
                        } else { // up
                            y -= 2;
                            distance -= 2;
                            if (distance <= 0) {
                                cycle = true;
                            }
                        }
                    }
                }
            } else { // move down, then alternatively left and right
                if (type == 4 && y < 20) { // down
                    y += 2;
                } else if (type == 5 && y < 60) {
                    y += 2;
                } else if (type == 6 && y < 100) {
                    y += 2;
                } else if (type == 7 && y < 140) {
                    y += 2;
                } else { // left and right (or right and left)
                    if (x < 320) { // on left side, so left-right
                        if (cycle) { // left
                            x -= 2;
                            distance -= 2;
                            if (distance <= -80) {
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
                            if (distance >= 80) {
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
            }

            // fire rate patterns; type of bullet fired
            if (type < 4 && (x == 60 || x == 120 || x == 180 || x == 240
                    || x == 360 || x == 420 || x == 480 || x == 540)) {
                if (fireTime == 0) {
                    WingMan.enBullet1.add(new EnemyBullet1(b1, x, y + 10));
                    fireTime++;
                } else if (fireTime == 59) {
                    fireTime = 0;
                } else {
                    fireTime++;
                }
            } else if (type >= 4 && (y == 20 || y == 60 || y == 100 || y == 140)) {
                if (fireTime == 0) {
                    WingMan.enBullet2.add(new EnemyBullet2(b2, x - 9, y));
                    fireTime++;
                } else if (fireTime == 119) {
                    fireTime = 0;
                } else {
                    fireTime++;
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