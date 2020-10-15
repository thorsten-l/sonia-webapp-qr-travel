package sonia.webapp.qrtravel.controller.admin;

import com.google.common.base.Strings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sonia.webapp.qrtravel.Config;
import sonia.webapp.qrtravel.QrTravelAdminToken;
import static sonia.webapp.qrtravel.QrTravelAdminToken.QR_TRAVEL_ADMIN_TOKEN;
import static sonia.webapp.qrtravel.QrTravelAdminToken.UNKNOWN_ADMIN_TOKEN;
import sonia.webapp.qrtravel.db.Database;
import sonia.webapp.qrtravel.db.Room;
import sonia.webapp.qrtravel.ldap.LdapAccount;
import sonia.webapp.qrtravel.ldap.LdapUtil;
import sonia.webapp.qrtravel.util.ErrorMessage;

/**
 *
 * @author Dr.-Ing. Thorsten Ludewig <t.ludewig@ostfalia.de>
 */
@Controller
@Scope("session")
public class AdminQRExportController
{
  private final static String QR_TRAVEL_ERROR_MESSAGE = "QR_TRAVEL_ERROR_MESSAGE";

  private final static Logger LOGGER = LoggerFactory.getLogger(
    AdminQRExportController.class.getName());

  private final static Config CONFIG = Config.getInstance();

  @GetMapping("/admin/qrexport")
  public String httpGetAdminQrExportPage(
    @CookieValue(value = QR_TRAVEL_ADMIN_TOKEN,
                 defaultValue = UNKNOWN_ADMIN_TOKEN) String tokenValue,
    HttpServletResponse response, HttpServletRequest request, Model model,
    @RequestParam(name = "owneruid", required = false) String ownerUid)
  {
    LOGGER.debug("Admin qr export GET request");
    QrTravelAdminToken token = QrTravelAdminToken.fromCookieValue(tokenValue);

    ErrorMessage errorMessage = (ErrorMessage) request.getSession(true).
      getAttribute(QR_TRAVEL_ERROR_MESSAGE);
    LOGGER.debug("Error message ({})", errorMessage);

    List<Room> rooms = Database.listRooms();
    HashMap<String, QRExportInfo> ownersRooms = new HashMap<>();

    if (!Strings.isNullOrEmpty(ownerUid))
    {
      ownerUid = ownerUid.trim().toLowerCase();

      for (Room r : rooms)
      {
        String roomOwner = r.getOwnerUid().trim().toLowerCase();

        if (roomOwner.startsWith(ownerUid))
        {
          QRExportInfo exportInfo = null;

          LOGGER.debug(roomOwner + " " + r.getDescription());

          try
          {
            exportInfo = ownersRooms.get(roomOwner);
          }
          catch (NullPointerException e)
          {
            //
          }

          if (exportInfo == null)
          {
            LdapAccount account = LdapUtil.searchForUid(roomOwner);
            
            if ( account != null )
            {
              exportInfo = new QRExportInfo(roomOwner, account.getGivenName()
              + " " + account.getSn(), account.getMail(), account.getLocality());
            }
            else
            {
              exportInfo = new QRExportInfo( roomOwner, "", "", r.getDomain() );
            }
          }

          exportInfo.incrementRoom();

          ownersRooms.put(roomOwner, exportInfo);
          LOGGER.debug(roomOwner + " " + exportInfo + " " + ownersRooms.size());
        }
      }
    }

    ArrayList<QRExportInfo> infoList = new ArrayList<>();
    
    String[] keys = ownersRooms.keySet().toArray(new String[0]);
    Arrays.sort( keys );
    
    for( String k : keys )
    {
      infoList.add( ownersRooms.get(k));
    }
    
    model.addAttribute("token", token);
    model.addAttribute("rooms", rooms);
    model.addAttribute("ownersRooms", infoList);
    model.addAttribute("errorMessage", errorMessage);
    token.addToHttpServletResponse(response);
    return "adminQRExport";
  }
}
