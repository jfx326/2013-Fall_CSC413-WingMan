package WingMan;


import java.awt.*;
import java.awt.image.*;
import java.util.ArrayList;

public class PlayerBulletL {

    private Image img;
    private int x, y, w, h, player, expX, expY, postBoomTime = 0;
    private boolean boom = false, gone = false;
    private ImageObserver obs;
    private ArrayList<Image> expimg = new ArrayList();

    PlayerBulletL(Image img, int x, int y, int player) {
        this.img = img;
        this.x = x;
        this.y = y;
        this.player = player;
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
            g.drawImage(expimg.get(postBoomTime / 5), expX, expY, obs);
        }
    }

    public void update() {
        if (!boom && !gone) {
            if (y <= -80 || x <= -80) { //offscreen - remove bullet
                x = y = -320;
                gone = true;
            } else if (WingMan.BOSS.collide(x, y, w, h) && !WingMan.BOSS.isDestroyed()) {
                if (player == 1) {
                    WingMan.gameEvents.setValue("P1ShotBoss");
                    destroy();
                } else {
                    WingMan.gameEvents.setValue("P2ShotBoss");
                    destroy();
                }
            } else {
                x -= 5; // top-leftward direction
                y -= 5;
            }
            
            // check collisions w/ enemy planes; destroy in collision
            if (!WingMan.E1.isEmpty()) {
                for (int i = 0; i < WingMan.E1.size(); i++) {
                    if (WingMan.E1.get(i).collide(x, y, w, h) && !WingMan.E1.get(i).isDestroyed()) {
                        WingMan.E1.get(i).damage();
                        if (player == 1) {
                            WingMan.p1.setScore(100);
                        } else {
                            WingMan.p2.setScore(100);
                        }
                        destroy();
                        break;
                    }
                }
            }
            
            if (!WingMan.E2.isEmpty()) {
                for (int i = 0; i < WingMan.E2.size(); i++) {
                    if (WingMan.E2.get(i).collide(x, y, w, h) && !WingMan.E2.get(i).isDestroyed()) {
                        WingMan.E2.get(i).damage();
                        if (WingMan.E2.get(i).isDestroyed()) {
                            if (player == 1) {
                                WingMan.p1.setScore(250);
                            } else {
                                WingMan.p2.setScore(250);
                            }
                        }
                        destroy();
                        break;
                    }
                }
            }
            
            if (!WingMan.E3.isEmpty()) {
                for (int i = 0; i < WingMan.E3.size(); i++) {
                    if (WingMan.E3.get(i).collide(x, y, w, h) && !WingMan.E3.get(i).isDestroyed()) {
                        WingMan.E3.get(i).damage();
                        if (WingMan.E3.get(i).isDestroyed()) {
                            if (player == 1) {
                                WingMan.p1.setScore(500);
                            } else {
                                WingMan.p2.setScore(500);
                            }
                        }
                        destroy();
                        break;
                    }
                }
            }
            
            if (!WingMan.E4.isEmpty()) {
                for (int i = 0; i < WingMan.E4.size(); i++) {
                    if (WingMan.E4.get(i).collide(x, y, w, h) && !WingMan.E4.get(i).isDestroyed()) {
                        WingMan.E4.get(i).damage();
                        if (WingMan.E4.get(i).isDestroyed()) {
                            if (player == 1) {
                                WingMan.p1.setScore(250);
                            } else {
                                WingMan.p2.setScore(250);
                            }
                        }
                        destroy();
                        break;
                    }
                }
            }
        }
    }

    public void destroy() {
        expX = x;
        expY = y;
        x = y = -320;
        boom = true;
    }

    public boolean isDestroyed() {
        return gone;
    }
}