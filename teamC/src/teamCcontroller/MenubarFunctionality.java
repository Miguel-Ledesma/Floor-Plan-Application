package teamCcontroller;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import teamCmodel.Furniture;
import teamCview.EZDesignerGUI;
import teamCview.Movement;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * MenubarFunctionality.
 * 
 * @author Team C
 * @version 11/09/2020
 *
 */
public class MenubarFunctionality extends Functionality
{
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private static ArrayList<Furniture> selectedFurns = new ArrayList<Furniture>();
  private static boolean selectAllFlag = false;
  private int copiedHeight;
  private int copiedWidth;
  private String copiedImagePath;
  private BufferedImage copiedImage;
  private ArrayList<Line2D> myLines;
  private ArrayList<Rectangle2D> myRooms;
  private ArrayList<Furniture> myFurns;
  private Movement move;

  /**
   * Menu bar Constructor.
   */
  public MenubarFunctionality()
  {
    move = new Movement();
  }

  /**
   * Menu Bar listeners.
   *
   * @param button
   *          the button to be pressed in the menu bar part of the GUI.
   * @return status of the button being pressed.
   */
  public String clickedMenuBar(String button)
  {
    switch (button)
    {
      case "New":
        newFile();
        break;
      case "Open":
        newFile();
        super.openFile();
        break;
      case "Save":
        new FileSaver();
        break;
      case "Print":
        printFile();
        break;
      case "Exit":
        System.exit(0);
        break;
      case "Copy":
        copyFurniture();
        break;
      case "Paste":
        pasteFurniture();
        break;
      case "Delete":
        if (getSelectAllFlag())
        {
          for (Furniture furns : MenubarFunctionality.getAllSelectedFurns())
          {
            setFurnitureSelected(furns);
            deleteImage();
          }
          setSelectAll(false);
          setAllSelectedFurns();
        }
        else
        {
          deleteImage();
        }
        break;
      case "Select All":
        selectAllFurns();
        break;
      case "About":
        aboutProgram();
        break;
      case "Tutorial":
        tutorial();
        break;
      default:
        System.out.println("Button clicked: " + button);
    }
    return button;
  }

  /**
   * Opens a blank new grid.
   */
  public void newFile()
  {
    // promt asking the user to save
    JPanel panel = new JPanel();
    panel.add(new JLabel("Would you like to save your work?"));
    int option = JOptionPane.showConfirmDialog(null, panel, "Save",
        JOptionPane.YES_NO_CANCEL_OPTION);

    while (option != JOptionPane.CANCEL_OPTION)
    {
      if (option == JOptionPane.YES_OPTION)
      {
        new FileSaver();
      }
      // delete furnList
      myLines = EZDesignerGUI.getGUIWallList();
      Iterator<Line2D> wallIterator = myLines.iterator();

      while (wallIterator.hasNext())
      {
        wallIterator.next();
        wallIterator.remove();
      }

      myRooms = EZDesignerGUI.getGUIRoomList();
      Iterator<Rectangle2D> roomIterator = myRooms.iterator();

      while (roomIterator.hasNext())
      {
        roomIterator.next();
        roomIterator.remove();
      }

      myFurns = EZDesignerGUI.getGUIFurnList();
      Iterator<Furniture> furnsIterator = myFurns.iterator();

      while (furnsIterator.hasNext())
      {
        Furniture furn = furnsIterator.next();
        furn.getLabel().setVisible(false);
        furnsIterator.remove();
      }

      break;
    }

  }

  /**
   * Saves the currently opened file.
   * 
   * @param filePath
   *          - String that identifies where the file needs will be saved
   */

