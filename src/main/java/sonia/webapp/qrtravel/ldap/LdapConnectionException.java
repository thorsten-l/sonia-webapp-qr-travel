package sonia.webapp.qrtravel.ldap;

/**
 *
 * @author Thorsten Ludewig (t.ludewig@ostfalia.de)
 */
public class LdapConnectionException extends RuntimeException
{

  /** Field description */
  private static final long serialVersionUID = 1916708941089543928L;

  //~--- constructors ---------------------------------------------------------

  public LdapConnectionException() {}

  public LdapConnectionException(String message)
  {
    super(message);
  }

  public LdapConnectionException(Throwable cause)
  {
    super(cause);
  }

  public LdapConnectionException(String message, Throwable cause)
  {
    super(message, cause);
  }
}
