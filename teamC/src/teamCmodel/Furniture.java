package teamCmodel;

import java.awt.image.BufferedImage;

import javax.swing.*;

import teamCview.EZDesignerGUI;
import teamCview.Movement;

/**
 * Furniture Class for all types of furniture in the room.
 *
 * @author Team C
 * @version 10/30/20
 *
 */
public class Furniture
{
  private static int DEFAULT_HEIGHT = 50;
  private static int DEFAULT_WIDTH = 50;
  private double aspectRatio;
  private BufferedImage image;
  private JLabel label;
  private int height;
  private int width;
  private String imagePath;
  private Movement move = new Movement();

  /**
   * 2-Param constructor for Furniture.
   * 
   * @param img
   *          image
   * @param label
   *          label
   */
  public Furniture(BufferedImage img, JLabel label)
  {
    this.aspectRatio = ((double) img.getWidth() / img.getHeight());
    this.height = DEFAULT_HEIGHT;
    this.width = DEFAULT_WIDTH;
    this.image = img;
    this.label = label;
  }

  /**
   * Returns the default height.
   * 
   * @return int Default height (50)
   */
  public static int getDefaultHeight()
  {
    return DEFAULT_HEIGHT;
  }

  /**
   * Returns the default width.
   * 
   * @return int Default width (50)
   */
  public static int getDefaultWidth()
  {
    return DEFAULT_WIDTH;
  }

  /**
   * Returns the JLabel.
   * 
   * @return label
   */
  public JLabel getLabel()
  {
    return this.label;
  }

  /**
   * returns the aspect ratio.
   * 
   * @return aspect ratio.
   */
  public double getAspectRatio()
  {
    return this.aspectRatio;
  }

  /**
   * Gets the image.
   *
   * @return image
   */
  public BufferedImage getImage()
  {
    return image;
  }

  /**
   * Gets the height.
   * 
   * @return the height of the furniture.
   */
  public int getHeight()
  {
    return this.height;
  }

  /**
   * Gets the width.
   * 
   * @return the width of the furniture.
   */
  public int getWidth()
  {
    return this.width;
  }

  /**
   * Changes the image of the furniture.
   * 
   * @param b
   *          the image we are changing to
   */
  public void setImage(BufferedImage b)
  {
    this.image = b;
  }

  /**
   * Changes the aspect ratio of the furniture.
   * 
   * @param ratio
   *          the new aspect ratio
   */
  public void setAspectRatio(double ratio)
  {
    this.aspectRatio = ratio;
  }

  /**
   * Changes the height of the furniture.
   * 
   * @param height
   *          the new height
   */
  public void setHeight(int height)
  {
    this.height = height;
  }

  /**
   * Changes the width of the furniture.
   * 
   * @param width
   *          the new width
   */
  public void setWidth(int width)
  {
    this.width = width;
  }

  /**
   * Sets the Image Path name.
   * 
   * @param imagePath
   */
  public void setImagePath(String imagePath)
  {
    this.imagePath = imagePath;
  }

  /**
   * Gets the Image Path name.
   * 
   * @return String
   */
  public String getImagePath()
  {
    return this.imagePath;
  }

  /**
   * Adds a furniture object to the EZDesignerGUI layout.
   * 
   */
  public void addToLayout()
  {
    EZDesignerGUI.getGUIFurnList().add(this);
    EZDesignerGUI.getRoomLayout().add(this.getLabel());
    EZDesignerGUI.getRoomLayout().moveToFront(this.getLabel());
    EZDesignerGUI.getGUIHelper().addFurnListener(this.getLabel(), this);
    move.movement(label);
  }

}
