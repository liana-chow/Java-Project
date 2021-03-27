package game;

import city.cs.engine.UserView;
import java.awt.*;

/**
 * @author      Liana Chowdhury
 * @version     3.0
 * @since       March 2021
 */
public class GameView extends UserView {

    //variables
    private Image background;
    private GameLevel w;

    //constructor
    public GameView(GameLevel w, int width, int height) {
        super(w, width, height);
        this.w = w;
    }

    /**
     * This will allow the image to be passed on in order to make a unqiue background per level.
     *
     * @param
     * @return
     */
    //this will allow for the bg to be changed for each level
    public void setBackground(Image background){
        this.background = background;
    }

    //keep to satisfy IntelliJ
    @Override
    protected void paintForeground(Graphics2D g) {
    }

    //draw the background with the given image
    @Override
    protected void paintBackground(Graphics2D g) {
        g.drawImage(background, 0, 0, this);
    }
}
