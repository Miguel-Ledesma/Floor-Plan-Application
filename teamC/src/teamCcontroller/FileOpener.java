package teamCcontroller;

import java.awt.Image;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import teamCmodel.Furniture;
import teamCview.EZDesignerGUI;
import teamCview.Movement;

/**
 * File chooser class; this class deals with some file functions in our program.
 * 
 * @author Team C
 * @version 12/1/2020
 *
 */
public class FileOpener extends JPanel
{
  /**
   * For checkstyle requirements.
   */
  private static final long serialVersionUID = 1L;
  private JFileChooser fc;
  Movement move;

  /**
   * FileOpener Constructor.
   */
  public FileOpener()
  {
    fc = new JFileChooser();
  }

  /**
   * opens the file.
   */
  public void openFile()
  {
    fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
    int returnVal = fc.showOpenDialog(this);

    if (returnVal == JFileChooser.APPROVE_OPTION)
    {
      File file = fc.getSelectedFile();

      if (EZDesignerGUI.getGUIHelper().getCustomActive())
      {
        try
        {
          loadPNG(file);
        }
        catch (BadFileFormatException e)
        {
          e.printStackTrace();
        }

        EZDesignerGUI.getGUIHelper().setCustomActive(false);
      }
      else
      {
        loadFile(file);
      }
    }
    else
    {
      JOptionPane.showMessageDialog(EZDesignerGUI.getRoomLayout(),
          "Open command has been cancelled by user.\n");
    }
  }

  /**
   * Loads a floor plan.
   * 
   */
  private void loadFile(File file)
  {
    try
    {
      Scanner csvScanner = new Scanner(file);
      String[] splitValues = {};
      ArrayList<JLabel> labels = new ArrayList<>();
      int furnIndex = 0;

      while (csvScanner.hasNextLine())
      {
        splitValues = csvScanner.nextLine().split(",");

        // populates the board and ArrayLists.
        if (splitValues[0].contains("furniture"))
        {
          labels.add(new JLabel());
          JLabel label = labels.get(furnIndex);
          move = new Movement();
          int width = Integer.parseInt(splitValues[1]);
          int height = Integer.parseInt(splitValues[2]);
          int xpos = Integer.parseInt(splitValues[3]);
          int ypos = Integer.parseInt(splitValues[4]);
          String path = splitValues[5];

          Furniture newFurniture = createFurn(path, label, width, height, xpos, ypos);
          newFurniture.addToLayout();

        }
        else if (splitValues[0].contains("Wall"))
        {
          double x1 = Double.parseDouble(splitValues[1]);
          double y1 = Double.parseDouble(splitValues[2]);
          double x2 = Double.parseDouble(splitValues[3]);
          double y2 = Double.parseDouble(splitValues[4]);

          EZDesignerGUI.getGUIWallList().add(new Line2D.Double(x1, y1, x2, y2));

        }
        else if (splitValues[0].contains("Room"))
        {
          double width = Double.parseDouble(splitValues[1]);
          double height = Double.parseDouble(splitValues[2]);
          double xpos = Double.parseDouble(splitValues[3]);
          double ypos = Double.parseDouble(splitValues[4]);

          EZDesignerGUI.getGUIRoomList().add(new Rectangle2D.Double(xpos, ypos, width, height));
        }
        else
        {
          throw new FileNotFoundException();
        }

        furnIndex++;

      }
      csvScanner.close();
    }
    catch (FileNotFoundException e)
    {
      e.getMessage();
    }
  }

  /**
   * loads the PNG file onto the layout.
   * 
   * @param file
   *          PNG file to be loaded.
   * @throws BadFileFormatException
   */
  private void loadPNG(File file) throws BadFileFormatException
  {
    move = new Movement();

    if (!file.toString().endsWith(".png"))
    {
      throw new BadFileFormatException("file needs to be a \".png\" file.");
    }

    JLabel label = new JLabel();
    int initialHeight = Furniture.getDefaultHeight();
    int width = 0;

    Furniture newFurniture = createFurn(file.toString(), label, width, initialHeight, 0, 0);
    newFurniture.addToLayout();

  }

  /**
   * creates a furniture object to be placed into the layout.
   * 
   * @param path
   *          image path
   * @param label
   *          label where the image will be placed on
   * @param width
   *          width of the image
   * @param height
   *          height of the image
   * @param xpos
   *          x-position of the label
   * @param ypos
   *          y-position of the label
   * @return furniture object
   */
  public Furniture createFurn(String path, JLabel label, int width, int height, int xpos, int ypos)
  {
    BufferedImage img = null;

    try
    {
      img = ImageIO.read(new File(path));
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }

    Furniture furniture = new Furniture(img, label);
    width = (int) Math.ceil(height * furniture.getAspectRatio());
    furniture.setWidth(width);
    label.setSize(width, height);
    furniture.setImagePath(path);

    Image resizedImg = img.getScaledInstance(label.getWidth(), label.getHeight(),
        Image.SCALE_SMOOTH);
    label.setIcon(new ImageIcon(resizedImg));

    label.setLocation(xpos, ypos);

    return furniture;
  }

}
