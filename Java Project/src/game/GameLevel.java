package game;

import city.cs.engine.*;
import city.cs.engine.Shape;
import org.jbox2d.common.Vec2;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * @author      Liana Chowdhury
 * @version     3.0
 * @since       March 2021
 */
public abstract class GameLevel extends World {
    //declare variables
    private Player player;
    /**
     * This stores a list of enemy objects so it is easier to control
     */
    private List<Enemy> EnemyList = new ArrayList<>();
    /**
     * This stored a list of platforms so it is easier to control
     */
    private List<StaticBody> listOfPlatforms = new ArrayList<>();
    /**
     * This stores data about the platforms such as the coordinates and the length of the box.
     */
    private List<String> PlatformList = new ArrayList<>();
    private Chest chest;
    private Vortex vortex;
    /**
     * This allows us to check if the chest object has been generated for that particular level
     */
    private boolean generated = false;
    /**
     * This allows us to check if the chest object has been 'picked up'.
     */
    private boolean pickedUp = false;
    private boolean generateVortex = false;
    private SoundClip background;
    private Game game;

    //constructor
    public GameLevel(Game game, int e) {
        //gravity set to 20 to bring down objects faster
        this.setGravity(20f);
        this.game = game;

        //create ground and walls around the screen out of view to prevent objects running out
        Shape left = new BoxShape(0.25f, 18f);
        StaticBody wallLeft = new StaticBody(this, left);
        wallLeft.setPosition(new Vec2(23f, 0f));

        Shape right = new BoxShape(0.25f, 18f);
        StaticBody wallRight = new StaticBody(this, right);
        wallRight.setPosition(new Vec2(-23f, 0f));

        Shape shape = new BoxShape(32f, 0.25f);
        StaticBody ground = new StaticBody(this, shape);
        ground.setPosition(new Vec2(0f, -16f));

        //generate platforms for the game
        generatePlatforms();

        //all levels have a player and enemies
        //the player needs to kill all enemies, take the chest and reach the vortex to move on

        //randomly place the player
        player = new Player(this);
        Random random = new Random();
        float posX = random.nextInt(20 + 20) - 20;
        player.setPosition(new Vec2(posX, 10));

        //generate the enemies and add the collision lister
        generateEnemies(e);
        EnemyController destroy = new EnemyController(player);
        player.addCollisionListener(destroy);

    }

    /**
     * Randomly generates the number of enemies for the level.
     *
     * This creates the enemies for the level and puts them together into a list in order to
     * make it easier to keep track of all the enemies.
     *
     * @param enemyNum Integer
     * @return
     */
    public void generateEnemies(int enemyNum){
        //creates the required number of enemies for the world
        for (int i = 0; i < enemyNum; i++) {
            //randomize the position of the enemy
            Random random = new Random();
            float posX = random.nextInt(17 + 17) - 17;
            float posY = random.nextInt(7 + 7) - 7;
            Enemy enemy = new Enemy(this);
            enemy.setPosition(new Vec2(posX, posY));
            //make sure the enemy follows the player and attacks them
            enemy.followPlayer();
            enemy.attackPlayer();
            //add to a list for ease of control and reference
            EnemyList.add(enemy);
        }
    }

    /**
     * Randomly generates the number of platforms for the level.
     *
     * This creates the platforms for the level and puts them together into a list in order to
     * make it easier to keep track of all the platforms.
     *
     * @param
     * @return
     */
    public void generatePlatforms(){
        //randomly generate how many platforms will be in the level
        Random random = new Random();
        int platforms = random.nextInt(10 - 2) + 3;
        for (int i = 0; i < platforms; i++) {
            //randomly generate the position of the platform and create it
            int posX = random.nextInt(22 + 22) - 22;
            int posY = random.nextInt(15 + 11) - 11;
            int boxLength = random.nextInt(6 - 2) + 2;
            Shape rectangle = new BoxShape(boxLength, 0.25f);
            StaticBody wall = new StaticBody(this, rectangle);
            wall.setPosition(new Vec2(posX, posY));
            listOfPlatforms.add(wall);
            PlatformList.add(String.valueOf(posX));
            PlatformList.add(String.valueOf(posY));
            PlatformList.add(String.valueOf(boxLength));
        }
    }

    /**
     * Randomly generates the position of the chest once the player has met the requirements
     *
     * @param
     * @return
     */
    public void generateChest(){
        //create a chest if the current level hasn't had one generated already
        if (player.getEnemiesKilled() >= 2 && !generated){
            chest = new Chest(player.getWorld());
            ChestController control = new ChestController(chest);
            chest.addCollisionListener(control);
            //set to true to prevent another one being generated
            generated = true;
        }
    }

