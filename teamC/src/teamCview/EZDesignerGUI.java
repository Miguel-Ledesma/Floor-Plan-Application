package teamCview;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import teamCcontroller.Functionality;
import teamCcontroller.MenubarFunctionality;
import teamCmodel.Furniture;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * GUI class.
 *
 * @author Team C
 * @version 10/23/2020
 *
 */
public class EZDesignerGUI extends JFrame
{
  /**
   * Default SerialVersionID for Checkstyle.
   */
  private static final long serialVersionUID = 1L;

  private static Functionality helper = new Functionality();
  private static JLayeredPane layout;
  private static Draw grid;
  private JTextField currentEntry = new JTextField(10);
  private MenubarFunctionality menuHelper = new MenubarFunctionality();

  /**
   * Builds the GUI for displaying.
   */
  public EZDesignerGUI()
  {
    setTitle("EZDesigner");
    setLayout(new BorderLayout());

    ImageIcon icon = new ImageIcon("EZLogo.png");
    setIconImage(icon.getImage());

    add(buildMenus(), BorderLayout.NORTH);
    add(buildRoomDisplay(), BorderLayout.CENTER);
    add(buildFurnitureItemsPanel(), BorderLayout.WEST);
    // Keeps everything compact and will close when exiting
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setResizable(true);
    pack();
    setVisible(true);
  }

  /**
   * gets the GUI's room layout.
   * 
   * @return GUI room layout.
   */
  public static JLayeredPane getRoomLayout()
  {
    return layout;
  }

  /**
   * gets the GUI furniture list. -- useful when referencing objects in the MenubarFunctionality
   * class --
   * 
   * @return GUI furniture list.
   */
  public static ArrayList<Furniture> getGUIFurnList()
  {
    return helper.getFurnitureList();
  }

  /**
   * gets the GUI rooms list.
   * 
   * @return GUI rooms list.
   */
  public static ArrayList<Rectangle2D> getGUIRoomList()
  {
    return grid.getRooms();
  }

  /**
   * gets the GUI walls list.
   * 
   * @return GUI walls list.
   */
  public static ArrayList<Line2D> getGUIWallList()
  {
    return grid.getLines();
  }

  /**
   * Gets the GUI's helper.
   * 
   * @return Functionality - returns GUIhelper to access the GUI easily
   */
  public static Functionality getGUIHelper()
  {
    return helper;
  }

  /**
   * Build a panel with the menu bar and tool bar.
   *
   * @return JPanel with menus.
   */
  private JPanel buildMenus()
  {
    JPanel menus = new JPanel();
    menus.setLayout(new BorderLayout());

    menus.setBackground(Color.DARK_GRAY);

    menus.add(buildMenuBar(), BorderLayout.NORTH);
    menus.add(buildToolBar(), BorderLayout.CENTER);
    menus.setBorder(BorderFactory.createLineBorder(Color.BLUE, 1));

    return menus;
  }

  /**
   * Create the menu bar. (Incomplete, buttons need to be replaced with menu bar)
   *
   * @return JPanel with menu bar.
   */
  private JPanel buildMenuBar()
  {
    JPanel panel = new JPanel();
    JMenuBar menuBar = new JMenuBar();
    // Names for each JMenu
    String[] menuNames = {"File", "Edit", "Help"};
    // Items to go in each JMenu
    String[][] menuItems = {{"New", "Open", "Save", "Print", "Exit"},
        {"Copy", "Paste", "Delete", "Select All"}, {"About", "Tutorial"}};

    panel.setLayout(new GridLayout(1, 1));
    menuBar.setBackground(Color.DARK_GRAY);

    // Add menus with their items to menuBar.
    for (int i = 0; i < menuNames.length; i++)
    {
      JMenu menu = new JMenu(menuNames[i]);
      menu.setForeground(Color.WHITE);

      // Add menu items to each menu.
      for (String menuItem : menuItems[i])
      {
        JMenuItem item = new JMenuItem(menuItem);
        item.setBackground(Color.DARK_GRAY);
        item.setForeground(Color.WHITE);
        menu.add(item);

        item.addActionListener(
            e -> currentEntry.setText(menuHelper.clickedMenuBar(e.getActionCommand())));
        item.setActionCommand(menuItem);
      }

      menuBar.add(menu);
      panel.add(menuBar);
    }

    return panel;
  }

  /**
   * Builds the tool bar.
   *
   * @return JPanel with tool bar.
   */
  private JPanel buildToolBar()
  {
    int index = 0;
    HashMap<String, String> toolTip = new HashMap<String, String>();
    JPanel panel = new JPanel();
    JToolBar toolBar = new JToolBar();
    String[] buttonNames = {" Rotate ", " Resize ", " Remove ", " Add Wall ", " Add Room ",
        " Transparency ", " Eraser "};
    String[] toolTipTexts = {"Rotates an image.", "Resizes a selected image.", "Removes an image.",
        "Press and drag mouse to add wall.", "Click mouse to add room.",
        "Changes the background transparency.", "Erases a wall or room."};

    panel.setLayout(new GridLayout(1, buttonNames.length));
    panel.setBackground(Color.DARK_GRAY);

    toolBar.setBackground(Color.DARK_GRAY);
    toolBar.setFloatable(false);

    // Set properties for the buttons.
    for (String name : buttonNames)
    {
      JButton button = new JButton(name);
      toolTip.put(name, toolTipTexts[index]);
      button.setToolTipText(toolTip.get(name));
      index++;

      if (name.equals(" Add Wall "))
      {
        for (int i = 0; i < 4; i++)
        {
          toolBar.addSeparator();
        }
      }

      // Colors
      button.setBackground(Color.DARK_GRAY);
      button.setForeground(Color.WHITE);
      button.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));

