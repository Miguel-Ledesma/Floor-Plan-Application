package teamCview;

import java.awt.Component;
import java.awt.event.*;

/**
 * Movement Class - Contains the event and action listeners that allow items to be dragged into the
 * room and buttons to perform functions.
 * 
 * @author Team C
 * @version 10/28/2020
 *
 */
public class Movement implements MouseListener, MouseMotionListener
{
  private int x;
  private int y;

  /**
   * Allows movement for the items being dragged into the grid.
   * 
   * @param pns
   *          The component that requires movement
   */
  public void movement(Component pns)
  {
    pns.addMouseListener(this);
    pns.addMouseMotionListener(this);
  }

  @Override
  public void mouseDragged(MouseEvent event)
  {
    event.getComponent().setLocation((event.getX() + event.getComponent().getX()) - x,
        event.getY() + event.getComponent().getY() - y);
  }

  @Override
  public void mouseMoved(MouseEvent e)
  {
  }

  @Override
  public void mouseClicked(MouseEvent e)
  {
  }

  @Override
  public void mousePressed(MouseEvent e)
  {
  }

  @Override
  public void mouseReleased(MouseEvent e)
  {
  }

  @Override
  public void mouseEntered(MouseEvent e)
  {
  }

  @Override
  public void mouseExited(MouseEvent e)
  {
  }

}
