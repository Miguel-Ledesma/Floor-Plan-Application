package teamCcontroller;

import java.util.ArrayList;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import teamCmodel.Furniture;
import teamCview.Movement;

/**
 * Helper method for GUI. This class deals with the listeners.
 *
 * @author Team C
 * @version 11/05/2020
 *
 */
public class Functionality extends FileOpener
{
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private static int valueSelected = 255;
  private static boolean wallActive = false;
  private static boolean roomActive = false;
  private static boolean eraserActive = false;
  private static boolean customActive = false;
  private static Color newColor;
  private static Furniture furnitureSelected = null;
  private int shade = 0;
  private int percentage = 100;
  private Movement move;
  private BufferedImage rotateImage;
  private JRadioButton heightRadio = new JRadioButton("Height");
  private JRadioButton widthRadio = new JRadioButton("Width");
  private JRadioButton allRadio = new JRadioButton("Resize All");
  private JTextField resizeTextField = new JTextField(10);
  private ArrayList<Furniture> furnitureList = new ArrayList<Furniture>();

  /**
   * Constructor for the Functionality class.
   */
  public Functionality()
  {
    move = new Movement();
  }
  // all of the getter and setter methods; some for testing only

  /**
   * For testing purposes: resizeImage() all button.
   * 
   * @return resizeImage() all radio
   */
  protected JRadioButton getAllButton()
  {
    return this.allRadio;
  }

  /**
   * For testing purposes: resizeImage() width button.
   * 
   * @return resizeImage() width radio
   */
  protected JRadioButton getWidthButton()
  {
    return this.widthRadio;
  }

  /**
   * For testing purposes: resizeImage() height button.
   * 
   * @return resizeImage() height radio
   */
  protected JRadioButton getHeightButton()
  {
    return this.heightRadio;
  }

  /**
   * For testing purposes: resizeImage() text field.
   * 
   * @return resizeImage() text field
   */
  protected JTextField getResizeTextField()
  {
    return this.resizeTextField;
  }

  /**
   * For testing purposes: sets furnitureSelected to the furniture.
   * 
   * @param furn
   */
  protected void setFurnitureSelected(Furniture furn)
  {
    furnitureSelected = furn;
  }

  /**
   * For testing purposes: sets furnitureSelected to the furniture.
   * 
   * @return furnitureSelected
   */
  protected Furniture getFurnitureSelected()
  {
    return furnitureSelected;
  }

  /**
   * Returns a list of all the furniture items on the board.
   * 
   * @return furnitureList
   */
  public ArrayList<Furniture> getFurnitureList()
  {
    return furnitureList;
  }

  /**
   * Returns wall active.
   * 
   * @return boolean true if wallActive has been clicked; false otherwise
   */
  public boolean getWallActive()
  {
    return wallActive;
  }

  /**
   * Returns room active.
   * 
   * @return boolean true if roomActive has been clicked; false otherwise
   */
  public boolean getRoomActive()
  {
    return roomActive;
  }

  /**
   * Returns eraser active.
   * 
   * @return boolean true if eraserActive has been clicked; false otherwise
   */
  public boolean getEraserActive()
  {
    return eraserActive;
  }

  /**
   * Returns custom active.
   * 
   * @return boolean true if customActive has been set to true; false otherwise.
   */
  public boolean getCustomActive()
  {
    return customActive;
  }

  /**
   * Returns newColor.
   * 
   * @return Color newColor of the grid panels
   */
  public Color getNewColor()
  {
    return newColor;
  }

  /**
   * For testing purposes: sets the new color.
   * 
   * @param color
   */
  public void setNewColor(Color color)
  {
    newColor = color;
  }

  /**
   * Set wall active.
   * 
   * @param activeStatus
   *          true is active; false otherwise
   */
  public void setWallActive(boolean activeStatus)
  {
    wallActive = activeStatus;
  }

  /**
   * Set room active.
   * 
   * @param activeStatus
   *          true is active; false otherwise
   */
  public void setRoomActive(boolean activeStatus)
  {
    roomActive = activeStatus;
  }

