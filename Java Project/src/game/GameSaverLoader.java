package game;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author      Liana Chowdhury
 * @version     3.0
 * @since       March 2021
 */
public class GameSaverLoader {
    protected static GameLevel level;

    /**
     * Saves the level into the specified file.
     *
     * To ensure that it is rewriting the file each time, the file writer is not in append mode. In order to write
     * all of the required information, it calls functions that specifically deals with that paticular object type
     * so it is easier to control.
     *
     * @param level GameLevel
     * @param fileName String
     *
     * @return
     */
    public void save(GameLevel level, String fileName) throws IOException {
        //this saves the level into a text file depending on which file they want to save to
        this.level = level;
        /**
         * To prevent accidentally rewriting the file, all the data is stored in one string that will then be written.
         */
        String data = level.getLevelName() + "\n";
        //split up each class that needs to be stored into its' own function for simplicity
        data = platformToString(data);
        data = playerToString(data);
        data = enemyToString(data);
        data = worldToString(data);
        FileWriter writer = null;
        try {
            //writing to file
            writer = new FileWriter(fileName);
            writer.write(data);
        } finally {
            //close file
            if (writer != null) {
                writer.close();
            }
        }
    }

    /**
     * Loads the level from the specified file.
     *
     * To load the file, it reads each line and stores it depending on what the information is which is then used
     * to create a level and regenerate the objects with their old information
     *
     * @param game Game, fileName String
     * @return level GameLevel
     */
    public static GameLevel load(Game game, String fileName) throws IOException{
        //this loads up the save that is selected
        FileReader fr = null;
        BufferedReader reader = null;
        //create variables and lists that will store the data
        String levelName = "";
        List<List<String>> Platform = new ArrayList<>();
        List<List<String>> Enemies = new ArrayList<>();
        List<String> Player = new ArrayList<>();
        List<String> World = new ArrayList<>();
        try {
            System.out.println("Loading ...");
            fr = new FileReader(fileName);
            reader = new BufferedReader(fr);
            //this will read it line by line
            String line = reader.readLine();
            while (line != null) {
                // file is assumed to contain data that is split by ','
                String[] tokens = line.split(",");
                //turn the tokens into an list for easy manipulation
                List<String> temp = new ArrayList<>(Arrays.asList(tokens));
                if (temp.size() == 1) {
                    //save the level name
                    levelName = temp.get(0);
                    System.out.println(levelName);
                } else if (temp.get(0).equals("P")) {
                    //add information about platforms into the list
                    Platform.add(temp);
                } else if (temp.get(0).equals("Pl")) {
                    //add information about player into the list
                    Player = temp;
                } else if (temp.get(0).equals("E")){
                    //add information about enemies into the list
                    Enemies.add(temp);
                }  else if (temp.get(0).equals("W")) {
                    //add information about world into the list
                    World = temp;
                }
                //read next line
                line = reader.readLine();
            }
            System.out.println("...done.");

            //create the level depending on what the saved level was
            if (levelName.equals("Level1")){
                level = new Level1(game);
                System.out.println("Level1 created");
            }
            if (levelName.equals("Level2")){
                level = new Level2(game);
                System.out.println("Level2");
            }
            if (levelName.equals( "Level3")){
                level = new Level3(game);
                System.out.println("Level3");
            }

            settingUpLevel(level, Platform, Enemies , Player, World);

        } finally {
            //close the reader
            if (reader != null) {
                reader.close();
            }
            if (fr != null) {
                fr.close();
            }
        }
        return level;
    }

    /**
     * Saves the level into the specified file.
     *
     * To ensure that it is rewriting the file each time, the file writer is not in append mode. In order to write
     * all of the required information, it calls functions that specifically deals with that paticular object type
     * so it is easier to control.
     *
     * @param level GameLevel, Platform List<List<String>> Platform,
     *              Enemies List<List<String>>,Player List<String>, World List<String>.
     *
     * @return
     */
    protected static void settingUpLevel(GameLevel level, List<List<String>> Platform,
                                         List<List<String>> Enemies, List<String> Player, List<String> World){
        //for all the different classes, it will regenerate what was already there
        level.regeneratePlatforms(Platform);
        level.regenerateEnemies(Enemies);
        level.regeneratePlayer(Player);
        level.regenerateWorld(World);
    }

    /**
     * Turns all of the player information into a string
     *
     * @param data String
     *
     * @return data String
     */
    protected String playerToString(String data){
        //putting together all of the player data into one line
        Player player = level.getPlayer();
        String temp = "Pl," + player.getHero() + "," +
                player.getHealth() + "," +
                player.getUnlockHero2() + "," +
                player.getUnlockHero3() + "," +
                player.getEnemiesKilled() + "," +
                player.getPosition() + "\n";
        data+= temp;
        return data;
    }

    /**
     * Turns all of the platform information into a string
     *
     * @param data String
     *
     * @return data String
     */
    protected String platformToString(String data){
        //putting together all of the platform data into the string, a line a platform
        List <String> Platform = level.getPlatforms();
        for (int i = 0; i < Platform.size()-3; i = i +3){
            String temp = "P," + level.getPlatforms().get(i)
                    + ","+ level.getPlatforms().get(i+1) + ","+
                    level.getPlatforms().get(i+2)+"\n";
            data+= temp;
        }
        return data;
    }

    /**
     * Turns all of the enemies information into a string
     *
     * @param data String
     *
     * @return data String
     */
    protected String enemyToString(String data){
        //putting together all of the enemies' data into the string, a line an enemy
        List<Enemy> EnemyList = level.getEnemy();
        EnemyList.removeAll(Collections.singleton(null));
        for (int i = 0; i < EnemyList.size(); i++){
            if (EnemyList.get(i) != null) {
                String temp = "E," + EnemyList.get(i).getHealth() + "," + EnemyList.get(i).getPosition() + "\n";
                data += temp;
            }
        }
        return data;
    }

    /**
     * Turns all of the world information into a string
     *
     * @param data String
     *
     * @return data String
     */
    protected String worldToString(String data){
        //turning world data into a string
        String temp = "W," + level.getGenerated() + "," +
                level.getPickedUp() + "\n";
        data+=temp;
        return data;
    }
}
