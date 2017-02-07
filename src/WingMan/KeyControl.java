package WingMan;


import java.awt.event.*;

public class KeyControl extends KeyAdapter {

    private int key;

    public void keyPressed(KeyEvent e) {
        key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            WingMan.p1.left = true;
        }
        if (key == KeyEvent.VK_RIGHT) {
            WingMan.p1.right = true;
        }
        if (key == KeyEvent.VK_UP) {
            WingMan.p1.up = true;
        }
        if (key == KeyEvent.VK_DOWN) {
            WingMan.p1.down = true;
        }
        if (key == KeyEvent.VK_CONTROL) {
            WingMan.p1.fire = true;
        }
        if (key == KeyEvent.VK_A) {
            WingMan.p2.left = true;
        }
        if (key == KeyEvent.VK_D) {
            WingMan.p2.right = true;
        }
        if (key == KeyEvent.VK_W) {
            WingMan.p2.up = true;
        }
        if (key == KeyEvent.VK_S) {
            WingMan.p2.down = true;
        }
        if (key == KeyEvent.VK_SHIFT) {
            WingMan.p2.fire = true;
        }
        
        if (key == KeyEvent.VK_ENTER && WingMan.r.isGameOver()) {
            WingMan.r.restart();
        }
    }

    public void keyReleased(KeyEvent e) {
        key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            WingMan.p1.left = false;
        }
        if (key == KeyEvent.VK_RIGHT) {
            WingMan.p1.right = false;
        }
        if (key == KeyEvent.VK_UP) {
            WingMan.p1.up = false;
        }
        if (key == KeyEvent.VK_DOWN) {
            WingMan.p1.down = false;
        }
        if (key == KeyEvent.VK_CONTROL) {
            WingMan.p1.fire = false;
        }
        if (key == KeyEvent.VK_A) {
            WingMan.p2.left = false;
        }
        if (key == KeyEvent.VK_D) {
            WingMan.p2.right = false;
        }
        if (key == KeyEvent.VK_W) {
            WingMan.p2.up = false;
        }
        if (key == KeyEvent.VK_S) {
            WingMan.p2.down = false;
        }
        if (key == KeyEvent.VK_SHIFT) {
            WingMan.p2.fire = false;
        }
    }
}