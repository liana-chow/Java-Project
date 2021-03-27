package game;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;
import java.util.Random;

/**
 * @author      Liana Chowdhury
 * @version     3.0
 * @since       March 2021
 */
public class Chest extends DynamicBody {

    //image and shape of the chest
    private static final BodyImage chestImage = new BodyImage("data/chest tile.png", 2f);
    private static final Shape chestShape = new BoxShape(1f, 1f);

    //randomly generate the x-coordinate for the chest
    Random random = new Random();
    float posX = random.nextInt(20 + 20) - 20;

    /**
     * Constructor for the Chest object
     *
     * @param  w World
     * @return
     */
    //constructor
    public Chest(World w) {
        super(w, chestShape);
        addImage(chestImage);
        this.setPosition(new Vec2(posX, -14));
    }
}
