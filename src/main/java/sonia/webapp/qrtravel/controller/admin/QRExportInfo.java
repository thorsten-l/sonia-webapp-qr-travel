package sonia.webapp.qrtravel.controller.admin;

import lombok.Getter;
import lombok.ToString;

/**
 *
 * @author Thorsten Ludewig (t.ludewig@ostfalia.de)
 */
@ToString
public class QRExportInfo
{
  public QRExportInfo( String ownerUid, String ownerName, String email, String l )
  {
    this.ownerUid = ownerUid;
    this.ownerName = ownerName;
    this.email = email;
    this.l = l;
  }
  
  public void incrementRoom()
  {
    roomCounter++;
  }
  
  @Getter
  private final String ownerUid;
  
  @Getter
  private final String ownerName;
  
  @Getter
  private final String email;
  
  @Getter
  private final String l;
  
  @Getter
  private int roomCounter;
}
