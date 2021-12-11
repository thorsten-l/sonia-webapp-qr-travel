package sonia.webapp.qrtravel;
 
import sonia.commons.crypt.cipher.AesSimpleCipher;
 
/**
 *
 * @author Thorsten Ludewig (t.ludewig@ostfalia.de)
 */
public final class AdminCipher 
{
   
  /** secret key */
  private char[] cipherKey;
  
  /** cipher */
  private AesSimpleCipher aesChipher;

  private static AdminCipher SINGLETON;
  
  private AdminCipher() {}
  
  public static synchronized AdminCipher getInstance()
  {
    if ( SINGLETON == null ) 
    {
      SINGLETON = new AdminCipher();
      SINGLETON.cipherKey = Config.getInstance().getAdminCipherKey().toCharArray();
      SINGLETON.aesChipher = AesSimpleCipher.builder().build(SINGLETON.cipherKey);
    }
    return SINGLETON;
  }
 
  public String encrypt(String value)
  {
    return aesChipher.encrypt(value);
  }

  public String decrypt(String value)
  {
    return aesChipher.decrypt(value);
  }
}