package sonia.webapp.qrtravel;
 
import sonia.commons.crypt.cipher.AesSimpleCipher;
 
/**
 *
 * @author Thorsten Ludewig (t.ludewig@ostfalia.de)
 */
public final class Cipher 
{
   
  /** secret key */
  private static final char[] KEY = Config.getInstance().getCipherKey().toCharArray();
  
  /** cipher */
  private static final AesSimpleCipher CIPHER = 
    AesSimpleCipher.builder().build(KEY);

  public static String encrypt(String value)
  {
    return CIPHER.encrypt(value);
  }

  public static String decrypt(String value)
  {
    return CIPHER.decrypt(value);
  }
}