package WingMan;


import java.awt.*;
import java.net.URL;

public class Renderer extends Component {

    private int move, // movement of sea image tile
            TileWidth, TileHeight, NumberX, NumberY, // width, height, pos. of sea image tile
            wave, // current enemy wave in game
            p1, p2; // scores of p1 and p2
    private boolean gameOver = false, // is game over?
            reset = false, // did game reset?
            result; // true if boss defeated; else false
    private Image img, // image of sprite loaded
            sea; // sea image tile
    private URL url;
    private MediaTracker tracker;
    private Font text, // for "YOU WIN!" or "YOU LOSE!"
            scores; // for displaying scores

    Renderer() {
        move = wave = 0;
        text = new Font("SansSerif", Font.BOLD, 64);
        scores = new Font("SansSerif", Font.BOLD, 24);
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean result) {
        gameOver = true;
        this.result = result;

        p1 = WingMan.p1.getScore();
        p2 = WingMan.p2.getScore();
    }

    // for resetting game's parameters at game over for new game
    public void restart() {
        reset = true;
    }

    // reset parameters of all objects
    private void reset() {
        WingMan.p1.reset();
        WingMan.p2.reset();
        WingMan.BOSS.reset();
        WingMan.power.reset();
        WingMan.health.reset();
        WingMan.pBullet1.clear();
        WingMan.pBullet2.clear();
        WingMan.pBulletL1.clear();
        WingMan.pBulletL2.clear();
        WingMan.pBulletR1.clear();
        WingMan.pBulletR2.clear();
        WingMan.enBullet1.clear();
        WingMan.enBullet2.clear();
        WingMan.E1.clear();
        WingMan.E2.clear();
        WingMan.E3.clear();
        WingMan.E4.clear();

        wave = 0; // start at first wave
        gameOver = reset = false;
    }

    private void displayScores(Graphics2D g2) { // display scores at game over
        g2.setFont(scores);
        g2.setColor(Color.WHITE);
        g2.drawString("P1 Score: " + p1, 120, 210);
        g2.drawString("P2 Score: " + p2, 120, 240);
        g2.drawString("Total Score: " + (p1 + p2), 120, 270);
        g2.drawString("Press ENTER to play again.", 120, 330);
    }

    public Image getSprite(String name) {
        url = WingMan.class.getResource(name);
        img = getToolkit().getImage(url);

        try {
            tracker = new MediaTracker(this);
            tracker.addImage(img, 0);
            tracker.waitForID(0);
        } catch (Exception e) {
        }
        return img;
    }

    private void drawBG(int w, int h, Graphics2D g2) { // draw moving sea background
        sea = getSprite("Resources/water.png");
        TileWidth = sea.getWidth(this);
        TileHeight = sea.getHeight(this);

        NumberX = (int) (w / TileWidth); // number of tiles to be drawn to stretch to window width
        NumberY = (int) (h / TileHeight); // number of tiles to be drawn to stretch to window height

        // continuously draw sea tile images
        // images move downward to give sense of movement
        for (int i = -1; i <= NumberY; i++) {
            for (int j = 0; j <= NumberX; j++) {
                g2.drawImage(sea, j * TileWidth, i * TileHeight + (move % TileHeight), TileWidth, TileHeight, this);
            }
        }
        move += 1; // downward movement
        if (move == 32) { // prevent integer overflow for long operation
            move = 0;
        }
    }

