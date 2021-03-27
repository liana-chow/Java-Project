package game;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * @author      Liana Chowdhury
 * @version     3.0
 * @since       March 2021
 */
public class Bomb implements ActionListener {

    //variables
    private final GameLevel w;
    private final DynamicBody bomb;
    private DynamicBody explosion;
    private static final BodyImage bombImage = new BodyImage("data/effects/bomb.png", 1f);
    private static final BodyImage explosionImage = new BodyImage("data/effects/explosion.gif", 10f);
    /**
     * Player position in order to know where it is fired from.
     */
    private static Vec2 position;
    /**
     * If the player is flipped, this will account for the polygon overlap.
     */
    private static final Vec2 flip = new Vec2(0.5f,0.5f);
    private int timerCount = 0;

    //sound of the explosion
    private static SoundClip explode;
    static {
        try {
            explode = new SoundClip("data/sounds/bomb.wav");
        } catch (UnsupportedAudioFileException | IOException |
                LineUnavailableException e) {
            System.out.println(e);
        }

    }

    /**
     * Creates a bomb object that is 'thrown' by the player
     *
     * @param w GameLevel.
     * @return Description of what the method returns.
     */
    //constructor
    public Bomb(GameLevel w){
        //create the bomb
        this.w = w;
        bomb = new DynamicBody(w, new CircleShape(0.5f));
        bomb.addImage(bombImage);

        //create it slightly to the left if the player faces left due to polygon overlapping
        if (w.getPlayer().getFlip()) {
            position = (new Vec2(w.getPlayer().getPosition())).sub(flip);
        } else {
            position = new Vec2(w.getPlayer().getPosition());
        }
        bomb.setPosition(position);

        //change impulse direction depending on which way the character faces
        Vec2 impulse;
        if (w.getPlayer().getFlip()) {
            impulse = new Vec2(-5,0f);
        } else {
            impulse = new Vec2(5,0f);
        }

        //apply the impulse to throw the bomb
        bomb.applyImpulse(impulse);

        //create and start the timer on when the explosion should occur
        Timer timer = new Timer(2500, this);
        timer.start();
        timer.setRepeats(false);
    }

    /**
     * When a bomb is created, it will run on a timer
     *
     * When a bomb is created, it will first start off as a created bomb that will then explode.
     * The explosion is a body so it will collide with other bodies which will inflict damage to them.
     * When the explosion has run its time, it will automatically be destroyed.
     *
     * @param e ActionEvent
     * @return
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        //when the bomb has been created, start the timer
        timerCount++;
        if (timerCount == 1) {
            //add 1 so it knows it has created a bomb
            position = bomb.getPosition();
            bomb.destroy();
            //create an explosion where the bomb once was
            explode.play();
            explosion = new DynamicBody(w, new CircleShape(5f));
            explosion.addImage(explosionImage);
            explosion.setPosition(position);

            //create a new timer in order to remove the explosion after some time
            Timer timer = new Timer(500, this);
            timer.setRepeats(false);
            timer.start();

            //add a collision listener to damage anyone near by
            explosion.addCollisionListener(e1 -> {
                //if an enemy collides with an explosion they get damaged
                if (e1.getOtherBody() instanceof Enemy) {
                    Enemy enemy = (Enemy) e1.getOtherBody();
                    int damage = enemy.getHealth() - 90;
                    enemy.setHealth(damage);
                    if (enemy.getHealth()<0){
                        enemy.death();
                        enemy.getP().setEnemiesKilled(enemy.getP().getEnemiesKilled()+1);
                        enemy.getP().getWorld().getGame().getGUI().updateEnemiesKilled();
                    }
                }
                //if the player collides with an explosion they get damaged
                if (e1.getOtherBody() instanceof Player){
                    Player player = (Player) e1.getOtherBody();
                    player.setHealth(player.getHealth() - 30);
                    if (player.getHealth()<0){
                        player.death();
                    }
                }
            });
        }

        //remove the explosion for after the timer ends
        if (timerCount == 2) {
            explosion.destroy();
            timerCount = 0;
        }
    }
}