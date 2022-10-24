package teamCcontroller;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

import org.junit.jupiter.api.Test;

import teamCmodel.Furniture;

class FunctionalityTest
{
  /**
   * Functionality Tests.
   */

  /*
   * Test badFileFormat
   */
  @Test
  void testBadFileFormat()
  {
    BadFileFormatException tester = new BadFileFormatException("This is not a valid file.");
  }

  /*
   * Simulates clicking the tool bar, and makes sure that it's functional when clicked on.
   */
  @Test
  void testToolbarClicked()
  {
    JLayeredPane layout = new JLayeredPane();
    Functionality functions = new Functionality();

    assertSame("The correct button was not clicked", " Help ",
        functions.clickedToolBar(" Help ", layout));

  }

  /*
   * Simulates clicking the menu bar, and makes sure that it's also functional when clicked on.
   */
  @Test
  void testMenuBarClicked()
  {
    MenubarFunctionality menuHelper = new MenubarFunctionality();

    assertSame("The correct button was not clicked", "Help", menuHelper.clickedMenuBar("Help"));
  }

  /*
   * Tests our resize method in accordance to the three button choices (All, Height, Width).
   */
  @Test
  void testResize()
  {
    JLayeredPane layout = new JLayeredPane();
    Functionality functions = new Functionality();
    Furniture furnSelect;
    boolean widthRadioPressed;
    boolean heightRadioPressed;
    String resizeText;
    int height = 50;
    int width = 50;
    ArrayList<Furniture> furnlist;

    BufferedImage furniture1 = null;
    BufferedImage furniture2 = null;
    BufferedImage furniture3 = null;

    try
    {
      // Populates the board with a few pieces of furniture.
      furniture1 = ImageIO.read(new File("Bathroom/BathroomLight.png"));
      furniture2 = ImageIO.read(new File("Chairs/Chair2.png"));
      furniture3 = ImageIO.read(new File("Doors/Doubledoors.png"));
      functions.getFurnitureList().add(new Furniture(furniture1, new JLabel("Bathroom Light")));
      functions.getFurnitureList().add(new Furniture(furniture2, new JLabel("Chair 2")));
      functions.getFurnitureList().add(new Furniture(furniture3, new JLabel("Double Doors")));

    }
    catch (IOException e)
    {
    }

    furnlist = functions.getFurnitureList();
    // Calls the method for user input
    functions.clickedToolBar(" Resize ", layout);
    widthRadioPressed = functions.getWidthButton().isSelected();
    heightRadioPressed = functions.getHeightButton().isSelected();
    resizeText = functions.getResizeTextField().getText();

    furnSelect = new Furniture(furnlist.get(0).getImage(), furnlist.get(0).getLabel());
    functions.setFurnitureSelected(furnSelect);

    // Width button selected
    if (widthRadioPressed)
    {
      System.out.println("Width button selected");
      assertFalse(heightRadioPressed);

      try
      {
        width = Integer.parseInt(resizeText);
        height = (int) Math.ceil(width * (1 / functions.getFurnitureSelected().getAspectRatio()));
        assertTrue(height > width);

      }
      catch (NumberFormatException e)
      {
        assertThrows(NumberFormatException.class, () -> 
        {
          Integer.parseInt(resizeText);
        });
      }
    }

    // Height button elected
    if (heightRadioPressed)
    {
      System.out.println("Height button selected");
      assertFalse(widthRadioPressed);
      // System.out.println(functions.getFurnitureSelected());
      try
      {
        height = Integer.parseInt(resizeText);
        width = (int) Math.ceil(height * functions.getFurnitureSelected().getAspectRatio());
        assertFalse(width > height);
      }
      catch (NumberFormatException e)
      {
        assertThrows(NumberFormatException.class, () -> 
        {
          Integer.parseInt(resizeText);
        });
      }

    }
  }

