package teamCcontroller;

/**
 * This exception is thrown if the file isn't of the right format.
 * 
 * @author Team C
 * @version 12/7/2020
 */
public class BadFileFormatException extends Exception
{
  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  /**
   * format exception constructor.
   * 
   * @param message
   *          message
   */
  public BadFileFormatException(String message)
  {
    super(message);
  }
}
