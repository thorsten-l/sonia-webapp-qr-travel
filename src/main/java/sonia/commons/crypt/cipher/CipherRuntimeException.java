package sonia.commons.crypt.cipher;

/**
 *
 * @author Thorsten Ludewig (t.ludewig@ostfalia.de)
 * @since 1.1.0
 */
public class CipherRuntimeException extends RuntimeException
{
  private static final long serialVersionUID = 8090355061429525271L;

  //~--- constructors ---------------------------------------------------------

  public CipherRuntimeException() {}

  public CipherRuntimeException(String message)
  {
    super(message);
  }

  public CipherRuntimeException(Throwable cause)
  {
    super(cause);
  }

  public CipherRuntimeException(String message, Throwable cause)
  {
    super(message, cause);
  }
}
