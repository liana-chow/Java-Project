package game;

import city.cs.engine.*;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

/**
 * @author      Liana Chowdhury
 * @version     3.0
 * @since       March 2021
 */
public class Enemy extends Walker {

    private final GameLevel w;

    //create the shape for the enemy
    private static final Shape oneShape = new PolygonShape(-0.22f,0.38f,
            -1.82f,-1.92f, -0.37f,-4.18f, 1.15f,-2.25f);

    //create the images for the enemy
    private static final BodyImage fodder1Attack = new BodyImage("data/fodder/one/attack.gif",10f);
    private static final BodyImage fodder1Death = new BodyImage("data/fodder/one/death.gif",10f);
    private static final BodyImage fodder1Idle = new BodyImage("data/fodder/one/idle.gif",10f);
    private static final BodyImage fodder1Jump = new BodyImage("data/fodder/one/jump.gif",10f);
    private static final BodyImage fodder1Run = new BodyImage("data/fodder/one/run.gif",10f);

    //set the enemy stats
    private int health = 75;
    private final int damage = 10;

    //sound clip for general monster sounds
    private static SoundClip monsterSound;
    static {
        try {
            monsterSound = new SoundClip("data/sounds/monster.wav");
        } catch (UnsupportedAudioFileException | IOException |
                LineUnavailableException e) {
            System.out.println(e);
        }

    }

    //constructor
    public Enemy(GameLevel world) {
        super(world, oneShape);
        addImage(fodder1Idle);
        this.w = world;
        monsterSound.setVolume(0.5);
        monsterSound.loop();
    }

    /**
     * This allows the enemy object to follow the player.
     *
     * By comparing the coordinates of the player in comparison to the enemy, we can get them to follow
     *
     * @param
     * @return
     */
    //this allows the body to go towards the object by comparing it's relative position to the player
    public void followPlayer(){
        //following along the x-axis
        if (w.getPlayer().getPosition().x > this.getPosition().x){
            this.startWalking(2f);
            this.removeAllImages();
            this.addImage(fodder1Run);
        } else if (w.getPlayer().getPosition().x < this.getPosition().x){
            this.startWalking(-2f);
            this.removeAllImages();
            this.addImage(fodder1Run).flipHorizontal();
        }
        //following along the y-axis
        if (w.getPlayer().getPosition().y > this.getPosition().y){
            this.jump(10f);
            this.removeAllImages();
            if (w.getPlayer().getPosition().x < this.getPosition().x) {
                this.addImage(fodder1Jump).flipHorizontal();
            } else {
                this.addImage(fodder1Jump);
            }
        }
    }

    /**
     * This allows the enemy object to attack the player.
     *
     * By comparing the coordinates of the player in comparison to the enemy, we can get them to play the
     * attack animation when they are close.
     *
     * @param
     * @return
     */
    //attacks the player if they are within a certain distance
    public void attackPlayer(){
        float distanceX = Math.abs(this.getPosition().x - w.getPlayer().getPosition().x);
        float distanceY = Math.abs(this.getPosition().y - w.getPlayer().getPosition().y);
        if (distanceX<2f&&distanceY<2f){
            this.removeAllImages();
            this.addImage(fodder1Attack);
        }
    }

    //death animation
    public void death(){
        monsterSound.stop();
        this.removeAllImages();
        this.addImage(fodder1Death);
        this.destroy();
    }

    //getters and setters
    public int getHealth() { return health; }
    public void setHealth(int health) { this.health = health; }
    public int getDamage() { return damage; }

    public World getW() { return w;}
    public Player getP() { return w.getPlayer();}
}