  /**
   * Sets custom active.
   * 
   * @param activeStatus
   *          true is active; false otherwise.
   */
  public void setCustomActive(boolean activeStatus)
  {
    customActive = activeStatus;
  }

  /**
   * Set eraser active.
   * 
   * @param activeStatus
   *          true is active; false otherwise
   */
  public void setEraserActive(boolean activeStatus)
  {
    eraserActive = activeStatus;
  }

  /**
   * For testing purposes: returns the rotated image.
   * 
   * @return BufferedImage
   */
  public BufferedImage getRotatedImage()
  {
    return rotateImage;
  }

  /*
   * public JLayeredPane getRoomLayout() { return roomLayout; }
   */

  /**
   * Tool Bar listeners.
   *
   * @param button
   *          the button to be pressed in the task bar part of the GUI.
   * @param layout
   *          the layout that the button clicked is contained in
   * @return status of the button being pressed.
   */
  public String clickedToolBar(String button, JLayeredPane layout)
  {
    switch (button)
    {
      case (" Remove "):
        if (MenubarFunctionality.getSelectAllFlag())
        {
          for (Furniture furns : MenubarFunctionality.getAllSelectedFurns())
          {
            furnitureSelected = furns;
            removeImage();
          }
          MenubarFunctionality.setAllSelectedFurns();
          MenubarFunctionality.setSelectAll(false);
        }
        else
        {
          removeImage();
        }
        break;
      case (" Transparency "):
        transparency();
        break;
      case (" Resize "):
        if (MenubarFunctionality.getSelectAllFlag())
        {
          for (Furniture furns : MenubarFunctionality.getAllSelectedFurns())
          {
            furnitureSelected = furns;
          }
        }
        resizeImage();
        break;
      case (" Rotate "):
        if (MenubarFunctionality.getSelectAllFlag())
        {
          for (Furniture furns : MenubarFunctionality.getAllSelectedFurns())
          {
            furnitureSelected = furns;
            rotateImage();
          }

          for (int i = 0; i < MenubarFunctionality.getAllSelectedFurns().size(); i++)
          {
            furnitureSelected = MenubarFunctionality.getAllSelectedFurns().get(i);
            if (i != MenubarFunctionality.getAllSelectedFurns().size() - 1)
            {
              furnitureSelected.getLabel().setBorder(null);
            }
          }

          MenubarFunctionality.setSelectAll(false);
        }
        else
        {
          rotateImage();
        }
        break;
      case (" Add Wall "):
        Functionality.wallActive = !Functionality.wallActive;
        Functionality.roomActive = false;
        Functionality.eraserActive = false;
        break;
      case (" Add Room "):
        Functionality.roomActive = !Functionality.roomActive;
        Functionality.wallActive = false;
        Functionality.eraserActive = false;
        break;
      case (" Eraser "):
        Functionality.eraserActive = !Functionality.eraserActive;
        Functionality.wallActive = false;
        Functionality.roomActive = false;
        break;
      case ("Custom Furnishing"):
        Functionality.customActive = !Functionality.customActive;
        super.openFile();
        break;
      default:
        System.out.println("Button Clicked: " + button);
        break;
    }
    return button;
  }

  /**
   * Add the clicked item from the tree to the layout.
   * 
   * @param layout
   *          layout to add the selected image to.
   * @param clicked
   *          the node in the tree that was clicked.
   */
  public void clickedTreeNode(JLayeredPane layout, String clicked)
  {
    // Do not attempt to add a non-png.

    // roomLayout = layout;

    if (!clicked.endsWith(".png"))
    {
      return;
    }
    Functionality.wallActive = false;
    Functionality.roomActive = false;
    Functionality.eraserActive = false;

    JLabel label = new JLabel(); // Label to hold image.
    int initialHeight = Furniture.getDefaultHeight(); // Initial height of label.
    int width = 0; // Width of label.
    Furniture furniture = super.createFurn(clicked, label, width, initialHeight, 0, 0);
    furniture.addToLayout();
  }

