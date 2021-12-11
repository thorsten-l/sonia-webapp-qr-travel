package sonia.commons.crypt.converter;

/**
 *
 * @author Thorsten Ludewig (t.ludewig@ostfalia.de)
 * @since 1.1.0
 */
public interface ByteConverter
{

  public byte[] decode(String data);

  public String encode(byte[] data);
}
