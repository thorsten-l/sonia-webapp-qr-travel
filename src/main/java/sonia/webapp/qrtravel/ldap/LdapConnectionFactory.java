/*
 * Copyright 2022 Thorsten Ludewig (t.ludewig@gmail.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package sonia.webapp.qrtravel.ldap;

//~--- non-JDK imports --------------------------------------------------------

import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPConnectionOptions;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.util.ssl.SSLUtil;
import com.unboundid.util.ssl.TrustAllTrustManager;

//~--- JDK imports ------------------------------------------------------------

import java.security.GeneralSecurityException;


import javax.net.ssl.SSLSocketFactory;
import sonia.webapp.qrtravel.Config;

/**
 *
 * @author Thorsten Ludewig (t.ludewig@gmail.com)
 */
public final class LdapConnectionFactory
{
  private final static Config CONFIG = Config.getInstance();
 
  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  private LdapConnectionFactory() {}

  //~--- methods --------------------------------------------------------------

  
  //~--- get methods ----------------------------------------------------------
 
  public static LDAPConnection getConnection()
    throws LDAPException
  {

    LDAPConnectionOptions options = new LDAPConnectionOptions();

    options.setAutoReconnect(true);

    LDAPConnection connection;

    if (CONFIG.isLdapHostSSL())
    {
      connection = new LDAPConnection(createSSLSocketFactory(), options,
        CONFIG.getLdapHostName(), CONFIG.getLdapHostPort(),
        CONFIG.getLdapBindDn(),
        CONFIG.getLdapBindPassword());
    }
    else
    {
      connection = new LDAPConnection(options, CONFIG.getLdapHostName(), 
        CONFIG.getLdapHostPort(),
        CONFIG.getLdapBindDn(),
        CONFIG.getLdapBindPassword());
    }

    connection.setConnectionName(CONFIG.getLdapHostName());

    return connection;
  }

  //~--- methods --------------------------------------------------------------

  private static SSLSocketFactory createSSLSocketFactory()
  {

    // trust all ??
    try
    {
      SSLUtil sslUtil = new SSLUtil(new TrustAllTrustManager());

      return sslUtil.createSSLSocketFactory();
    }
    catch (GeneralSecurityException ex)
    {
      throw new LdapConnectionException("could not create ldap connection", ex);
    }
  }
}
