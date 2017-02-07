package WingMan;


import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;
import javax.swing.*;

public class WingMan extends JApplet implements Runnable {

    private Thread thread, me;
    private BufferedImage bimg; // image drawn onscreen
    private Random generator = new Random(); // for random power-up, island generation
    static Island I1, I2, I3; // islands
    static Player1 p1;
    static Player2 p2;
    static Boss BOSS;
    static PowerUp power, health; // triple bullets and restored health
    static LinkedList<PlayerBullet> pBullet1 = new LinkedList(); // p1 center bullet
    static LinkedList<PlayerBulletL> pBulletL1 = new LinkedList(); // p1 left ...
    static LinkedList<PlayerBulletR> pBulletR1 = new LinkedList(); // p1 right ...
    static LinkedList<PlayerBullet> pBullet2 = new LinkedList(); // p2 center bullet
    static LinkedList<PlayerBulletL> pBulletL2 = new LinkedList(); // p2 left ...
    static LinkedList<PlayerBulletR> pBulletR2 = new LinkedList(); // p2 right ...
    static LinkedList<EnemyBullet1> enBullet1 = new LinkedList(); // weak enemy bullet
    static LinkedList<EnemyBullet2> enBullet2 = new LinkedList(); // strong ...
    static LinkedList<Enemy1> E1 = new LinkedList(); // enemy 1 list
    static LinkedList<Enemy2> E2 = new LinkedList(); // enemy 2 list
    static LinkedList<Enemy3> E3 = new LinkedList(); // enemy 3 list
    static LinkedList<Enemy4> E4 = new LinkedList(); // enemy 4 list
    static GameEvents gameEvents;
    private Image player, // player plane image
            is1, is2, is3, // images of islands
            boss, pwr, hp; // boss image, power-up image, health restore image
    private KeyControl key; // controller
    private Graphics2D g2; // for drawing bimg
    private Dimension d; // for drawing bimg (same size as window)
    static Renderer r = new Renderer(); // drawing elements onto bimg
    static Timeline tl = new Timeline(); // creates wave of enemies
    static GameSounds gs = new GameSounds(); // play sounds

    public void init() {
        setBackground(Color.white);

        player = r.getSprite("Resources/myplane.png");
        is1 = r.getSprite("Resources/island1.png");
        is2 = r.getSprite("Resources/island2.png");
        is3 = r.getSprite("Resources/island3.png");
        boss = r.getSprite("Resources/boss.png");
        
        pwr = WingMan.r.getSprite("Resources/powerup.png");
        hp = WingMan.r.getSprite("Resources/health.png");

        gs.playSound("Resources/music.wav"); // bgm

        // display islands in random locations
        I1 = new Island(is1, 100, 100, generator);
        I2 = new Island(is2, 200, 400, generator);
        I3 = new Island(is3, 300, 200, generator);
        
        p1 = new Player1(player, 120, 320);
        p2 = new Player2(player, 450, 320);
        BOSS = new Boss(boss, 260, -240);
        
        // display power-ups in random locations but rarely
        // power-ups spawn far offscreen at the top
        power = new PowerUp(pwr, generator.nextInt(600), -3200, true, generator);
        health = new PowerUp(hp, generator.nextInt(600), -4800, false, generator);

        gameEvents = new GameEvents();
        gameEvents.addObserver(p1);
        gameEvents.addObserver(p2);
        gameEvents.addObserver(BOSS);

        key = new KeyControl();
        addKeyListener(key);

        setFocusable(true);
    }

    public void start() {
        thread = new Thread(this);
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();
    }

    public void run() {
        me = Thread.currentThread();
        while (thread == me) {
            repaint();

            try {
                thread.sleep(17); // approximately 60 FPS (1/60 s = 17 ms)
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    public void paint(Graphics g) {
        d = getSize();
        g2 = createGraphics2D(d.width, d.height); // create new bimg
        r.drawFull(d.width, d.height, g2); // draw objects onto bimg
        g2.dispose();
        g.drawImage(bimg, 0, 0, this); // draw bimg
    }

    private Graphics2D createGraphics2D(int w, int h) {
        if (bimg == null || bimg.getWidth() != w || bimg.getHeight() != h) {
            bimg = (BufferedImage) createImage(w, h);
        }
        g2 = bimg.createGraphics();
        g2.setBackground(getBackground());
        g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g2.clearRect(0, 0, w, h);
        return g2;
    }

    public static void main(String argv[]) {
        final WingMan wm = new WingMan();
        wm.init();
        JFrame f = new JFrame("WingMan");
        f.addWindowListener(new WindowAdapter() {
        });
        f.getContentPane().add("Center", wm);
        f.pack();
        f.setSize(new Dimension(640, 480));
        f.setVisible(true);
        f.setResizable(false);
        f.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        wm.start();
    }
}