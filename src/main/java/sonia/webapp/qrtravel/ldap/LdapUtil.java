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

import com.google.common.base.Strings;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.unboundid.ldap.sdk.BindResult;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.ResultCode;
import com.unboundid.ldap.sdk.SearchResult;
import com.unboundid.ldap.sdk.SearchResultEntry;
import com.unboundid.ldap.sdk.SearchScope;
import java.text.MessageFormat;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import org.slf4j.LoggerFactory;
import sonia.webapp.qrtravel.Config;

/**
 *
 * @author Thorsten Ludewig (t.ludewig@gmail.com)
 */
public class LdapUtil
{
  private final static org.slf4j.Logger LOGGER = LoggerFactory.getLogger(
    LdapUtil.class.getName());

  private final static Config CONFIG = Config.getInstance();

  private final static LdapUtil SINGLETON = new LdapUtil();

  private LdapUtil()
  {
    loginAttemptsCache = CacheBuilder.newBuilder().expireAfterWrite(CONFIG.
      getLoginFailedBlockingDuration(), TimeUnit.SECONDS).
      build(new CacheLoader<String, LoginAttempt>()
      {
        @Override
        public LoginAttempt load(String username)
        {
          LOGGER.debug("load login attempt cache for username=" + username);
          return new LoginAttempt(username);
        }
      });

    accountCache = CacheBuilder.newBuilder().expireAfterWrite(300,
      TimeUnit.SECONDS).
      build(new CacheLoader<String, LdapAccount>()
      {
        @Override
        public LdapAccount load(String uid)
        {
          LOGGER.debug("load account cache for uid=" + uid);
          return _searchForUid(uid);
        }
      });
  }

  public static LdapAccount bind(String uid, String password)
  {
    LdapAccount account = null;
    LDAPConnection connection = null;

    try
    {
      connection = LdapConnectionFactory.getConnection();
      SearchResult searchResult = connection.search(CONFIG.getLdapBaseDn(),
        getSearchScope(CONFIG.getLdapSearchScope()), "(uid=" + uid + ")",
        "uid");

      String dn = null;

      if (searchResult.getEntryCount() == 1) // uid is unique
      {
        dn = searchResult.getSearchEntries().get(0).getDN();
      }

      LOGGER.debug("dn=" + dn);

      if (!Strings.isNullOrEmpty(dn))
      {
        try
        {
          BindResult bindResult = connection.bind(dn, password);
          if (bindResult.getResultCode() == ResultCode.SUCCESS)
          {
            LOGGER.debug("dn=" + dn + " bind success");
            account = new LdapAccount(connection.getEntry(dn, "uid", "mail",
              "sn",
              "givenName", "soniaStudentNumber"));
            LOGGER.debug(account.toString());
          }
        }
        catch (LDAPException ex)
        {
          LOGGER.debug("dn=" + dn + " bind failed");
        }
      }
    }
    catch (LDAPException ex)
    {
      LOGGER.error("ldap connection failed", ex);
    }
    finally
    {
      if (connection != null)
      {
        connection.close();
      }
    }

    return account;
  }

  public static LdapAccount adminBind(String uid, String password)
  {
    LdapAccount account = null;
    LDAPConnection connection = null;

    try
    {
      connection = LdapConnectionFactory.getConnection();

      String filter = MessageFormat.format(CONFIG.getAdminLdapSearchFilter(),
        uid);

      LOGGER.debug("Admin ldap filter = {}", filter);

      SearchResult searchResult = connection.search(CONFIG.getLdapBaseDn(),
        getSearchScope(CONFIG.getAdminLdapSearchScope()), filter);

      String dn = null;

      if (searchResult.getEntryCount() == 1) // uid is unique
      {
        dn = searchResult.getSearchEntries().get(0).getDN();
      }

      LOGGER.debug("dn=" + dn);

      if (!Strings.isNullOrEmpty(dn))
      {
        try
        {
          BindResult bindResult = connection.bind(dn, password);
          if (bindResult.getResultCode() == ResultCode.SUCCESS)
          {
            LOGGER.debug("dn=" + dn + " bind success");
            account = new LdapAccount(connection.getEntry(dn, "uid", "mail",
              "sn", "givenName"));
            LOGGER.debug(account.toString());
          }
        }
        catch (LDAPException ex)
        {
          LOGGER.debug("dn=" + dn + " bind failed");
        }
      }
    }
    catch (LDAPException ex)
    {
      LOGGER.error("ldap connection failed", ex);
    }
    finally
    {
      if (connection != null)
      {
        connection.close();
      }
    }

    return account;
  }

