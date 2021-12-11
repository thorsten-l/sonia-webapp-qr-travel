package sonia.commons.crypt.cipher;

/**
 *
 * @author Thorsten Ludewig (t.ludewig@ostfalia.de)
 * @since 1.1.0
 */
public interface SimpleCipher
{

  public String decrypt(String value);

  public String encrypt(String value);
}
