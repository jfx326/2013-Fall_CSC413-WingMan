package WingMan;


import java.awt.*;
import java.awt.image.*;
import java.util.*;

public class Boss implements Observer {

    private Image img, bullet; // images of boss and bullet it fires
    private int x, y, w, h, // position, height, width of boss
            expX, expY, // position of boss prior to explosion
            health = 64, 
            postBoomTime = 0, // for explosion animation
            fireTime = 0; // rate of boss's fire
    private boolean boom = false, // exploded?
            dead = false, // destroyed?
            cycle = true; // went back and forth already?
    private ImageObserver obs;
    private GameEvents ge;
    private String msg;
    private GameSounds gs = new GameSounds();
    private ArrayList<Image> expimg = new ArrayList(); // explosion images

    Boss(Image img, int x, int y) {
        this.img = img;
        this.x = x;
        this.y = y;
        w = img.getHeight(obs);
        h = img.getWidth(obs);

        bullet = WingMan.r.getSprite("Resources/enemyBulletB.png");
        
        for (int i = 1; i <= 6; i++) { // add explosion image sequence to list
            expimg.add(WingMan.r.getSprite("Resources/explosion2_" + i + ".png"));
        }
    }

    public void draw(Graphics g, ImageObserver obs) {
        this.obs = obs;
        if (!boom) {
            g.drawImage(img, x, y, obs); // draw boss if not exploded
        } else if (postBoomTime == 29) { // boss is completely destroyed
            dead = true;
            WingMan.r.setGameOver(true); // a winner is you!
        } else { // explosion animation
            postBoomTime++;
            g.drawImage(expimg.get(postBoomTime / 5), expX, expY, obs);
            g.drawImage(expimg.get(postBoomTime / 5), expX + 65, expY, obs);
            g.drawImage(expimg.get(postBoomTime / 5), expX, expY + 65, obs);
            g.drawImage(expimg.get(postBoomTime / 5), expX + 65, expY + 65, obs);
        }
    }

    public void update() {
        if (!boom) {
            if (y < 40) { // slowly goes down to screen
                y += 1;
            } else { // moves left and right
                if (cycle) { // move left
                    x += 3;
                    if (x >= 550) {
                        cycle = false;
                    }
                } else { // move right
                    x -= 3;
                    if (x <= 0) {
                        cycle = true;
                    }
                }

                // fires two bullets at a time three times in first 0.2 seconds of cycle
                if (fireTime >= 0 && fireTime <= 20) {
                    if (fireTime % 10 == 0) {
                        WingMan.enBullet2.add(new EnemyBullet2(bullet, x + 10, y + 25));
                        WingMan.enBullet2.add(new EnemyBullet2(bullet, x + 35, y + 25));
                    }
                    fireTime++;
                } else if (fireTime == 59) { // cycle again; fire again
                    fireTime = 0;
                } else { // doesn't fire
                    fireTime++;
                }
            }

            // check for collisions w/ players; damage players and boss accordingly
            // player 1 collision
            if (WingMan.p1.collide(x, y, w, h) && !WingMan.p1.isDestroyed() && !WingMan.p1.isDead()) {
                WingMan.gameEvents.setValue("P1HitBoss");
                damage();
                if (health <= 0) { // in case player 1 destroys boss by crashing
                    WingMan.p1.setScore(2000); // 2000 points for p1
                }
            } // player 2 collision
            else if (WingMan.p2.collide(x, y, w, h) && !WingMan.p2.isDestroyed() && !WingMan.p2.isDead()) {
                WingMan.gameEvents.setValue("P2HitBoss");
                damage();
                if (health <= 0) { // in case player 2 destroys boss by crashing
                    WingMan.p2.setScore(2000); // 2000 points for p2
                }
            }
        }
    }

    public void update(Observable obj, Object arg) {
        if (!boom) {
            ge = (GameEvents) arg;
            msg = (String) ge.event;
            switch (msg) {
                case "P1ShotBoss":
                    gs.playSound("Resources/snd_explosion1.wav");
                    damage();
                    if (health <= 0) { // if killing shot
                        WingMan.p1.setScore(2000);
                    }
                    break;
                case "P2ShotBoss":
                    gs.playSound("Resources/snd_explosion1.wav");
                    damage();
                    if (health <= 0) { // if killing shot
                        WingMan.p2.setScore(2000);
                    }
                    break;
            }
        }
    }

    public boolean collide(int x, int y, int w, int h) {
        if ((y + h > this.y) && (y < this.y + this.h)) {
            if ((x + w > this.x + 20) && (x < this.x + this.w - 20)) {
                return true;
            }
        }
        return false;
    }

    private void damage() {
        health--;
        if (health <= 0) {
            expX = x; // x position of explosion
            expY = y; // y position of explosion
            x = y = 640; // set boss offscreen
            boom = true; // boom!
            gs.playSound("Resources/snd_explosion2.wav");
        }
    }

    public boolean isDestroyed() {
        return boom;
    }

    public boolean isDead() {
        return dead;
    }
    
    public void reset() {
        x = 260;
        y = -160;
        health = 64;
        postBoomTime = fireTime = 0;
        boom = dead = false;
        cycle = true;
    }
}