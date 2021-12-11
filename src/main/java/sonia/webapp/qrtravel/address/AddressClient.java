package sonia.webapp.qrtravel.address;

import java.util.List;

/**
 *
 * @author Thorsten Ludewig (t.ludewig@ostfalia.de)
 */
public interface AddressClient
{
  List<Address> searchByUid( String uid );
  List<Address> searchByCardNumber( String cardNumber );
}
