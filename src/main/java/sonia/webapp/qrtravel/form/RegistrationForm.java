package sonia.webapp.qrtravel.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import sonia.webapp.qrtravel.QrTravelToken;

/**
 *
 * @author Thorsten Ludewig (t.ludewig@ostfalia.de)
 */
@ToString
public class RegistrationForm implements AttendeeForm
{
  @Override
  public void setAttendeeData(String pin, QrTravelToken token)
  {
    this.pin = pin;
    this.phone = token.getPhone();
    this.location = token.getLocation();
    this.city = token.getCity();
    this.street = token.getStreet();
    this.mail = token.getMail();
    this.givenName = token.getGivenName();
    this.surname = token.getSurname();
  }

  @NotNull
  @Size(min = 6, message = "Minimum 6 Zeichen")
  @Getter
  @Setter
  private String pin;

  @Pattern(regexp = "^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$",
           message = "Formatfehler: e-Mail")
  @Getter
  @Setter
  private String mail;

  @NotNull
  @Size(min = 4, message = "Minimum 4 Zeichen")
  @Getter
  @Setter
  private String phone;

  @NotNull
  @Getter
  @Setter
  @Size(min = 2, message = "Minimum 2 Zeichen")
  private String surname;

  @NotNull
  @Getter
  @Setter
  @Size(min = 2, message = "Minimum 2 Zeichen")
  private String givenName;

  @Getter
  @Setter
  private String location;

  @Getter
  @Setter
  @NotNull
  @Size(min = 2, message = "Minimum 2 Zeichen")
  private String street;

  @Getter
  @Setter
  @NotNull
  @Size(min = 2, message = "Minimum 2 Zeichen")
  private String city;
}
