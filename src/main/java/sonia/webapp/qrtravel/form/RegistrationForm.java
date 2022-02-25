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
 * @author Thorsten Ludewig (t.ludewig@gmail.com)
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
