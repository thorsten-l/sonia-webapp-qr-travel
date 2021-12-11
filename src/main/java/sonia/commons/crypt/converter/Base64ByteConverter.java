package sonia.commons.crypt.converter;

//~--- non-JDK imports --------------------------------------------------------

import sonia.commons.crypt.util.Convert;

/**
 *
 * @author Thorsten Ludewig (t.ludewig@ostfalia.de)
 * @since 1.1.0
 */
public class Base64ByteConverter implements ByteConverter
{

  @Override
  public byte[] decode(String data)
  {
    return Convert.fromBase64(data);
  }

  @Override
  public String encode(byte[] data)
  {
    return Convert.toBase64(data);
  }
}