    // draw all game elements
    public void drawFull(int w, int h, Graphics2D g2) {
        drawBG(w, h, g2); // moving sea

        WingMan.I1.update(w, h); // island 1
        WingMan.I1.draw(g2, this);

        WingMan.I2.update(w, h); // island 2
        WingMan.I2.draw(g2, this);

        WingMan.I3.update(w, h); // island 3
        WingMan.I3.draw(g2, this);

        if (gameOver) { // show results at game over
            if (result) {
                g2.setFont(text);
                g2.setColor(Color.WHITE);
                g2.drawString("YOU WIN!", 120, 120);
            } else {
                g2.setFont(text);
                g2.setColor(Color.RED);
                g2.drawString("YOU LOSE!", 120, 120);
            }

            if (reset) { // reset game if reset button presed
                reset();
            }

            displayScores(g2);
        } else { // show and update all elements in the game
            WingMan.power.update();
            WingMan.health.update();
            WingMan.power.draw(g2, this);
            WingMan.health.draw(g2, this);
            
            // create wave of enemies if wave cleared
            if (WingMan.E1.isEmpty() && WingMan.E2.isEmpty()
                    && WingMan.E3.isEmpty() && WingMan.E4.isEmpty()) {
                if (wave == 10) { // boss is at last wave
                    WingMan.BOSS.update();
                    WingMan.BOSS.draw(g2, this);
                } else { // new wave if wave cleared
                    WingMan.tl.createWave(wave);
                    wave++;
                }
            }

            // draw and update bullets from each linked list
            // remove first bullet from list if already destroyed or gone
            if (!WingMan.pBullet1.isEmpty()) {
                for (int i = 0; i < WingMan.pBullet1.size(); i++) {
                    WingMan.pBullet1.get(i).update();
                    WingMan.pBullet1.get(i).draw(g2, this);
                }

                if (WingMan.pBullet1.getFirst().isDestroyed()) {
                    WingMan.pBullet1.removeFirst();
                }
            }

            if (!WingMan.pBulletL1.isEmpty()) {
                for (int i = 0; i < WingMan.pBulletL1.size(); i++) {
                    WingMan.pBulletL1.get(i).update();
                    WingMan.pBulletL1.get(i).draw(g2, this);
                }

                if (WingMan.pBulletL1.getFirst().isDestroyed()) {
                    WingMan.pBulletL1.removeFirst();
                }
            }

            if (!WingMan.pBulletR1.isEmpty()) {
                for (int i = 0; i < WingMan.pBulletR1.size(); i++) {
                    WingMan.pBulletR1.get(i).update();
                    WingMan.pBulletR1.get(i).draw(g2, this);
                }

                if (WingMan.pBulletR1.getFirst().isDestroyed()) {
                    WingMan.pBulletR1.removeFirst();
                }
            }

            if (!WingMan.pBullet2.isEmpty()) {
                for (int i = 0; i < WingMan.pBullet2.size(); i++) {
                    WingMan.pBullet2.get(i).update();
                    WingMan.pBullet2.get(i).draw(g2, this);
                }

                if (WingMan.pBullet2.getFirst().isDestroyed()) {
                    WingMan.pBullet2.removeFirst();
                }
            }

            if (!WingMan.pBulletL2.isEmpty()) {
                for (int i = 0; i < WingMan.pBulletL2.size(); i++) {
                    WingMan.pBulletL2.get(i).update();
                    WingMan.pBulletL2.get(i).draw(g2, this);
                }

                if (WingMan.pBulletL2.getFirst().isDestroyed()) {
                    WingMan.pBulletL2.removeFirst();
                }
            }

            if (!WingMan.pBulletR2.isEmpty()) {
                for (int i = 0; i < WingMan.pBulletR2.size(); i++) {
                    WingMan.pBulletR2.get(i).update();
                    WingMan.pBulletR2.get(i).draw(g2, this);
                }

                if (WingMan.pBulletR2.getFirst().isDestroyed()) {
                    WingMan.pBulletR2.removeFirst();
                }
            }

            if (!WingMan.enBullet1.isEmpty()) {
                for (int i = 0; i < WingMan.enBullet1.size(); i++) {
                    WingMan.enBullet1.get(i).update();
                    WingMan.enBullet1.get(i).draw(g2, this);
                }

                if (WingMan.enBullet1.getFirst().isDestroyed()) {
                    WingMan.enBullet1.removeFirst();
                }
            }

            if (!WingMan.enBullet2.isEmpty()) {
                for (int i = 0; i < WingMan.enBullet2.size(); i++) {
                    WingMan.enBullet2.get(i).update();
                    WingMan.enBullet2.get(i).draw(g2, this);
                }

                if (WingMan.enBullet2.getFirst().isDestroyed()) {
                    WingMan.enBullet2.removeFirst();
                }
            }

            // draw and update enemies from each linked list
            // remove first enemy from list if already destroyed or gone
            if (!WingMan.E1.isEmpty()) {
                for (int i = 0; i < WingMan.E1.size(); i++) {
                    WingMan.E1.get(i).update();
                    WingMan.E1.get(i).draw(g2, this);
                }

                if (WingMan.E1.getFirst().isDead()) {
                    WingMan.E1.removeFirst();
                }
            }

            if (!WingMan.E2.isEmpty()) {
                for (int i = 0; i < WingMan.E2.size(); i++) {
                    WingMan.E2.get(i).update();
                    WingMan.E2.get(i).draw(g2, this);
                }

                if (WingMan.E2.getFirst().isDead()) {
                    WingMan.E2.removeFirst();
                }
            }

            if (!WingMan.E3.isEmpty()) {
                for (int i = 0; i < WingMan.E3.size(); i++) {
                    WingMan.E3.get(i).update();
                    WingMan.E3.get(i).draw(g2, this);
                }

                if (WingMan.E3.getFirst().isDead()) {
                    WingMan.E3.removeFirst();
                }
            }

            if (!WingMan.E4.isEmpty()) {
                for (int i = 0; i < WingMan.E4.size(); i++) {
                    WingMan.E4.get(i).update();
                    WingMan.E4.get(i).draw(g2, this);
                }

                if (WingMan.E4.getFirst().isDead()) {
                    WingMan.E4.removeFirst();
                }
            }
            
            // draw and update players as long as each is alive
            if (!WingMan.p1.isDead()) {
                WingMan.p1.update();
            }

            if (!WingMan.p2.isDead()) {
                WingMan.p2.update();
            }

            WingMan.p1.draw(g2, this);
            WingMan.p2.draw(g2, this);
        }
    }
}