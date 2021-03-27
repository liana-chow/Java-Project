package game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author      Liana Chowdhury
 * @version     3.0
 * @since       March 2021
 */
public class PlayerController implements KeyListener {

    //variables
    private Player player;
    /**
     * This stores all of the enemies in a list so it is easier to control.
     */
    private List<Enemy> EnemyList = new ArrayList<>();
    private GameSaverLoader gameSaverLoader = new GameSaverLoader();
    /**
     * This prevents player from spamming attacks and requires them to release the key
     */
    private boolean spamPrevention;

    //constructor
    public PlayerController(Player p) {
        player = p;
    }

    //keep to satisfy IJ
    @Override
    public void keyTyped(KeyEvent e) {
    }

    /**
     * Controls the player and game depending on what is pressed.
     *
     * Controls: W - Jump, A - Move Left, D - Move Right, J - Switch Heroes, L - Attack,
     * 1 - Save 1, 2 - Save 2, 3 - Save 3.
     * Whenever a key is pressed, it will also update all the information such as following the player,
     * checking if the chest and vortex have been created in order to create smooth game play.
     *
     * @param  e KeyEvent
     * @return
     */
    public void keyPressed(KeyEvent e) {
        //getting the key pressed
        int key = e.getKeyCode();

        //updating objects in the game
        updateGame();

        //allowing the user to save in different files
        if (key == KeyEvent.VK_1) {
            try {
                gameSaverLoader.save(player.getWorld(), "data/saves/fileSaveOne.txt");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            System.out.println("Saving in 1...");
        }
        if (key == KeyEvent.VK_2) {
            try {
                gameSaverLoader.save(player.getWorld(), "data/saves/fileSaveTwo.txt");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            System.out.println("Saving in 2...");
        }
        if (key == KeyEvent.VK_3) {
            try {
                gameSaverLoader.save(player.getWorld(), "data/saves/fileSaveThree.txt");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            System.out.println("Saving in 3...");
        }

        //allowing the player to switch heroes depending if conditions are met
        if (key == KeyEvent.VK_J) {
            int i = player.getHero()+1;
            if (player.getUnlockHero2() && i == 2){
                player.setHero(2);
                player.idle();
            } else if (player.getUnlockHero3() && i == 3){
                player.setHero(3);
                player.idle();
            } else {
                player.setHero(1);
                player.idle();
            }
        }

        //allowing the player to control using WAD
        if (key == KeyEvent.VK_W) {
            player.jumpUp();
            player.jump(player.getJumpingSpeed());
        } else if (key == KeyEvent.VK_A) {
            player.setFlip(true);
            player.runLeft();
            player.startWalking(-player.getWalkingSpeed());
        } else if (key == KeyEvent.VK_D) {
            player.setFlip(false);
            player.runRight();
            player.startWalking(player.getWalkingSpeed());
        }

        //allowing the player to attack differently based on who they are
        if (key == KeyEvent.VK_L && player.getHero() == 1 && !spamPrevention) {
            player.attack();
            Magic attack = new Magic(player.getWorld());
            MagicController control = new MagicController(attack);
            attack.addCollisionListener(control);
            spamPrevention = true;
        } else if (key == KeyEvent.VK_L && player.getHero() == 2 && !spamPrevention) {
            swordAttack(2f);
            spamPrevention = true;
        } else if (key == KeyEvent.VK_L && player.getHero() == 3 && !spamPrevention && player.getBombsLaunched() < 3) {
            bombAttack();
            player.setBombsLaunched(player.getBombsLaunched()+1);
            spamPrevention = true;
        }
    }

    /**
     * Controls the player and game depending on what is pressed.
     *
     * This is mainly used for spam prevention so the player can no longer send otu a stream of attacks
     * as well as stop walking.
     *
     * @param  e KeyEvent
     * @return
     */
    @Override
    public void keyReleased(KeyEvent e) {
        //getting the key
        int key = e.getKeyCode();

        //updating objects in the game
        updateGame();

        //preventing the player walking without pressing a key
        if (key == KeyEvent.VK_W) {
            player.idle();
        } else if (key == KeyEvent.VK_A) {
            player.stopWalking();
            player.idle();
        } else if (key == KeyEvent.VK_D) {
            player.stopWalking();
            player.idle();
        }  else if (key == KeyEvent.VK_L){
            player.idle();
        }

        if (key == KeyEvent.VK_L){
            spamPrevention = false;
        }
    }

    /**
     * Updates the game state
     *
     * @param
     *
     * @return
     */
    protected void updateGame(){
        //every time a key is pressed, update where the player to the enemy
        EnemyList = player.getWorld().getEnemy();
        for (int i = 0; i < EnemyList.size(); i++) {
            EnemyList.get(i).followPlayer();
            EnemyList.get(i).attackPlayer();
        }

        //continuously run as they have built in validation preventing it happening all the time
        player.getWorld().generateChest();
        player.getWorld().generateVortex();
    }

    /**
     * Attack style specific to Hero 2
     *
     * Using the same concept from following enemy, by comparing the distance between the player coordinates
     * and the enemy coordinates, we are able to see if they are within a range that you can attack with.
     *
     * @param
     *
     * @return
     */
    //another style of attack for the player
    protected void swordAttack(float swordSpan) {
        //change image
        player.attack();
        for (int i = 0; i < EnemyList.size(); i++) {
            Enemy enemy = EnemyList.get(i);
            float distanceX = Math.abs(enemy.getPosition().x - player.getPosition().x);
            float distanceY = Math.abs(enemy.getPosition().y - player.getPosition().y);
            //compare distance of all enemies to player
            if (distanceX < swordSpan && distanceY < swordSpan) {
                int damage = enemy.getHealth() - player.getDamage();
                enemy.setHealth(damage);
                //if the enemy health is below 0, destroy the object and add one to kill count
                if (enemy.getHealth() < 0) {
                    enemy.death();
                    player.setEnemiesKilled(player.getEnemiesKilled() + 1);
                    player.getWorld().getGame().getGUI().updateEnemiesKilled();
                }
            }
        }
    }

    /**
     * Attack style specific to Hero 3
     *
     * Using the same concept from magic, this creates a bomb object that will hurt the enemies.
     *
     * @param
     *
     * @return
     */
    protected void bombAttack(){
        //change image
        player.attack();
        Bomb b = new Bomb(player.getWorld());
    }

    //setter
    public void updatePlayer(Player player) { this.player = player; }
}
