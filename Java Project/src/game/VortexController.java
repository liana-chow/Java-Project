package game;

import city.cs.engine.*;

/**
 * @author      Liana Chowdhury
 * @version     3.0
 * @since       March 2021
 */
public class VortexController implements CollisionListener {

    //variables
    private GameLevel level;
    private Game game;

    //constructor
    public VortexController(GameLevel level, Game game){
        this.level = level;
        this.game = game;
    }

    /**
     * Collision detection that will allow you to move onto the next level or finish the game.
     *
     * Based on the current level and depending on if you have picked up the chest, you will be able to
     * either move onto the next level or finish the game.
     *
     * @param  e CollisionEvent
     * @return
     */
    //collision detection that will allow you to move onto the next level
    @Override
    public void collide(CollisionEvent e) {
        //if player collided with vortex and the
        //conditions for completing the level are fulfilled
        //goToNextLevel
        if (e.getOtherBody() instanceof Player && level.isComplete()){
            level.getVortex().getSound().stop();
            game.goToNextLevel();
        }
    }
}