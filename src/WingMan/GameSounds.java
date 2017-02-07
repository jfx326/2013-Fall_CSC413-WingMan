package WingMan;


import javax.sound.sampled.*;
import java.net.URL;

public class GameSounds {
    private URL url;
    private AudioInputStream ais;
    private Clip c;
    
    GameSounds() {}
    
    public void playSound(String name) {
        url = WingMan.class.getResource(name);

        try {
            ais = AudioSystem.getAudioInputStream(url);
            c = AudioSystem.getClip();
            c.open(ais);

            if (name.equals("Resources/music.wav")) {
                c.loop(javax.sound.midi.Sequencer.LOOP_CONTINUOUSLY);
            }

            c.start();
        } catch (Exception e) {
        }
    }
}