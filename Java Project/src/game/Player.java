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
public class Player extends Walker {

    private final GameLevel world;

    //all of the variables that the player needs
    private int health = 100;
    private int damage = 10;
    private final int WALKING_SPEED = 2;
    private final int JUMPING_SPEED = 6;
    private int enemiesKilled;
    /**
     * Limits bomb creation to 3 per level.
     */
    private int bombsLaunched = 0;

    //these variables allow the player to switch between heroes
    private int hero = 1;
    private boolean unlockHero2 = false;
    private boolean unlockHero3 = false;
    /**
     * This allows us to check if the player is travelling towards the left and so will have to alter all images.
     */
    private boolean isFlip = false;

    //the sound of injury
    private static SoundClip oof;
    static {
           try {
               oof = new SoundClip("data/sounds/oof.wav");
           } catch (UnsupportedAudioFileException | IOException |
                   LineUnavailableException e) {
               System.out.println(e);
           }

    }

    //the sound of magic
    private static SoundClip magic;
    static {
        try {
            magic = new SoundClip("data/sounds/magic.wav");
        } catch (UnsupportedAudioFileException | IOException |
                LineUnavailableException e) {
            System.out.println(e);
        }

    }

    //the sound of sword
    private static SoundClip sword;
    static {
        try {
            sword = new SoundClip("data/sounds/sword.wav");
        } catch (UnsupportedAudioFileException | IOException |
                LineUnavailableException e) {
            System.out.println(e);
        }

    }



    //creating the polygon for the player
    private static final Shape playerShape = new PolygonShape(0.01f,1.78f, -1.16f,0.16f, -0.92f,-1.97f,
            0.05f,-2.26f, 0.85f,-1.72f, 0.99f,0.08f, 0.65f,1.11f);

    //creating all the images for the Hero 1
    private static final BodyImage hero1Attack = new BodyImage("data/hero1/attack.gif", 5f);
    private static final BodyImage hero1Death = new BodyImage("data/hero1/death.gif", 5f);
    private static final BodyImage hero1Idle = new BodyImage("data/hero1/idle.gif", 5f);
    private static final BodyImage hero1Jump = new BodyImage("data/hero1/jump.gif", 5f);
    private static final BodyImage hero1Run = new BodyImage("data/hero1/run.gif", 5f);

    //creating all the images for the Hero 2
    private static final BodyImage hero2Attack = new BodyImage("data/hero2/attack.gif", 6f);
    private static final BodyImage hero2Death = new BodyImage("data/hero2/death.gif", 6f);
    private static final BodyImage hero2Idle = new BodyImage("data/hero2/idle.gif", 6f);
    private static final BodyImage hero2Jump = new BodyImage("data/hero2/jump.gif", 6f);
    private static final BodyImage hero2Run = new BodyImage("data/hero2/run.gif", 6f);

    //creating all the images for the Hero 3
    private static final BodyImage hero3Attack = new BodyImage("data/hero3/attack.gif", 16.5f);
    private static final BodyImage hero3Death = new BodyImage("data/hero3/death.gif", 16.5f);
    private static final BodyImage hero3Idle = new BodyImage("data/hero3/idle.gif", 16.5f);
    private static final BodyImage hero3Jump = new BodyImage("data/hero3/jump.gif", 16.5f);
    private static final BodyImage hero3Run = new BodyImage("data/hero3/run.gif", 16.5f);

    //setting the first image as an idle animation
    public Player(GameLevel w) {
        super(w, playerShape);
        if (hero == 1) {
            addImage(hero1Idle);
        } else if (hero == 2){
            addImage(hero2Idle);
        } else if (hero == 3){
            addImage(hero3Idle);
        }
        this.world = w;
    }

    /**
     * Changes the player image and plays a sound
     *
     * @param
     * @return
     */
    //creating an attack function to change the body image to an attack animation
    public void attack(){
        this.removeAllImages();
        if (hero == 1 && isFlip) {
            addImage(hero1Attack).flipHorizontal();
            magic.play();
        } else if (hero == 2 && isFlip){
            addImage(hero2Attack).flipHorizontal();
            sword.play();
        } else if (hero == 3 && isFlip){
            addImage(hero3Attack).flipHorizontal();
        } else if (hero == 1) {
            addImage(hero1Attack);
            magic.play();
        } else if (hero == 2){
            addImage(hero2Attack);
            sword.play();
        } else if (hero == 3){
            addImage(hero3Attack);
        }
    }