  public static LdapAccount searchForMail(String mail)
  {
    LdapAccount account = null;
    LDAPConnection connection = null;

    LOGGER.debug("LDAP search for mail = " + mail);

    try
    {
      connection = LdapConnectionFactory.getConnection();

      MessageFormat searchFormat = new MessageFormat(CONFIG.
        getLdapSearchFilter());

      String searchFilter = searchFormat.format(new Object[]
      {
        mail
      });

      SearchResult searchResult = connection.search(CONFIG.getLdapBaseDn(),
        getSearchScope(CONFIG.getLdapSearchScope()), searchFilter,
        "uid", "mail", "sn", "givenName", "soniaStudentNumber");

      List<SearchResultEntry> searchResultEntry = searchResult.
        getSearchEntries();

      if (searchResultEntry.size() > 0)
      {
        account = new LdapAccount(searchResultEntry.get(0));
        LOGGER.debug(account.toString());
      }
    }
    catch (LDAPException ex)
    {
      LOGGER.error("searchForMail", ex);
    }
    finally
    {
      if (connection != null)
      {
        connection.close();
      }
    }

    return account;
  }

  private static LdapAccount _searchForUid(String uid)
  {
    LdapAccount account = null;
    LDAPConnection connection = null;

    LOGGER.debug("LDAP search for uid = " + uid);

    try
    {
      connection = LdapConnectionFactory.getConnection();

      MessageFormat searchFormat = new MessageFormat("(uid={0})");

      String searchFilter = searchFormat.format(new Object[]
      {
        uid
      });

      SearchResult searchResult = connection.search(CONFIG.getLdapBaseDn(),
        getSearchScope(CONFIG.getLdapSearchScope()), searchFilter,
        "uid", "mail", "sn", "givenName", "l");

      List<SearchResultEntry> searchResultEntry = searchResult.
        getSearchEntries();

      if (searchResultEntry.size() > 0)
      {
        account = new LdapAccount(searchResultEntry.get(0));
        LOGGER.debug(account.toString());
      }
    }
    catch (LDAPException ex)
    {
      LOGGER.error("searchForUid", ex);
    }
    finally
    {
      if (connection != null)
      {
        connection.close();
      }
    }

    return account;
  }

  public static String getLocalityFromUid(String uid)
  {
    return SINGLETON._getLocalityFromUid(uid);
  }

  public static LdapAccount searchForUid(String uid)
  {
    return SINGLETON._searchForUidFromCache(uid);
  }

  private LdapAccount _searchForUidFromCache(String uid)
  {
    LdapAccount account = null;

    if (!Strings.isNullOrEmpty(uid))
    {
      LOGGER.debug(uid);
      try
      {
        account = accountCache.get(uid);
      }
      catch (Exception ex)
      {
        LOGGER.error("Failed to get account from cache ", ex);
      }
    }

    return account;
  }

  private String _getLocalityFromUid(String uid)
  {
    String l = "";

    LdapAccount account = _searchForUidFromCache(uid);
    
    if (account != null && account.getLocality() != null)
    {
      l = account.getLocality();
    }

    return l;
  }

  public static LdapAccount searchForCard(long serialNumber)
  {
    LdapAccount account = null;
    LDAPConnection connection = null;

    LOGGER.debug("LDAP search for card = " + serialNumber);

    if (serialNumber > 0)
    {
      try
      {
        connection = LdapConnectionFactory.getConnection();

        MessageFormat searchFormat = new MessageFormat(
          "(&(objectClass=soniaPerson)(!(soniaIsUnregistered=true))(soniaChipCardNumber={0}))"
        );

        String searchFilter = searchFormat.format(new Object[]
        {
          Long.toString(serialNumber)
        });

        LOGGER.debug(searchFilter);

        SearchResult searchResult = connection.search(CONFIG.getLdapBaseDn(),
          getSearchScope(CONFIG.getLdapSearchScope()), searchFilter,
          "uid", "mail", "sn", "givenName", "soniaStudentNumber", "ou",
          "employeeType", "jpegPhoto", "soniaChipcardBarcode");

        List<SearchResultEntry> searchResultEntry = searchResult.
          getSearchEntries();

        if (searchResultEntry.size() > 0)
        {
          account = new LdapAccount(searchResultEntry.get(0));
          LOGGER.debug(account.toString());
        }
      }
      catch (LDAPException ex)
      {
        LOGGER.error("searchForMail", ex);
      }
      finally
      {
        if (connection != null)
        {
          connection.close();
        }
      }
    }
    return account;
  }

  private static SearchScope getSearchScope(String name)
  {
    SearchScope scope;

    switch (name)
    {
      case "ONE":
        scope = SearchScope.ONE;
        break;

      case "SUB":
        scope = SearchScope.SUB;
        break;

      default:
        scope = SearchScope.BASE;
    }

    return scope;
  }

  private synchronized LoginAttempt _getLoginAttempt(String username)
  {
    LOGGER.debug("_getLoginAttempt");
    LoginAttempt loginAttempt = null;

    if (!Strings.isNullOrEmpty(username))
    {
      try
      {
        loginAttempt = loginAttemptsCache.get(username);
        LOGGER.debug(loginAttempt.toString());
      }
      catch (ExecutionException ex)
      {
        LOGGER.error("Failed to get login attempt from cache ", ex);
      }
    }
    return loginAttempt;
  }

  public static LoginAttempt getLoginAttempt(String username)
  {
    return SINGLETON._getLoginAttempt(username);
  }

  private LoadingCache<String, LoginAttempt> loginAttemptsCache;

  private LoadingCache<String, LdapAccount> accountCache;
}