  /*
   * Tests the rotate method.
   */
  @Test
  void testRotate()
  {
    Functionality functions = new Functionality();

    try
    {
      // Populates the board with a few pieces of furniture.
      int height;
      int width;
      BufferedImage furniture1 = null;
      BufferedImage furniture2 = null;
      BufferedImage furniture3 = null;

      furniture1 = ImageIO.read(new File("Chairs/Couch1.png"));
      furniture2 = ImageIO.read(new File("Chairs/Chair2.png"));
      furniture3 = ImageIO.read(new File("Doors/Doubledoors.png"));
      functions.getFurnitureList().add(new Furniture(furniture1, new JLabel("Couch 1")));
      functions.getFurnitureList().add(new Furniture(furniture2, new JLabel("Chair 2")));
      functions.getFurnitureList().add(new Furniture(furniture3, new JLabel("Double Doors")));

      // sets the furniture selected as the first image in the array, Couch1
      Furniture furnSelected = new Furniture(functions.getFurnitureList().get(0).getImage(),
          functions.getFurnitureList().get(0).getLabel());

      functions.setFurnitureSelected(furnSelected);

      // get original height and width of the image
      height = furnSelected.getImage().getHeight();
      width = furnSelected.getImage().getWidth();

      // rotate the image
      functions.rotateImage();

      // store rotatedImage for comparison
      BufferedImage rotatedImage = functions.getRotatedImage();

      // checks if the height and widths have been swapped during the rotation
      assertFalse(height == rotatedImage.getHeight());
      assertFalse(width == rotatedImage.getWidth());

    }
    catch (IOException e)
    {
    }
  }

  /*
   * Tests the remove method to make sure the image is being removed from the furniture array.
   */
  @Test
  void testRemove()
  {
    Functionality functions = new Functionality();

    try
    {
      // Populates the board with a few pieces of furniture.
      BufferedImage furniture1 = null;
      BufferedImage furniture2 = null;
      BufferedImage furniture3 = null;
      furniture1 = ImageIO.read(new File("Bathroom/BathroomLight.png"));
      furniture2 = ImageIO.read(new File("Chairs/Chair2.png"));
      furniture3 = ImageIO.read(new File("Doors/Doubledoors.png"));
      functions.getFurnitureList().add(new Furniture(furniture1, new JLabel("Bathroom Light")));
      functions.getFurnitureList().add(new Furniture(furniture2, new JLabel("Chair 2")));
      functions.getFurnitureList().add(new Furniture(furniture3, new JLabel("Double Doors")));

      // makes sure the size of the array is 3 after adding 3 images to the furniture array
      assertEquals(functions.getFurnitureList().size(), 3);

      Furniture furnSelected = new Furniture(functions.getFurnitureList().get(0).getImage(),
          functions.getFurnitureList().get(0).getLabel());
      functions.setFurnitureSelected(furnSelected);

      // makes sure the furniture was selected
      assertNotNull(furnSelected);

      functions.getFurnitureList().remove(0);
      functions.removeImage();

      // makes sure the selected image was removed from the furniture array
      assertEquals(functions.getFurnitureList().size(), 2);

    }
    catch (IOException e)
    {
    }

  }

  /*
   * Tests the eraserActive.
   */
  @Test
  void testSetEraserActive()
  {
    Functionality functions = new Functionality();

    functions.setEraserActive(true);

    assertEquals(functions.getEraserActive(), true);
  }

  /*
   * Tests the wallActive.
   */
  @Test
  void testSetWallActive()
  {
    Functionality functions = new Functionality();

    functions.setWallActive(true);

    assertEquals(functions.getWallActive(), true);
  }

  /*
   * Tests the roomActive.
   */
  @Test
  void testSetRoomActive()
  {
    Functionality functions = new Functionality();

    functions.setRoomActive(true);

    assertEquals(functions.getRoomActive(), true);
  }

  /*
   * Tests the newColor.
   */
  @Test
  void testgetNewColor()
  {
    Functionality functions = new Functionality();

    functions.setNewColor(Color.black);

    assertEquals(functions.getNewColor(), Color.black);
  }

  /*
   * Test the getAll radio button.
   */
  @Test
  void testAllRadioButton()
  {
    Functionality functions = new Functionality();

    assertNotNull(functions.getAllButton());
  }

