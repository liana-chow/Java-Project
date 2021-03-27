package game;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author      Liana Chowdhury
 * @version     3.0
 * @since       March 2021
 */
public class MouseHandler extends MouseAdapter {

    private GameLevel level;
    private GameView view;

    public MouseHandler(GameLevel w, GameView v){
        level = w;
        view = v;
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
