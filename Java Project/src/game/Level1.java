package game;

import city.cs.engine.*;
import java.awt.*;
import java.io.IOException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;

/**
 * @author      Liana Chowdhury
 * @version     3.0
 * @since       March 2021
 */
public class Level1 extends GameLevel {
    private SoundClip gameMusic;

    //constructor
    public Level1(Game game) {
        super(game, 3);

        //play music
        try {
            gameMusic = new SoundClip("data/sounds/bg/1.wav");   // Open an audio input stream
            gameMusic.loop();  // Set it to continuous playback (looping)
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println(e);
        }
        gameMusic.setVolume(1.0d);

    }

    //getter for music
    public SoundClip getGameMusic() {
        return gameMusic;
    }

    //lets us know what level this is
    @Override
    public String getLevelName() {
        return "Level1";
    }

    /**
     * Checks if the player has killed all the enemies on the level.
     *
     * @param
     * @return boolean
     */
    //condition for the game ending is if they have killed all enemies
    @Override
    public boolean isComplete() {
        if (getPlayer().getEnemiesKilled() == 3)
            return true;
        else
            return false;
    }

    //paint a different background
    @Override
    public Image paintBackground() {
        Image background = new ImageIcon("data/bg/bg1.png").getImage();
        return background;
    }
}