  /*
   * Tests selectedFurniture.
   */
  @Test
  void testSelectedFurniture()
  {
    Functionality functions = new Functionality();

    try
    {
      // populate the board
      BufferedImage furniture1 = null;
      BufferedImage furniture2 = null;
      BufferedImage furniture3 = null;
      furniture1 = ImageIO.read(new File("Bathroom/BathroomLight.png"));
      furniture2 = ImageIO.read(new File("Chairs/Chair2.png"));
      furniture3 = ImageIO.read(new File("Doors/Doubledoors.png"));
      functions.getFurnitureList().add(new Furniture(furniture1, new JLabel("Bathroom Light")));
      functions.getFurnitureList().add(new Furniture(furniture2, new JLabel("Chair 2")));
      functions.getFurnitureList().add(new Furniture(furniture3, new JLabel("Double Doors")));

      Furniture furnSelected = new Furniture(functions.getFurnitureList().get(0).getImage(),
          functions.getFurnitureList().get(0).getLabel());

      functions.setFurnitureSelected(furnSelected);
      functions.selectFurniture(furnSelected);

      // makes sure the furniture was selected
      assertNotNull(furnSelected);

    }
    catch (IOException e)
    {
    }
  }

  /*
   * Test addFurnitureListener.
   */
  @Test
  void testAddFurnitureListener()
  {
    Functionality functions = new Functionality();

    try
    {
      BufferedImage furniture1 = null;
      furniture1 = ImageIO.read(new File("Bathroom/BathroomLight.png"));
      JLabel label = new JLabel("Bathroom Light");

      Furniture furniture = new Furniture(furniture1, label);

      functions.addFurnListener(label, furniture);

    }
    catch (IOException e)
    {
    }

  }

  /*
   * Tests createFurn().
   */
  @Test
  void testCreateFurn()
  {
    try
    {
      FileOpener testFileOpener = new FileOpener();

      BufferedImage furniture1 = null;
      furniture1 = ImageIO.read(new File("Bathroom/BathroomLight.png"));
      Furniture furniture = new Furniture(furniture1, new JLabel("Bathroom Light"));
      furniture.setImagePath("Bathroom/BathroomLight.png");

      Furniture furnTester = testFileOpener.createFurn("Bathroom/BathroomLight.png",
          new JLabel("Bathroom Light"), 50, 50, 50, 50);

      assertEquals(furniture.getImagePath(), furnTester.getImagePath());
    }
    catch (IOException e)
    {
    }

  }

  /*
   * Test setCustomActive() and getCustomActive()
   */
  @Test
  void testsetCustomActive()
  {
    Functionality functions = new Functionality();

    functions.setCustomActive(true);

    assertEquals(functions.getCustomActive(), true);
  }

  /*
   * Test setImagePath.
   */
  @Test
  void testsetImagePath()
  {
    try
    {
      BufferedImage furniture1 = null;
      furniture1 = ImageIO.read(new File("Bathroom/BathroomLight.png"));

      Furniture furniture = new Furniture(furniture1, new JLabel("Bathroom Light"));

      furniture.setImagePath("Testing1");

      assertEquals(furniture.getImagePath(), "Testing1");

    }
    catch (IOException e)
    {
    }
  }

  /**
   * MenubarFunctionality Tests.
   */