  /**
   * Adds listener to the furniture.
   * 
   * @param label
   * @param furniture
   */
  public void addFurnListener(JLabel label, Furniture furniture)
  {
    label.addMouseListener(new MouseAdapter()
    {
      public void mouseClicked(MouseEvent e)
      {
        selectFurniture(furniture);
      }
    });
  }

  /**
   * Select a piece of furniture.
   * 
   * @param furniture
   *          furniture being selected
   */
  public void selectFurniture(Furniture furniture)
  {
    furniture.getLabel().setBorder(BorderFactory.createDashedBorder(Color.RED));

    if (furnitureSelected == null)
    {
      furnitureSelected = furniture;
      return;
    }
    else if (furnitureSelected == furniture)
    {
      return;
    }

    furnitureSelected.getLabel().setBorder(null);
    furnitureSelected = furniture;
  }

  /**
   * Scales the images from the board properly on the grid.
   */
  private void resizeImage()
  {
    JPanel panel = new JPanel();
    JRadioButton heightButton = this.heightRadio;
    JRadioButton widthButton = this.widthRadio;
    JRadioButton allButton = this.allRadio;

    JTextField textField = this.resizeTextField;
    int height = Furniture.getDefaultHeight();
    int width = Furniture.getDefaultWidth();

    ButtonGroup group = new ButtonGroup(); // groups so only one option can be selected
    group.add(heightButton);
    group.add(widthButton);

    panel.add(allButton);
    panel.add(heightButton);
    panel.add(widthButton);
    panel.add(textField);

    heightRadio.setSelected(true);

    int optpane = JOptionPane.showConfirmDialog(null, panel, "Resize Image",
        JOptionPane.OK_CANCEL_OPTION);

    if (furnitureSelected != null)
    {
      if (heightRadio.isSelected())
      {
        while (optpane != JOptionPane.CANCEL_OPTION)
        {
          try
          {
            height = Integer.parseInt(textField.getText());
            width = (int) Math.ceil(height * furnitureSelected.getAspectRatio());
            furnitureSelected.getLabel().setSize(width, height);
            break;
          }
          catch (NumberFormatException e)
          {
            System.out.println("please input a valid integer");
            JOptionPane.showConfirmDialog(null, panel, "Resize Image",
                JOptionPane.OK_CANCEL_OPTION);
          }
        }
      }
      else
      {
        while (optpane != JOptionPane.CANCEL_OPTION)
        {

          try
          {
            width = Integer.parseInt(textField.getText());
            height = (int) Math.ceil(width * (1 / furnitureSelected.getAspectRatio()));
            furnitureSelected.getLabel().setSize(width, height);
            break;
          }
          catch (NumberFormatException e)
          {
            System.out.println("please input a valid integer");
            JOptionPane.showConfirmDialog(null, panel, "Resize Image",
                JOptionPane.OK_CANCEL_OPTION);
          }
        }
      }

      Image resizedImg = furnitureSelected.getImage().getScaledInstance(
          furnitureSelected.getLabel().getWidth(), furnitureSelected.getLabel().getHeight(),
          Image.SCALE_SMOOTH);
      furnitureSelected.setHeight(height);
      furnitureSelected.setWidth(width);
      furnitureSelected.getLabel().setIcon(new ImageIcon(resizedImg));
    }
    else
    {
      System.out.println("Please select an image");

      while (furnitureSelected == null && optpane != JOptionPane.CANCEL_OPTION)
      {
        optpane = JOptionPane.showConfirmDialog(null, panel, "Resize Image",
            JOptionPane.OK_CANCEL_OPTION);
      }

    }

    if (allRadio.isSelected() && furnitureList.size() > 0)
    {
      Furniture furn;

      for (int i = 0; i < furnitureList.size(); i++)
      {
        furn = furnitureList.get(i);

        if (furn != null)
        {
          if (heightRadio.isSelected()) // integer check is already completed.
          {
            height = Integer.parseInt(textField.getText());
            width = (int) Math.ceil(height * furn.getAspectRatio());
          }
          else
          {
            width = Integer.parseInt(textField.getText());
            height = (int) Math.ceil(width * (1 / furn.getAspectRatio()));
          }

          furn.getLabel().setSize(width, height);

          Image resizedImg = furn.getImage().getScaledInstance(furn.getLabel().getWidth(),
              furn.getLabel().getHeight(), Image.SCALE_SMOOTH);
          furn.getLabel().setIcon(new ImageIcon(resizedImg));
        }
      }
    }
  }

