package sonia.commons.crypt.util;

/**
 *
 * @author Thorsten Ludewig (t.ludewig@ostfalia.de)
 */
public class HEX
{

  /** Field description */
  public final static char HEXCHARS[] =
  {
    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E',
    'F'
  };

  //~--- methods --------------------------------------------------------------

  public static String convert(byte b)
  {
    return "" + HEXCHARS[(b & 0xff) >> 4] + HEXCHARS[b & 0x0f];
  }

  public static String convert(byte array[])
  {
    String returnValue = "";

    for (int i = 0; i < array.length; i++)
    {
      returnValue += (convert(array[i]));
    }

    return returnValue;
  }
}
