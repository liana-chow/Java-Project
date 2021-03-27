package game;

import city.cs.engine.CollisionEvent;
import city.cs.engine.CollisionListener;

/**
 * @author      Liana Chowdhury
 * @version     3.0
 * @since       March 2021
 */
public class EnemyController implements CollisionListener {

    //variables
    private Player player;
    public EnemyController(Player p){
        this.player = p;
    }

    /**
     * Collision detection if the enemy collides with the player
     *
     * When the player collides with an enemy, they will get hurt based and if their health falls to
     * 0 then the player will be destroyed.
     *
     * @param  e CollisionEvent
     * @return
     */
    @Override
    public void collide(CollisionEvent e) {
        //collision detection if the enemy collides with the player
        //this allows the enemy to hurt the player
        if (e.getOtherBody() instanceof Enemy) {
            if (player.getHealth()<0){
                player.death();
            } else {
                //destroy the player object if their health drops below 0
                player.setHealth(player.getHealth() - ((Enemy) e.getOtherBody()).getDamage());
                player.getWorld().getGame().getGUI().updateHealthBar(player.getWorld());
                player.damaged();
            }
        }
    }

}