      button.addActionListener(
          e -> currentEntry.setText(helper.clickedToolBar(e.getActionCommand(), layout)));
      button.setActionCommand(name);

      toolBar.add(button);
      panel.add(toolBar);
    }

    return panel;
  }

  /**
   * Builds the display area for the room.
   *
   * @return JPanel with room display
   */
  private JLayeredPane buildRoomDisplay()
  {
    layout = new JLayeredPane();

    layout.setPreferredSize(new Dimension(800, 600));
    layout.setBackground(Color.gray);
    layout.setBorder(BorderFactory.createEtchedBorder(Color.BLUE, Color.DARK_GRAY));
    layout.setLayout(null);

    // Creates the grid, but move it to the back of panel to avoid bugs with furniture.
    grid = new Draw(3000, 3000, 100, 100);
    layout.add(grid);
    layout.moveToBack(grid);

    layout.add(buildFurnitureItemsPanel());

    return layout;
  }

  /**
   * Creates a furniture item display for easier implementation to the room.
   *
   * @return furniture panel
   */
  private JPanel buildFurnitureItemsPanel()
  {
    JPanel panel = new JPanel();
    panel.setPreferredSize(new Dimension(250, 500));
    panel.setBackground(Color.gray);
    panel.setBorder(BorderFactory.createEtchedBorder(Color.BLUE, Color.DARK_GRAY));

    panel.add(buildTreeFurnitureItems());
    panel.add(customFurnButton());

    return panel;
  }

  private JTree buildTreeFurnitureItems()
  {
    File chairFile = new File("Chairs");
    File tableFile = new File("Tables");
    File bathroomFile = new File("Bathroom");
    File kitchenFile = new File("Kitchen");
    File bedroomFile = new File("Bedroom");
    File doorFile = new File("Doors");
    File windowFile = new File("Windows");
    File decorFile = new File("Decor");

    File[] chairImages = chairFile.listFiles();
    File[] tableImages = tableFile.listFiles();
    File[] bathroomImages = bathroomFile.listFiles();
    File[] kitchenImages = kitchenFile.listFiles();
    File[] bedroomImages = bedroomFile.listFiles();
    File[] doorImages = doorFile.listFiles();
    File[] windowImages = windowFile.listFiles();
    File[] decorImages = decorFile.listFiles();

    // Creates node for the furnishings tree
    DefaultMutableTreeNode itemsRoot = new DefaultMutableTreeNode("Furnishings");
    DefaultMutableTreeNode doorNode = new DefaultMutableTreeNode("Door");
    DefaultMutableTreeNode windowNode = new DefaultMutableTreeNode("Window");
    DefaultMutableTreeNode seatNode = new DefaultMutableTreeNode("Seating");
    DefaultMutableTreeNode tableNode = new DefaultMutableTreeNode("Tables");
    DefaultMutableTreeNode bathroomNode = new DefaultMutableTreeNode("Bathroom");
    DefaultMutableTreeNode kitchenNode = new DefaultMutableTreeNode("Kitchen");
    DefaultMutableTreeNode bedroomNode = new DefaultMutableTreeNode("Bedroom");
    DefaultMutableTreeNode decorNode = new DefaultMutableTreeNode("Other");

    // Adds images to the node
    for (File doorImage : doorImages)
    {
      doorNode.add(new DefaultMutableTreeNode(doorImage.toString()));
    }

    for (File windowImage : windowImages)
    {
      windowNode.add(new DefaultMutableTreeNode(windowImage.toString()));
    }

    for (File chairImage : chairImages)
    {
      seatNode.add(new DefaultMutableTreeNode(chairImage.toString()));
    }

    for (File tableImage : tableImages)
    {
      tableNode.add(new DefaultMutableTreeNode(tableImage.toString()));
    }

    for (File bathroomImage : bathroomImages)
    {
      bathroomNode.add(new DefaultMutableTreeNode(bathroomImage.toString()));
    }

    for (File kitchenImage : kitchenImages)
    {
      kitchenNode.add(new DefaultMutableTreeNode(kitchenImage.toString()));
    }

    for (File bedroomImage : bedroomImages)
    {
      bedroomNode.add(new DefaultMutableTreeNode(bedroomImage.toString()));
    }

    for (File decorImage : decorImages)
    {
      decorNode.add(new DefaultMutableTreeNode(decorImage.toString()));
    }

    // Adds nodes to the tree
    itemsRoot.add(doorNode);
    itemsRoot.add(windowNode);
    itemsRoot.add(seatNode);
    itemsRoot.add(tableNode);
    itemsRoot.add(bathroomNode);
    itemsRoot.add(kitchenNode);
    itemsRoot.add(bedroomNode);
    itemsRoot.add(decorNode);

    JTree tree = new JTree(itemsRoot);
    tree.setBackground(Color.gray);

    tree.addTreeSelectionListener(new TreeSelectionListener()
    {
      public void valueChanged(TreeSelectionEvent e)
      {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

        if (node == null)
          return;

        helper.clickedTreeNode(layout, node.toString());
      }
    });

    return tree;
  }

  /**
   * button opens a file menu which the user can select a .png file and load it as a furniture
   * object on the layout.
   * 
   * @return custom furnishing button
   */
  private JButton customFurnButton()
  {
    JButton button = new JButton("Custom Furnishing");
    button.setBackground(Color.WHITE);
    button.setForeground(Color.DARK_GRAY);
    button.addActionListener(
        e -> currentEntry.setText(helper.clickedToolBar(e.getActionCommand(), layout)));
    button.setActionCommand("Custom Furnishing");

    return button;
  }

  /**
   * Starts the program.
   *
   * @param args
   *          unused
   */
  public static void main(String[] args)
  {
    SwingUtilities.invokeLater(() -> new EZDesignerGUI());
  }
}
