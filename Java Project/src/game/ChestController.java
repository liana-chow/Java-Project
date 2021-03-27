package game;

import city.cs.engine.*;

/**
 * @author      Liana Chowdhury
 * @version     3.0
 * @since       March 2021
 */
public class ChestController implements CollisionListener {

    //variables for the collision listener
    private final Chest c;

    //constructor
    public ChestController(Chest c){ this.c = c; }

    /**
     * Collision detection that will allow you to unlock a character or heal
     *
     * Based on the current level and depending on what you have already unlocked, you will be able to
     * collide which destroys the chest as a pick up item
     *
     * @param  e CollisionEvent
     * @return
     */
    @Override
    public void collide(CollisionEvent e) {
        //collision detection that will allow you to unlock a character
        if (e.getOtherBody() instanceof Player) {
            Player p = (Player) e.getOtherBody();
            //if player has not unlocked the 2nd hero for the first level then unlock
            if (!p.getUnlockHero2() && p.getWorld() instanceof Level1){
                p.setUnlockHero2(true);
            }
            //if player has not unlocked the 3rd hero for the second level then unlock
            if (!p.getUnlockHero3() && p.getWorld() instanceof Level2) {
                p.setUnlockHero3(true);
            }
            if (p.getWorld() instanceof Level3) {
                if (p.getHealth()<51){
                    p.setHealth(p.getHealth()+50);
                }
            }
            //destroy the chest and acknowledge that it's been picked up
            c.destroy();
            p.getWorld().setPickedUp(true);
        }
    }
}