    /**
     * Randomly generates the position of the vortex once the player has met the requirements
     *
     * @param
     * @return
     */
    public void generateVortex(){
        //generate the vortex that will lead to the next level
        if (player.getEnemiesKilled() == player.getWorld().getEnemy().size() && pickedUp && !generateVortex){
            vortex = new Vortex(player.getWorld());
            VortexController control = new VortexController(player.getWorld(), game);
            vortex.addCollisionListener(control);
            //set to true to prevent another one being generated
            generateVortex = true;
        }
    }

    /**
     * Recreates the platforms from the save.
     *
     * Due to the automatic random generation of the platforms within each level, the platforms first must be
     * removed in order to be able to recreate the old ones using the data that is stored int eh text files.
     *
     * @param Platforms List<List<String>>
     * @return
     */
    public void regeneratePlatforms(List<List<String>> Platforms){
        //remove the platforms automatically generated
        for (int i = 0; i < listOfPlatforms.size(); i++) {
            listOfPlatforms.get(i).destroy();
        }
        //create a clean slate for the construction of old platforms
        listOfPlatforms.clear();
        PlatformList.clear();
        for (int i = 0; i < Platforms.size(); i++) {
            //generate the platform where it used to be
            int posX = Integer.parseInt(Platforms.get(i).get(1));
            int posY = Integer.parseInt(Platforms.get(i).get(2));
            int boxLength = Integer.parseInt(Platforms.get(i).get(3));
            Shape rectangle = new BoxShape(boxLength, 0.25f);
            StaticBody wall = new StaticBody(this, rectangle);
            wall.setPosition(new Vec2(posX, posY));
            //add to list for easy manipulation
            listOfPlatforms.add(wall);
            PlatformList.add(String.valueOf(posX));
            PlatformList.add(String.valueOf(posY));
            PlatformList.add(String.valueOf(boxLength));
        }
    }

    /**
     * Recreates the enemies from the save.
     *
     * Due to the automatic random generation of the enemies within each level, the enemies first must be
     * removed in order to be able to recreate the old ones using the data that is stored int eh text files.
     *
     * @param Enemies List<List<String>>
     * @return
     */
    public void regenerateEnemies(List<List<String>> Enemies){
        //remove the enemies automatically generated with a new level
        for (int i = 0; i < EnemyList.size(); i++) {
            EnemyList.get(i).destroy();
        }
        //create a clean slate for the new generation
        EnemyList.clear();
        for (int i = 0; i < Enemies.size(); i++) {
            //generate enemies in the positions they were with the health they were at
            int health = Integer.parseInt(Enemies.get(i).get(1));
            float posX = Float.parseFloat(Enemies.get(i).get(2).substring(1));
            float posY = Float.parseFloat(Enemies.get(i).get(3).substring(0, Enemies.get(i).get(3).length() - 1));
            Enemy enemy = new Enemy(this);
            enemy.setPosition(new Vec2(posX, posY));
            enemy.setHealth(health);
            //make sure the enemy follows the player and attacks them
            enemy.followPlayer();
            enemy.attackPlayer();
            //add to a list for ease of control and reference
            EnemyList.add(enemy);
            if (enemy.getHealth()<0){
                enemy.destroy();
            }
        }
    }

    /**
     * Recreates the player from the save.
     *
     * All of the stats that are saved in the file are then recovered.
     *
     * @param Player List<String>
     * @return
     */
    public void regeneratePlayer(List<String> Player){
        //pass on all of the stats and set position of the player at the state of saving
        this.getPlayer().setHero(Integer.parseInt(Player.get(1)));
        this.getPlayer().setHealth(Integer.parseInt(Player.get(2)));
        this.getPlayer().setUnlockHero2(Boolean.parseBoolean(Player.get(3)));
        this.getPlayer().setUnlockHero3(Boolean.parseBoolean(Player.get(4)));
        this.getPlayer().setEnemiesKilled(Integer.parseInt(Player.get(5)));
        float posX = Float.parseFloat(Player.get(6).substring(1));
        float posY = Float.parseFloat(Player.get(7).substring(0, Player.get(7).length() - 1));
        this.getPlayer().setPosition(new Vec2(posX, posY));
    }
    
    //abstract method to help with saving
    public abstract String getLevelName();

    //getters and setters
    public Player getPlayer() { return player; }
    public List<Enemy> getEnemy() { return EnemyList; }
    public List<String> getPlatforms() { return PlatformList; }

    public Vortex getVortex(){ return vortex;}

    public boolean getGenerated() { return generated;}
    public void setGenerated(boolean g) { this.generated = g;}

    public SoundClip getGameMusic() { return background; }

    public Game getGame() { return game; }

    public boolean getGeneratedVortex() { return generateVortex;}
    public void setGeneratedVortex(boolean g) { this.generateVortex = g;}

    public boolean getPickedUp() { return pickedUp;}
    public void setPickedUp(boolean p) { this.pickedUp = p;}

    public abstract boolean isComplete();
    public abstract Image paintBackground();
}