  public void saveFile(String filePath) throws IOException
  {
    // <wall, room, furniture>, width, height, x, y, [imagePath]
    FileWriter file = new FileWriter(filePath);
    int index = 0;
    int roomCount = 1;
    int wallCount = 1;

    // Furniture
    ArrayList<Furniture> furnitureList = EZDesignerGUI.getGUIFurnList();
    for (Furniture furniture : furnitureList)
    {
      file.write("furniture,");
      file.write(furniture.getWidth() + ",");
      file.write(furniture.getHeight() + ",");
      file.write(furniture.getLabel().getX() + ",");
      file.write(furniture.getLabel().getY() + ",");
      file.write(furniture.getImagePath() + "\n");
    }

    // Rooms
    ArrayList<Rectangle2D> rooms = EZDesignerGUI.getGUIRoomList();
    for (Rectangle2D room : rooms)
    {
      if (index % 2 == 0)
      {
        file.write("Room " + roomCount + ": outer square,");
      }
      else
      {
        file.write("Room " + roomCount + ": inner square,");
        roomCount++;
      }
      file.write(room.getWidth() + ",");
      file.write(room.getHeight() + ",");
      file.write(room.getX() + ",");
      file.write(room.getY() + ",");
      file.write("\n");
      index++;
    }

    // Walls
    ArrayList<Line2D> walls = EZDesignerGUI.getGUIWallList();
    index = 0;
    for (Line2D wall : walls)
    {
      if (index % 2 == 0)
      {
        file.write("Wall " + wallCount + ": outer line,");
      }
      else
      {
        file.write("Wall " + wallCount + ": inner line,");
        wallCount++;
      }

      file.write(wall.getX1() + ",");
      file.write(wall.getY1() + ",");
      file.write(wall.getX2() + ",");
      file.write(wall.getY2() + ",");
      file.write("\n");
      index++;
    }

    file.close();
  }

  /**
   * Prints the currently opened file.
   */
  public void printFile()
  {
    PrinterJob print = PrinterJob.getPrinterJob();
    print.setJobName("EZDesignerPrint");
    print.setPrintable(new Printable()
    {
      @Override
      public int print(Graphics graphics, PageFormat format, int pageNum) throws PrinterException
      {
        if (pageNum > 0)
        {
          return Printable.NO_SUCH_PAGE;
        }

        Graphics2D drawings = (Graphics2D) graphics;

        drawings.translate(format.getImageableX() * 2, format.getImageableY() * 2);

        drawings.scale(0.5, 0.5);

        EZDesignerGUI.getRoomLayout().paint(drawings);

        return Printable.PAGE_EXISTS;

      }
    });
    if (print.printDialog())
    {
      try
      {
        print.print();
      }
      catch (PrinterException e)
      {
        System.out.println("Print failure!!!");
      }
    }
  }

  /**
   * Copy furniture within the room with all its modifications.
   */
  public void copyFurniture()
  {
    Furniture copiedFurniture = getFurnitureSelected();
    copiedHeight = copiedFurniture.getHeight();
    copiedWidth = copiedFurniture.getWidth();
    copiedImage = copiedFurniture.getImage();
    copiedImagePath = copiedFurniture.getImagePath();
  }

  /**
   * Paste the furniture currently in the clip board with all its modifications.
   */
  public void pasteFurniture()
  {
    JLabel label = new JLabel();

    Furniture newFurniture = createFurn(copiedImagePath, label, copiedWidth, copiedHeight, 0, 0);
    newFurniture.addToLayout();
  }

  /**
   * Delete a piece of furniture from the room.
   */
  public void deleteImage()
  {
    if (super.getFurnitureSelected() != null)
    {
      super.getFurnitureSelected().getLabel().setVisible(false);
      super.getFurnitureList().remove(super.getFurnitureSelected());
    }
  }

  /**
   * Select all the furniture in the current room.
   */
  public void selectAllImages()
  {
    ArrayList<Furniture> mySelectedFurns = EZDesignerGUI.getGUIFurnList();
    Iterator<Furniture> selectedFurnsIterator = mySelectedFurns.iterator();
    selectedFurns = new ArrayList<Furniture>();

    while (selectedFurnsIterator.hasNext())
    {
      Furniture furn = selectedFurnsIterator.next();
      furn.getLabel().setBorder(BorderFactory.createDashedBorder(Color.RED));
      selectedFurns.add(furn);

    }
  }