    //creating an death function to change the body image to an death animation
    public void death(){
        this.removeAllImages();
        if (hero == 1 && isFlip) {
            addImage(hero1Death).flipHorizontal();
        } else if (hero == 2 && isFlip){
            addImage(hero2Death).flipHorizontal();
        } else if (hero == 3 && isFlip){
            addImage(hero3Death).flipHorizontal();
        } else if (hero == 1) {
            addImage(hero1Death);
        } else if (hero == 2){
            addImage(hero2Death);
        } else if (hero == 3){
            addImage(hero3Death);
        }
        this.destroy();
        System.out.println("Game Over! You Lose!");
        System.exit(0);
    }

    //creating an idle function to change the body image to an idle animation
    public void idle(){
        this.removeAllImages();
        if (hero == 1 && isFlip) {
            addImage(hero1Idle).flipHorizontal();
        } else if (hero == 2 && isFlip){
            addImage(hero2Idle).flipHorizontal();
        } else if (hero == 3 && isFlip){
            addImage(hero3Idle).flipHorizontal();
        } else if (hero == 1) {
            addImage(hero1Idle);
        } else if (hero == 2){
            addImage(hero2Idle);
        } else if (hero == 3){
            addImage(hero3Idle);
        }
    }

    //creating a run function to change the body image to a jumping animation
    public void jumpUp(){
        this.removeAllImages();
        if (hero == 1 && isFlip) {
            addImage(hero1Jump).flipHorizontal();
        } else if (hero == 2 && isFlip){
            addImage(hero2Jump).flipHorizontal();
        } else if (hero == 3 && isFlip){
            addImage(hero3Jump).flipHorizontal();
        } else if (hero == 1) {
            addImage(hero1Jump);
        } else if (hero == 2){
            addImage(hero2Jump);
        } else if (hero == 3){
            addImage(hero3Jump);
        }
    }

    //creating a run function to change the body image to a right facing running animation
    public void runRight(){
        this.removeAllImages();
        if (hero == 1) {
            addImage(hero1Run);
        } else if (hero == 2){
            addImage(hero2Run);
        } else if (hero == 3){
            addImage(hero3Run);
        }
    }

    //creating a run function to change the body image to a left facing running animation
    public void runLeft(){
        this.removeAllImages();
        if (hero == 1) {
            addImage(hero1Run).flipHorizontal();
        } else if (hero == 2){
            addImage(hero2Run).flipHorizontal();
        } else if (hero == 3){
            addImage(hero3Run).flipHorizontal();
        }
    }

    //creating a function that will play the damage sound
    public void damaged(){
        oof.play();
    }


    //all the getters and setters made
    public int getHealth() { return health; }
    public void setHealth(int health) { this.health = health; }

    public int getDamage() { return damage*hero; }
    public void setDamage(int damage) { this.damage = damage; }

    public int getHero() { return hero; }
    public void setHero(int hero) { this.hero = hero; }

    public boolean getUnlockHero2() { return unlockHero2; }
    public void setUnlockHero2(boolean unlockHero2) { this.unlockHero2 = unlockHero2; }

    public boolean getUnlockHero3() { return unlockHero3; }
    public void setUnlockHero3(boolean unlockHero3){ this.unlockHero3 = unlockHero3; }

    public float getWalkingSpeed() { return WALKING_SPEED+(5*hero); }
    public float getJumpingSpeed() { return JUMPING_SPEED+(5*hero); }

    public int getEnemiesKilled() { return enemiesKilled; }
    public void setEnemiesKilled(int enemiesKilled) { this.enemiesKilled = enemiesKilled; }

    public int getBombsLaunched() { return bombsLaunched; }
    public void setBombsLaunched(int bombsLaunched) { this.bombsLaunched = bombsLaunched; }

    public boolean getFlip() { return isFlip; }
    public void setFlip(boolean flip) { this.isFlip = flip; }

    public GameLevel getWorld() { return world;}
}

