package WingMan;


import java.awt.*;
// import java.awt.image.*;

public class Timeline {

    private Image en1, en2, en3, en4;

    Timeline() {
        en1 = WingMan.r.getSprite("Resources/enemy1.png");
        en2 = WingMan.r.getSprite("Resources/enemy2.png");
        en3 = WingMan.r.getSprite("Resources/enemy3.png");
        en4 = WingMan.r.getSprite("Resources/enemy4.png");
    }

    public void createWave(int wave) {
        switch (wave) {
            case 0:
                for (int i = 0; i < 4; i++) {
                    WingMan.E4.add(new Enemy4(en4, 20, 600 + (100 * i), i));
                    WingMan.E4.add(new Enemy4(en4, 590, 600 + (100 * i), i + 4));
                    WingMan.E4.add(new Enemy4(en4, 50, 600 + (100 * i), i));
                    WingMan.E4.add(new Enemy4(en4, 560, 600 + (100 * i), i + 4));
                }
                break;
            case 1:
                for (int i = 0; i < 4; i++) {
                    WingMan.E1.add(new Enemy1(en1, 240 - (40 * i), -200 - (100 * i), 3 - i));
                    WingMan.E1.add(new Enemy1(en1, 370 + (40 * i), -200 - (100 * i), 3 - i));
                    WingMan.E1.add(new Enemy1(en1, 280 - (40 * i), -700 - (100 * i), 7 - i));
                    WingMan.E1.add(new Enemy1(en1, 330 + (40 * i), -700 - (100 * i), 7 - i));
                }
                WingMan.E1.add(new Enemy1(en1, 300, -1200, 4));
                break;
            case 2:
                for (int i = 0; i < 4; i++) {
                    WingMan.E1.add(new Enemy1(en1, 260 - (60 * i), -200 - (100 * i), 3 - i));
                    WingMan.E1.add(new Enemy1(en1, 350 + (60 * i), -200 - (100 * i), 3 - i));
                }
                WingMan.E2.add(new Enemy2(en2, 260, -800, 5));
                WingMan.E2.add(new Enemy2(en2, 350, -800, 5));
                break;
            case 3:
                for (int i = 0; i < 4; i++) {
                    WingMan.E2.add(new Enemy2(en2, -160 - (160 * i), 180 - (40 * i), 3 - i));
                    WingMan.E2.add(new Enemy2(en2, 800 + (160 * i), 180 - (40 * i), 3 - i));
                    WingMan.E1.add(new Enemy1(en1, 260 - (40 * i), -400 - (100 * i), 7 - i));
                    WingMan.E1.add(new Enemy1(en1, 350 + (40 * i), -400 - (100 * i), 7 - i));
                }
                break;
            case 4:
                for (int i = 0; i < 4; i++) {
                    WingMan.E4.add(new Enemy4(en4, 480, 600 + (200 * i), i + 4));
                    WingMan.E4.add(new Enemy4(en4, 520, 600 + (200 * i), i + 4));
                    WingMan.E4.add(new Enemy4(en4, 120, 700 + (200 * i), i));
                    WingMan.E4.add(new Enemy4(en4, 80, 700 + (200 * i), i));
                }
                break;
            case 5:
                for (int i = 0; i < 4; i++) {
                    WingMan.E2.add(new Enemy2(en2, -160 - (160 * i), 180 - (40 * i), 3 - i));
                    WingMan.E2.add(new Enemy2(en2, 800 + (160 * i), 180 - (40 * i), 3 - i));
                }
                for (int i = 0; i < 2; i++) {
                    WingMan.E2.add(new Enemy2(en2, 260 - (60 * i), -400 - (200 * i), 6 - i));
                    WingMan.E2.add(new Enemy2(en2, 350 + (60 * i), -400 - (200 * i), 6 - i));
                }
                break;
            case 6:
                for (int i = 0; i < 4; i++) {
                    WingMan.E2.add(new Enemy2(en2, 80 + (60 * i), -400 - (200 * i), 7 - i));
                    WingMan.E2.add(new Enemy2(en2, 530 - (60 * i), -400 - (200 * i), 7 - i));
                }
                break;
            case 7:
                WingMan.E2.add(new Enemy2(en2, 80, -200, 7));
                WingMan.E2.add(new Enemy2(en2, 530, -200, 7));
                for (int i = 0; i < 3; i++) {
                    WingMan.E1.add(new Enemy1(en1, 140 + (60 * i), -100 - (100 * i), 6 - i));
                    WingMan.E1.add(new Enemy1(en1, 470 - (60 * i), -100 - (100 * i), 6 - i));
                }
                WingMan.E3.add(new Enemy3(en3, 260, -200, 3));
                WingMan.E3.add(new Enemy3(en3, 350, -200, 3));
                break;
            case 8:
                WingMan.E2.add(new Enemy2(en2, 140, -200, 7));
                WingMan.E2.add(new Enemy2(en2, 470, -200, 7));
                for (int i = 0; i < 4; i++) {
                    WingMan.E1.add(new Enemy1(en1, 200, -200 - (100 * i), 7 - i));
                    WingMan.E1.add(new Enemy1(en1, 260, -200 - (100 * i), 7 - i));
                    WingMan.E1.add(new Enemy1(en1, 350, -200 - (100 * i), 7 - i));
                    WingMan.E1.add(new Enemy1(en1, 410, -200 - (100 * i), 7 - i));
                }
                WingMan.E3.add(new Enemy3(en3, 80, -600, 4));
                WingMan.E3.add(new Enemy3(en3, 530, -600, 4));
                break;
            case 9:
                for (int i = 0; i < 4; i++) {
                    WingMan.E3.add(new Enemy3(en3, 240 - (40 * i), -200 - (200 * i), 3 - i));
                    WingMan.E3.add(new Enemy3(en3, 370 + (40 * i), -200 - (200 * i), 3 - i));
                    WingMan.E3.add(new Enemy3(en3, 280 - (40 * i), -400 - (200 * i), 7 - i));
                    WingMan.E3.add(new Enemy3(en3, 330 + (40 * i), -400 - (200 * i), 7 - i));
                }
                break;
        }
    }
}