  /**
   * Select all furniture in the current room helper.
   */
  public void selectAllFurns()
  {
    ArrayList<Furniture> mySelectedFurns = EZDesignerGUI.getGUIFurnList();
    Iterator<Furniture> selectedFurnsIterator = mySelectedFurns.iterator();

    while (selectedFurnsIterator.hasNext())
    {
      Furniture furn = selectedFurnsIterator.next();
      furn.getLabel().setBorder(BorderFactory.createDashedBorder(Color.RED));
      selectedFurns.add(furn);
    }

    setSelectAll(true);
  }

  /**
   * Returns the selected furniture ArrayList.
   * 
   * @return ArrayList ArrayList of the selected furniture objects
   */
  public static ArrayList<Furniture> getAllSelectedFurns()
  {
    return selectedFurns;

  }

  /**
   * Clears the selectedFurn ArrayList.
   */
  public static void setAllSelectedFurns()
  {
    selectedFurns = new ArrayList<Furniture>();
  }

  /**
   * Returns the flag indicating if the select all button has been selected.
   * 
   * @return boolean true if select all has been selected, false otherwise
   */
  public static boolean getSelectAllFlag()
  {
    return selectAllFlag;
  }

  /**
   * Sets the select all flag to the parameter value.
   * 
   * @param bool
   *          - boolean
   */
  public static void setSelectAll(boolean bool)
  {
    selectAllFlag = bool;
  }

