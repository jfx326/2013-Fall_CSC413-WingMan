package WingMan;

import java.awt.*;
import java.awt.image.*;
import java.util.*;

public class Player2 implements Observer {

    private Image img, // plane image
            pb, pbL, pbR, // player bullet, bullet left, bullet right
            pow, life; // power-up, life indicator
    private int x, y, w, h, // pos., height, width
            expX, expY, // pos. of explosion
            spawnTime = 0, // for flickering respawn animation
            fireTime = 0, // rate of fire
            health = 16, score = 0, lives = 3;
    private boolean boom = false, // exploded?
            powerUp = false,
            ready = true; // is already spawned and ready to move?
    static boolean up = false, down = false, left = false, right = false, fire = false;
    private ImageObserver obs;
    private GameEvents ge;
    private String msg;
    private ArrayList<Image> expimg = new ArrayList(); // explosion images
    private ArrayList<Image> hpimg = new ArrayList(); // health images
    private Font display, // displays score on top of health
            label; // labels player 1 on appropriate plane

    Player2(Image img, int x, int y) {
        this.img = img;
        this.x = x;
        this.y = y;
        w = img.getWidth(obs);
        h = img.getHeight(obs);

        pb = WingMan.r.getSprite("Resources/bullet.png");
        pbL = WingMan.r.getSprite("Resources/bulletLeft.png");
        pbR = WingMan.r.getSprite("Resources/bulletRight.png");

        pow = WingMan.r.getSprite("Resources/powerup.png");
        life = WingMan.r.getSprite("Resources/life.png");

        for (int i = 1; i <= 6; i++) { // add explosion image sequence to list
            expimg.add(WingMan.r.getSprite("Resources/explosion2_" + i + ".png"));
        }

        // adding health indicating images
        hpimg.add(WingMan.r.getSprite("Resources/health00.png"));

        for (int i = 1; i <= 9; i++) {
            hpimg.add(WingMan.r.getSprite("Resources/health0" + i + "-R.png"));
        }

        for (int i = 10; i <= 15; i++) {
            hpimg.add(WingMan.r.getSprite("Resources/health" + i + "-R.png"));
        }

        hpimg.add(WingMan.r.getSprite("Resources/health16.png"));

        display = new Font("SansSerif", Font.BOLD, 24);
        label = new Font("SansSerif", Font.BOLD, 16);
    }

    public void draw(Graphics g, ImageObserver obs) {
        this.obs = obs;

        // display health of player
        g.drawImage(hpimg.get(health), 320, 410, obs);

        // display score on top of health bar
        g.setFont(display);
        g.setColor(Color.BLACK);
        g.drawString("P2: " + score, 325, 435);

        // display label of player once spawned
        if (ready) {
            g.setFont(label);
            g.setColor(Color.WHITE);
            g.drawString("Player 2", x + 2, y + 70);
        }

        // display lives according to life count
        if (lives > 1) {
            g.drawImage(life, 450, 410, obs);
            if (lives > 2) {
                g.drawImage(life, 480, 410, obs);
            }
        }

        if (powerUp) { // power-up indicator
            g.drawImage(pow, 520, 410, obs);
        }

        if (!boom && lives > 0) { // draw plane if not exploded and has lives
            g.drawImage(img, x, y, obs);
        } else if (lives == 0) { // p2 is dead
            x = y = 640; // move offscreen to avoid "invisible plane" collisions
            health = 0;
            if (WingMan.p1.isDead()) { // p1 is dead = game over
                WingMan.r.setGameOver(false);
            }
        } else if (spawnTime == 29) { // start of spawn sequence
            lives--;
            if (lives > 0) {
                health = 16;
            }
            x = 450; // respawn at starting position
            y = 330;
            ready = true;
            spawnTime++;
        } else if (spawnTime > 29 && spawnTime < 329) { // respawn animation
            if (spawnTime % 30 <= 15) {
                g.drawImage(img, x, y, obs);
            }
            spawnTime++;
        } else if (spawnTime == 329) { // end of spawn sequence
            boom = false;
            spawnTime = 0;
        } else { // explosion sequence
            g.drawImage(expimg.get(spawnTime / 5), expX, expY, obs);
            spawnTime++;
        }
    }

    public void update() {
        if (ready) {
            if (left) {
                if (x >= 0) {
                    x -= 5;
                }
            }
            if (right) {
                if (x <= 570) {
                    x += 5;
                }
            }
            if (up) {
                if (y >= 0) {
                    y -= 5;
                }
            }
            if (down) {
                if (y <= 330) {
                    y += 5;
                }
            }
            if (fire) {
                if (fireTime == 0) { // fires a bullet
                    WingMan.pBullet2.add(new PlayerBullet(pb, x + 17, y, 2));
                    if (powerUp) { // fires another two bullets
                        WingMan.pBulletL2.add(new PlayerBulletL(pbL, x + 10, y, 2));
                        WingMan.pBulletR2.add(new PlayerBulletR(pbR, x + 10, y, 2));
                    }
                    fireTime++;
                } else if (fireTime == 14) { // able to fire again
                    fireTime = 0;
                } else { // delay firing if fire button held
                    fireTime++;
                }
            } else { // if fire button released, be able to fire again
                fireTime = 0;
            }
        }
    }

    public void update(Observable obj, Object arg) {
        ge = (GameEvents) arg;
        msg = (String) ge.event;
        
        if (!boom) {
            switch (msg) {
                case "P2HitBoss": // instant death if crash into boss
                    health = 0;
                    break;
                case "P2HitEnemy": // major damage if crash into enemy
                    health -= 4;
                    WingMan.gs.playSound("Resources/snd_explosion2.wav");
                    break;
                case "P2HitBulletS": // hit small enemy bullet
                    health--;
                    WingMan.gs.playSound("Resources/snd_explosion1.wav");
                    break;
                case "P2HitBulletB": // hit big enemy bullet
                    health -= 2;
                    WingMan.gs.playSound("Resources/snd_explosion1.wav");
                    break;

            }
            
            if (ready) {
                switch (msg) {
                    case "P2GotPowerUp": // has power-up
                        powerUp = true;
                        break;
                    case "P2GotHealth": // restore health
                        health = 16;
                        break;
                }
            }

            if (health <= 0) { // if destroyed...
                health = 0; // set health to 0; otherwise error in displaying health image
                expX = x; // set pos. of explosion
                expY = y;
                x = y = 640; // set plane offscreen; otherwise, invisible plane collision
                boom = true;
                powerUp = ready = false;
                WingMan.gs.playSound("Resources/snd_explosion2.wav");
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

    public boolean isDestroyed() {
        return boom;
    }

    public boolean isDead() {
        if (lives == 0) {
            return true;
        }
        return false;
    }

    public void setScore(int value) {
        score += value;
    }

    public int getScore() {
        return score;
    }

    public void reset() {
        x = 450;
        y = 320;
        spawnTime = fireTime = score = 0;
        health = 16;
        lives = 3;
        boom = powerUp = false;
    }
}