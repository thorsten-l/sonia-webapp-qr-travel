package sonia.webapp.qrtravel.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.ToString;

/**
 *
 * @author Thorsten Ludewig (t.ludewig@ostfalia.de)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class ApiRequest
{
  @Getter
  private String authToken;

  @Getter
  private String phone;

  @Getter
  private String pin;

  @Getter
  private String location;

  @Getter
  private String username;

  @Getter
  private String password;
}