  /**
   * Opens a JFrame window describing the program in general.
   * 
   * @see wordtohtml.com for text to HTML conversion
   */
  public void aboutProgram()
  {
    double width = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    double height = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    ImageIcon icon = new ImageIcon("EZLogo.png");
    JFrame frame = new JFrame();

    // had to use HTML code because JLabel doesn't recognize newline characters.
    JLabel label = new JLabel(
        "<html><p dir=\"ltr\" style=\"line-height:1.38;margin-top:0pt;margin-bottom:0pt;\"><span style=\"font-size: 11pt; font-family: Arial; color: rgb(239, 239, 239); background-color: transparent; font-weight: 700; font-style: normal; font-variant: normal; text-decoration: none; vertical-align: baseline; white-space: pre-wrap;\"><strong>EZDesigner About:</strong></span></p>\r\n"
            + "<p><span style=\"color: rgb(209, 213, 216);\"><br></span></p>\r\n"
            + "<p dir=\"ltr\" style=\"line-height:1.38;margin-top:0pt;margin-bottom:0pt;\"><span style=\"color: rgb(239, 239, 239);\"><span style=\"font-size: 11pt; font-family: Arial; background-color: transparent; font-weight: 400; font-style: normal; font-variant: normal; text-decoration: none; vertical-align: baseline; white-space: pre-wrap;\">Date: 12/3/2020</span></span></p>\r\n"
            + "<p dir=\"ltr\" style=\"line-height:1.38;margin-top:0pt;margin-bottom:0pt;\"><span style=\"color: rgb(41, 105, 176);\"><span style=\"font-size: 11pt; font-family: Arial; background-color: transparent; font-weight: 400; font-style: normal; font-variant: normal; text-decoration: none; vertical-align: baseline; white-space: pre-wrap;\"><strong>The developers of EZDesigner:</strong></span></span></p>\r\n"
            + "<ul style=\"margin-top:0;margin-bottom:0;padding-inline-start:48px;\">\r\n"
            + "    <li dir=\"ltr\" style=\"list-style-type:disc;font-size:11pt;font-family:Arial;color:#000000;background-color:transparent;font-weight:400;font-style:normal;font-variant:normal;text-decoration:none;vertical-align:baseline;white-space:pre;\">\r\n"
            + "        <p dir=\"ltr\" style=\"line-height:1.38;margin-top:0pt;margin-bottom:0pt;\"><span style=\"color: rgb(239, 239, 239);\"><span style=\"font-size: 11pt; font-family: Arial; background-color: transparent; font-weight: 400; font-style: normal; font-variant: normal; text-decoration: none; vertical-align: baseline; white-space: pre-wrap;\">Hailey Bodycoat</span></span></p>\r\n"
            + "    </li>\r\n"
            + "    <li dir=\"ltr\" style=\"list-style-type:disc;font-size:11pt;font-family:Arial;color:#000000;background-color:transparent;font-weight:400;font-style:normal;font-variant:normal;text-decoration:none;vertical-align:baseline;white-space:pre;\">\r\n"
            + "        <p dir=\"ltr\" style=\"line-height:1.38;margin-top:0pt;margin-bottom:0pt;\"><span style=\"color: rgb(239, 239, 239);\"><span style=\"font-size: 11pt; font-family: Arial; background-color: transparent; font-weight: 400; font-style: normal; font-variant: normal; text-decoration: none; vertical-align: baseline; white-space: pre-wrap;\">Quade Curry</span></span></p>\r\n"
            + "    </li>\r\n"
            + "    <li dir=\"ltr\" style=\"list-style-type:disc;font-size:11pt;font-family:Arial;color:#000000;background-color:transparent;font-weight:400;font-style:normal;font-variant:normal;text-decoration:none;vertical-align:baseline;white-space:pre;\">\r\n"
            + "        <p dir=\"ltr\" style=\"line-height:1.38;margin-top:0pt;margin-bottom:0pt;\"><span style=\"color: rgb(239, 239, 239);\"><span style=\"font-size: 11pt; font-family: Arial; background-color: transparent; font-weight: 400; font-style: normal; font-variant: normal; text-decoration: none; vertical-align: baseline; white-space: pre-wrap;\">Daniel Hassler</span></span></p>\r\n"
            + "    </li>\r\n"
            + "    <li dir=\"ltr\" style=\"list-style-type:disc;font-size:11pt;font-family:Arial;color:#000000;background-color:transparent;font-weight:400;font-style:normal;font-variant:normal;text-decoration:none;vertical-align:baseline;white-space:pre;\">\r\n"
            + "        <p dir=\"ltr\" style=\"line-height:1.38;margin-top:0pt;margin-bottom:0pt;\"><span style=\"color: rgb(239, 239, 239);\"><span style=\"font-size: 11pt; font-family: Arial; background-color: transparent; font-weight: 400; font-style: normal; font-variant: normal; text-decoration: none; vertical-align: baseline; white-space: pre-wrap;\">Miguel Ledesma</span></span></p>\r\n"
            + "    </li>\r\n"
            + "    <li dir=\"ltr\" style=\"list-style-type:disc;font-size:11pt;font-family:Arial;color:#000000;background-color:transparent;font-weight:400;font-style:normal;font-variant:normal;text-decoration:none;vertical-align:baseline;white-space:pre;\">\r\n"
            + "        <p dir=\"ltr\" style=\"line-height:1.38;margin-top:0pt;margin-bottom:0pt;\"><span style=\"color: rgb(239, 239, 239);\"><span style=\"font-size: 11pt; font-family: Arial; background-color: transparent; font-weight: 400; font-style: normal; font-variant: normal; text-decoration: none; vertical-align: baseline; white-space: pre-wrap;\">Pana Muhamad</span></span></p>\r\n"
            + "    </li>\r\n" + "</ul>\r\n"
            + "<p><span style=\"color: rgb(209, 213, 216);\"><br></span></p>\r\n"
            + "<p dir=\"ltr\" style=\"line-height:1.38;margin-top:0pt;margin-bottom:0pt;\"><span style=\"color: rgb(41, 105, 176);\"><span style=\"font-size: 11pt; font-family: Arial; background-color: transparent; font-weight: 700; font-style: normal; font-variant: normal; text-decoration: none; vertical-align: baseline; white-space: pre-wrap;\">Overview:</span></span></p>\r\n"
            + "<p dir=\"ltr\" style=\"line-height:1.38;margin-top:0pt;margin-bottom:0pt;\"><span style=\"color: rgb(239, 239, 239);\"><span style=\"font-size: 11pt; font-family: Arial; background-color: transparent; font-weight: 400; font-style: normal; font-variant: normal; text-decoration: none; vertical-align: baseline; white-space: pre-wrap;\">EZDeisgner was created during the Fall 2020 semester in our CS 345: Software Engineering course at James Madison University. The goal of this program was to give the consumer more options for creating floor plans. Whether you are an interior designer, a homeowner, or just like to create floor plans for fun, EZDesigner provides flexibility that some other floor plan programs can not.&nbsp;</span></span></p>\r\n"
            + "<p><span style=\"color: rgb(209, 213, 216);\"><br></span></p>\r\n"
            + "<p dir=\"ltr\" style=\"line-height:1.38;margin-top:0pt;margin-bottom:0pt;\"><span style=\"color: rgb(41, 105, 176);\"><span style=\"font-size: 11pt; font-family: Arial; background-color: transparent; font-weight: 700; font-style: normal; font-variant: normal; text-decoration: none; vertical-align: baseline; white-space: pre-wrap;\">The Design Process:</span></span></p>\r\n"
            + "<p dir=\"ltr\" style=\"line-height:1.38;margin-top:0pt;margin-bottom:0pt;\"><span style=\"color: rgb(239, 239, 239);\"><span style=\"font-size: 11pt; font-family: Arial; background-color: transparent; font-weight: 400; font-style: normal; font-variant: normal; text-decoration: none; vertical-align: baseline; white-space: pre-wrap;\">We completed EZDesigner using the Java programming language and an agile development process called Scrum. Our team worked in &ldquo;sprints&rdquo;; a sprint in EZDeisgner was about a week long, where every sprint we would have a defined amount of tasks to implement. At the end of each sprint, we would display our progress and a working prototype to our product owner. To conclude our weekly sprint, we would have Scrum meetings, where we talked about our sprint&rsquo;s progress and what we need to implement for future sprints. </span></span></p>\r\n"
            + "<p><span style=\"color: rgb(209, 213, 216);\"><br></span></p>\r\n"
            + "<p dir=\"ltr\" style=\"line-height:1.38;margin-top:0pt;margin-bottom:0pt;\"><span style=\"color: rgb(41, 105, 176);\"><span style=\"font-size: 11pt; font-family: Arial; background-color: transparent; font-weight: 700; font-style: normal; font-variant: normal; text-decoration: none; vertical-align: baseline; white-space: pre-wrap;\">Conclusion:</span></span></p>\r\n"
            + "<p dir=\"ltr\" style=\"line-height:1.38;margin-top:0pt;margin-bottom:0pt;\"><span style=\"color: rgb(239, 239, 239);\"><span style=\"font-size: 11pt; font-family: Arial; color: rgb(239, 239, 239); background-color: transparent; font-weight: 400; font-style: normal; font-variant: normal; text-decoration: none; vertical-align: baseline; white-space: pre-wrap;\">We will continue providing updates to our product and making EZDesigner a better program for all. If you would like to provide feedback or report a bug, please feel free to contact us at any time over email at EZDesignerhelp@gmail.com. Thank you for choosing EZDesigner.</span></p></html>");
    frame.setVisible(true);
    frame.setTitle("About");
    frame.getContentPane().setBackground(Color.DARK_GRAY);
    frame.setIconImage(icon.getImage());
    frame.setLocation((int) Math.ceil(width / 2) - 100, (int) Math.ceil(height / 2) - 100);
    frame.setSize(800, 400);
    frame.add(label);

  }

  /**
   * Opens the YouTube link to the tutorial.
   */
  public void tutorial()
  {

    try
    {
      Desktop.getDesktop().browse(new URL("https://youtu.be/y7ENKDa6Kks").toURI());
    }
    catch (IOException e)
    {
    }
    catch (URISyntaxException e)
    {
    }

  }

}
