package sonia.commons.crypt.util;

/**
 *
 * @author Thorsten Ludewig (t.ludewig@ostfalia.de)
 */
public class Random extends java.util.Random
{

  private final static Random singleton = new Random();

  //~--- constructors ---------------------------------------------------------
  private Random()
  {
    super(System.currentTimeMillis());
  }

  //~--- get methods ----------------------------------------------------------
  public static String getDigits(int numberOfDigits)
  {
    String str = null;

    if (numberOfDigits > 0)
    {
      str = "";

      for (int i = 0; i < numberOfDigits; i++)
      {
        str += singleton.nextInt(10);
      }
    }

    return str;
  }

  public static Random getRandom()
  {
    return singleton;
  }
}
