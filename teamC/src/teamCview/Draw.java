package teamCview;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JPanel;

import teamCcontroller.Functionality;

/**
 * Draws walls and rooms.
 * 
 * @author Team C
 * @version 11/13/2020
 *
 */
public class Draw extends JPanel implements MouseListener, MouseMotionListener
{
  private static final long serialVersionUID = 1L;
  private int width = 0;
  private int height = 0;
  private int rows = 0;
  private int cols = 0;
  private int xin, yin, xdrag, ydrag, xend, yend;
  private boolean room = false;
  private Point inPoint = null;
  private Point endPoint = null;
  private Functionality functions = new Functionality();
  private ArrayList<Line2D> myLines = new ArrayList<Line2D>();
  private ArrayList<Rectangle2D> myRooms = new ArrayList<Rectangle2D>();

  /**
   * Draw constructor.
   * 
   * @param w
   *          width
   * @param h
   *          height
   * @param r
   *          row
   * @param c
   *          col
   */
  public Draw(int w, int h, int r, int c)
  {
    width = w;
    height = h;
    setSize(width, height);
    rows = r;
    cols = c;

  }

  /**
   * Paint method.
   * 
   * @param g
   *          graphics
   */
  public void paint(Graphics g)
  {
    super.paint(g);
    int i;
    width = getSize().width;
    height = getSize().height;
    g.setColor(functions.getNewColor());

    // draw the rows
    int rowHt = height / (rows);
    for (i = 0; i < rows; i++)
      g.drawLine(0, i * rowHt, width, i * rowHt);

    // draw the columns
    int rowWid = width / (cols);
    for (i = 0; i < cols; i++)
      g.drawLine(i * rowWid, 0, i * rowWid, height);
  }

  /**
   * Paints the walls and room.
   * 
   * @param g
   *          graphics
   */
  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);

    Graphics2D g2d = (Graphics2D) g;
    addMouseListener(this);
    addMouseMotionListener(this);

    g.setColor(Color.BLACK);
    g2d.setStroke(new BasicStroke(5));

    // creates walls when 'add wall' button is active and adds it to the array
    if (functions.getWallActive())
    {
      g.drawLine(xin, yin, xdrag, ydrag);

      if (xdrag - xin > 20 || xin - xdrag > 20)
      {
        g.drawLine(xin, yin + 10, xdrag, ydrag + 10);
      }
      else
      {
        g.drawLine(xin + 10, yin, xdrag + 10, ydrag);
      }

      if (!(inPoint == null) && !(endPoint == null))
      {
        myLines.add(new Line2D.Double(xin, yin, xend, yend));

        if (xdrag - xin > 20 || xin - xdrag > 20)
        {
          myLines.add(new Line2D.Double(xin, yin + 10, xend, yend + 10));
        }
        else
        {
          myLines.add(new Line2D.Double(xin + 10, yin, xend + 10, yend));
        }

      }

    }

    // creates room when 'add room' button is active and adds it to the array
    if (functions.getRoomActive())
    {
      if (room)
      {
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(5));
        g.drawRect(xin - 300, yin - 200, 600, 400);
        g.drawRect((xin - 10 - 300), (yin - 10) - 200, 620, 420);

        if (!(inPoint == null))
        {
          myRooms.add(new Rectangle2D.Double(xin - 300, yin - 200, 600, 400));
          myRooms.add(new Rectangle2D.Double((xin - 10 - 300), (yin - 10) - 200, 620, 420));
        }
      }
    }

    // draws the walls from the array onto the grid
    Iterator<Line2D> wallIterator = myLines.iterator();

    while (wallIterator.hasNext())
    {
      Line2D wall = wallIterator.next();

      if (functions.getEraserActive())
      {

        if (((wall.getX1() >= xin - 30 && wall.getX1() <= xin + 30)
            && (wall.getY1() >= yin - 30 && wall.getY1() <= yin + 30))
            || (wall.getX2() >= xin - 30 && wall.getX2() <= xin + 30
                && (wall.getY2() >= yin - 30 && wall.getY2() <= yin + 30)))
        {
          wallIterator.remove();
        }

      }

      g2d.draw(wall);
    }

    // draws the room(s) from the array onto the grid
    Iterator<Rectangle2D> roomIterator = myRooms.iterator();

    while (roomIterator.hasNext())
    {
      Rectangle2D rooms = roomIterator.next();

      if (functions.getEraserActive())
      {
        // Top left corner
        if ((rooms.getX() >= xin - 30 && rooms.getX() <= xin + 30)
            && (rooms.getY() >= yin - 30 && rooms.getY() <= yin + 30))
        {
          roomIterator.remove();
        }
        // Top right corner
        else if ((rooms.getX() + rooms.getWidth() >= xin - 30
            && rooms.getX() + rooms.getWidth() <= xin + 30)
            && (rooms.getY() >= yin - 30 && rooms.getY() <= yin + 30))
        {
          roomIterator.remove();
        }
        // Bottom left corner
        else if ((rooms.getX() >= xin - 30 && rooms.getX() <= xin + 30)
            && (rooms.getY() + rooms.getHeight() >= yin - 30
                && rooms.getY() + rooms.getHeight() <= yin + 30))
        {
          roomIterator.remove();
        }
        // Bottom right corner
        else if ((rooms.getX() + rooms.getWidth() >= xin - 30
            && rooms.getX() + rooms.getWidth() <= xin + 30)
            && (rooms.getY() + rooms.getHeight() >= yin - 30
                && rooms.getY() + rooms.getHeight() <= yin + 30))
        {
          roomIterator.remove();
        }

      }

      g2d.draw(rooms);
    }

  }

  @Override
  public void mouseDragged(MouseEvent e)
  {
    // gets the points for the length of the line
    xdrag = (int) e.getPoint().getX();
    ydrag = (int) e.getPoint().getY();
    repaint();
  }

  @Override
  public void mousePressed(MouseEvent e)
  {
    // gets the points for the beginning of the line
    inPoint = e.getPoint();
    endPoint = null;
    xin = (int) e.getPoint().getX();
    yin = (int) e.getPoint().getY();
  }

  @Override
  public void mouseReleased(MouseEvent e)
  {
    // gets the points for the end of the line
    endPoint = e.getPoint();
    xend = (int) e.getPoint().getX();
    yend = (int) e.getPoint().getY();

    repaint();
  }

  @Override
  public void mouseClicked(MouseEvent e)
  {
    // allows the room to be created and drawn with a click of the mouse
    room = true;
  }

  @Override
  public void mouseMoved(MouseEvent e)
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

  /**
   * Return the list of lines.
   * 
   * @return lines.
   */
  public ArrayList<Line2D> getLines()
  {
    return this.myLines;
  }

  /**
   * Return the list of rooms.
   * 
   * @return rooms.
   */
  public ArrayList<Rectangle2D> getRooms()
  {
    return this.myRooms;
  }

}