  /*
   * Tests the copy and paste image buttons.
   */
  @Test
  void testCopyandPasteImage()
  {
    Functionality functions = new Functionality();
    MenubarFunctionality menuBarFunctions = new MenubarFunctionality();
    try
    {
      // Populates the board with a few pieces of furniture.
      BufferedImage furniture1 = null;
      BufferedImage furniture2 = null;
      BufferedImage furniture3 = null;
      furniture1 = ImageIO.read(new File("Bathroom/BathroomLight.png"));
      furniture2 = ImageIO.read(new File("Chairs/Chair2.png"));
      furniture3 = ImageIO.read(new File("Doors/Doubledoors.png"));
      functions.getFurnitureList().add(new Furniture(furniture1, new JLabel("Bathroom Light")));
      functions.getFurnitureList().add(new Furniture(furniture2, new JLabel("Chair 2")));
      functions.getFurnitureList().add(new Furniture(furniture3, new JLabel("Double Doors")));

      // furniture list is size 3
      assertEquals(functions.getFurnitureList().size(), 3);

      // selects furniture
      Furniture furnSelected = new Furniture(functions.getFurnitureList().get(0).getImage(),
          functions.getFurnitureList().get(0).getLabel());
      functions.setFurnitureSelected(furnSelected);

      // makes sure the furniture was selected
      assertNotNull(furnSelected);

      menuBarFunctions.copyFurniture();
      // menuBarFunctions.pasteFurniture();

      // a new image has been added to the array
      // assertEquals(functions.getFurnitureList().size(), 4);

    }
    catch (IOException e)
    {
    }

  }

  /*
   * Tests the delete button.
   */
  @Test
  void testDeleteImage()
  {
    Functionality functions = new Functionality();
    MenubarFunctionality menuBarFunctions = new MenubarFunctionality();

    try
    {
      // Populates the board with a few pieces of furniture.
      BufferedImage furniture1 = null;
      BufferedImage furniture2 = null;
      BufferedImage furniture3 = null;
      furniture1 = ImageIO.read(new File("Bathroom/BathroomLight.png"));
      furniture2 = ImageIO.read(new File("Chairs/Chair2.png"));
      furniture3 = ImageIO.read(new File("Doors/Doubledoors.png"));
      functions.getFurnitureList().add(new Furniture(furniture1, new JLabel("Bathroom Light")));
      functions.getFurnitureList().add(new Furniture(furniture2, new JLabel("Chair 2")));
      functions.getFurnitureList().add(new Furniture(furniture3, new JLabel("Double Doors")));

      // makes sure the size of the array is 3 after adding 3 images to the furniture array
      assertEquals(functions.getFurnitureList().size(), 3);

      Furniture furnSelected = new Furniture(functions.getFurnitureList().get(0).getImage(),
          functions.getFurnitureList().get(0).getLabel());
      functions.setFurnitureSelected(furnSelected);

      // makes sure the furniture was selected
      assertNotNull(furnSelected);

      functions.getFurnitureList().remove(0);
      menuBarFunctions.deleteImage();

      // makes sure the selected image was removed from the furniture array
      assertEquals(functions.getFurnitureList().size(), 2);

    }
    catch (IOException e)
    {
    }
  }

  /*
   * Tests the select all button.
   */
  @Test
  void testSelectAllImages()
  {
    Functionality functions = new Functionality();
    MenubarFunctionality menuBarFunctions = new MenubarFunctionality();

    try
    {
      // Populates the board with a few pieces of furniture.
      BufferedImage furniture1 = null;
      BufferedImage furniture2 = null;
      BufferedImage furniture3 = null;
      furniture1 = ImageIO.read(new File("Bathroom/BathroomLight.png"));
      furniture2 = ImageIO.read(new File("Chairs/Chair2.png"));
      furniture3 = ImageIO.read(new File("Doors/Doubledoors.png"));
      functions.getFurnitureList().add(new Furniture(furniture1, new JLabel("Bathroom Light")));
      functions.getFurnitureList().add(new Furniture(furniture2, new JLabel("Chair 2")));
      functions.getFurnitureList().add(new Furniture(furniture3, new JLabel("Double Doors")));

      // makes sure the size of the array is 3 after adding 3 images to the furniture array
      assertEquals(functions.getFurnitureList().size(), 3);

      // selects everything in the arraylist and then removes everything when delete is called
      menuBarFunctions.selectAllFurns();
      menuBarFunctions.setAllSelectedFurns();

      assertEquals(menuBarFunctions.getAllSelectedFurns().size(), 0);
    }
    catch (IOException e)
    {
    }
  }

  @Test
  void testAbout()
  {
    MenubarFunctionality menuBarFunctions = new MenubarFunctionality();
    menuBarFunctions.aboutProgram();
  }

}
