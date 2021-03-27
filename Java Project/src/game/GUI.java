package game;

import javax.swing.*;

/**
 * @author      Liana Chowdhury
 * @version     3.0
 * @since       March 2021
 */
public class GUI extends JPanel {

    //variables
    private GameLevel level;
    private final JPanel gamePanel;
    private final JProgressBar healthBar;
    private final JLabel enemiesKilled;

    /**
     * Creation of the GUI
     *
     * This creates the GUI with all of the buttons and their respective listeners.
     *
     * @param  level GameLevel
     * @param game Game
     *
     * @return
     */
    //constructor
    public GUI(GameLevel level, Game game) {
        this.level = level;

        //assigning variables
        gamePanel= new JPanel();
        healthBar = new JProgressBar(0, 100);
        JButton pauseButton = new JButton(new ImageIcon("data/icons/pause.png"));
        JButton quitButton = new JButton(new ImageIcon("data/icons/quit.png"));
        JButton playButton = new JButton(new ImageIcon("data/icons/play.png"));
        JButton restartButton = new JButton(new ImageIcon("data/icons/restart.png"));
        JButton muteButton = new JButton(new ImageIcon("data/icons/mute.png"));
        JButton unmuteButton = new JButton(new ImageIcon("data/icons/unmute.png"));
        JButton soundUpButton = new JButton(new ImageIcon("data/icons/soundup.png"));
        JButton soundDownButton = new JButton(new ImageIcon("data/icons/sounddown.png"));
        JButton saveOneButton = new JButton(new ImageIcon("data/icons/1.png"));
        JButton saveTwoButton = new JButton(new ImageIcon("data/icons/2.png"));
        JButton saveThreeButton = new JButton(new ImageIcon("data/icons/3.png"));
        enemiesKilled = new JLabel("Enemies Killed: " + level.getPlayer().getEnemiesKilled());
        updateEnemiesKilled();

        //making the button transparent to only show the image
        makeTransparent(pauseButton);
        makeTransparent(quitButton);
        makeTransparent(playButton);
        makeTransparent(restartButton);
        makeTransparent(muteButton);
        makeTransparent(unmuteButton);
        makeTransparent(soundUpButton);
        makeTransparent(soundDownButton);
        makeTransparent(saveOneButton);
        makeTransparent(saveTwoButton);
        makeTransparent(saveThreeButton);

        //add the buttons to the panel
        gamePanel.add(healthBar);
        gamePanel.add(enemiesKilled);
        gamePanel.add(playButton);
        gamePanel.add(pauseButton);
        gamePanel.add(restartButton);
        gamePanel.add(quitButton);
        gamePanel.add(muteButton);
        gamePanel.add(unmuteButton);
        gamePanel.add(soundUpButton);
        gamePanel.add(soundDownButton);
        gamePanel.add(saveOneButton);
        gamePanel.add(saveTwoButton);
        gamePanel.add(saveThreeButton);

        //all of the action listeners
        pauseButton.addActionListener(e -> game.pause());

        playButton.addActionListener(e -> game.play());

        quitButton.addActionListener(e -> game.exit());

        restartButton.addActionListener(e -> game.restart());

        soundDownButton.addActionListener(e -> game.soundDown());

        soundUpButton.addActionListener(e -> game.soundUp());

        muteButton.addActionListener(e -> game.mute());

        unmuteButton.addActionListener(e -> game.unmute());

        saveOneButton.addActionListener(e -> game.saveOneButton());

        saveTwoButton.addActionListener(e -> game.saveTwoButton());

        saveThreeButton.addActionListener(e -> game.saveThreeButton());


    }

    /**
     * Turns all of the buttons transparent besides the icon
     *
     * @param button JButton
     *
     * @return
     */
    public void makeTransparent(JButton button){
        //method to make the buttons transparent
        button.setBorder(null);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
    }

    /**
     * Updates the enemies killed and immediately updates the GUI to display it
     *
     * @param
     *
     * @return
     */
     public void updateEnemiesKilled() {
        //method to update the text to show the correct value
        enemiesKilled.setText("Enemies Killed: " + level.getPlayer().getEnemiesKilled());
        enemiesKilled.paintImmediately(enemiesKilled.getVisibleRect());
     }


    //getters and setters
    public JPanel getGamePanel(){
        return gamePanel;
    }
    public JProgressBar getHealthBar() { return healthBar; }
    public void updateHealthBar(GameLevel level){
        healthBar.setValue(level.getPlayer().getHealth());
        healthBar.paintImmediately(healthBar.getVisibleRect());
    }
    public void setLevel(GameLevel level) {
        this.level = level;
        updateEnemiesKilled();}
}