  /**
   * Rotates the images in the grid.
   * 
   */
  // used for reference:
  // https://docs.oracle.com/javase/7/docs/api/java/awt/geom/AffineTransform.html
  public void rotateImage()
  {
    int originalHeight;
    double ratio;
    Image resizedImg;
    BufferedImage rotatedImage;
    AffineTransformOp rotateOp;
    BufferedImage selectedImg = furnitureSelected.getImage();
    AffineTransform at = new AffineTransform();

    double rad = Math.toRadians(90);
    int width = (int) (selectedImg.getWidth() * Math.abs(Math.cos(rad))
        + selectedImg.getHeight() * Math.abs(Math.sin(rad)));
    int height = (int) (selectedImg.getHeight() * Math.abs(Math.cos(rad))
        + selectedImg.getWidth() * Math.abs(Math.sin(rad)));

    rotatedImage = new BufferedImage(width, height, selectedImg.getType());

    at.translate(width / 2, height / 2);
    at.rotate(rad, 0, 0);
    at.translate(-selectedImg.getWidth() / 2, -selectedImg.getHeight() / 2);

    rotateOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
    rotateOp.filter(selectedImg, rotatedImage);
    furnitureSelected.setImage(rotatedImage);

    // for testing purposes
    rotateImage = rotatedImage;

    originalHeight = furnitureSelected.getHeight();
    furnitureSelected.setHeight(furnitureSelected.getWidth());
    furnitureSelected.setWidth(originalHeight);

    furnitureSelected.getLabel().setSize(furnitureSelected.getWidth(),
        furnitureSelected.getHeight());

    resizedImg = furnitureSelected.getImage().getScaledInstance(furnitureSelected.getWidth(),
        furnitureSelected.getHeight(), Image.SCALE_SMOOTH);

    ratio = 1 / furnitureSelected.getAspectRatio();
    furnitureSelected.setAspectRatio(ratio);
    furnitureSelected.getLabel().setIcon(new ImageIcon(resizedImg));
  }

  /**
   * Removes the image.
   */
  public void removeImage()
  {
    if (furnitureSelected != null)
    {
      furnitureSelected.getLabel().setVisible(false);
      furnitureList.remove(furnitureSelected);
    }
  }

  /**
   * sets the transparency of the background in EZDesignerGUI.
   */
  public void transparency()
  {
    Functionality.roomActive = false;
    Functionality.wallActive = false;
    Functionality.eraserActive = false;
    int b = 0;
    int w = 255;

    JFrame frame = new JFrame();
    JPanel panel = new JPanel();
    JLabel text = new JLabel("");
    JSlider slider = new JSlider(b, w);
    slider.setValue(valueSelected);
    percentage = (int) (valueSelected / 2.55);
    text.setText(percentage + "%");
    frame.setTitle("Transparency Slider");
    frame.setVisible(true);
    frame.setSize(200, 200);
    frame.add(panel);
    panel.add(text);
    panel.add(slider);
    frame.pack();
    slider.setPaintLabels(true);

    slider.addChangeListener(new ChangeListener()
    {
      public void stateChanged(ChangeEvent e)
      {
        JSlider slider = (JSlider) e.getSource();
        shade = slider.getValue();
        percentage = (int) (shade / 2.55);
        text.setText(percentage + "%");
        newColor = new Color(0, 0, 0, shade);
        valueSelected = shade;
      }
    });

  }
}
