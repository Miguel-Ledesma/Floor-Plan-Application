package teamCcontroller;

import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import teamCview.EZDesignerGUI;

/**
 * FileSaver - handles the fileName and filePath of the file when user wants to save their layout.
 * 
 * @author Team C (12/03/2020)
 *
 */
public class FileSaver extends JPanel
{
  /**
   * For checkstyle requirements.
   */
  private static final long serialVersionUID = 1L;
  private String filePath;
  private JFileChooser fc;
  private MenubarFunctionality function;

  /**
   * FileSaver Constructor.
   */
  public FileSaver()
  {
    fc = new JFileChooser();
    function = new MenubarFunctionality();

    getFileName();
  }

  private void getFileName()
  {
    fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
    int returnVal = fc.showSaveDialog(this);

    if (returnVal == JFileChooser.APPROVE_OPTION)
    {
      filePath = fc.getSelectedFile().getAbsolutePath();
      try
      {
        function.saveFile(filePath);
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
    else
    {
      JOptionPane.showMessageDialog(EZDesignerGUI.getRoomLayout(),
          "Open command has been cancelled by user.\n");
    }
  }

}
