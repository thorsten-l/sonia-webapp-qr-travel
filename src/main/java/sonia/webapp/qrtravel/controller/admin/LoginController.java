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
package sonia.webapp.qrtravel.controller.admin;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import sonia.webapp.qrtravel.Config;
import sonia.webapp.qrtravel.QrTravelAdminToken;
import static sonia.webapp.qrtravel.QrTravelAdminToken.QR_TRAVEL_ADMIN_TOKEN;
import static sonia.webapp.qrtravel.QrTravelAdminToken.UNKNOWN_ADMIN_TOKEN;
import sonia.webapp.qrtravel.form.LoginForm;
import sonia.webapp.qrtravel.ldap.LdapAccount;
import sonia.webapp.qrtravel.ldap.LdapUtil;
import sonia.webapp.qrtravel.ldap.LoginAttempt;
import sonia.webapp.qrtravel.util.ErrorMessage;

/**
 *
 * @author Thorsten Ludewig (t.ludewig@gmail.com)
 */
@Controller
public class LoginController
{
  private final static Logger LOGGER = LoggerFactory.getLogger(
    LoginController.class.getName());

  private final static Config CONFIG = Config.getInstance();

  @GetMapping("/sys/login")
  public String httpGetLoginPage(
    @CookieValue(value = QR_TRAVEL_ADMIN_TOKEN,
                 defaultValue = UNKNOWN_ADMIN_TOKEN) String tokenValue,
    HttpServletResponse response, Model model, LoginForm loginForm)
  {
    String page = "login";
    LOGGER.debug("Login GET Request");
    QrTravelAdminToken token = QrTravelAdminToken.fromCookieValue(tokenValue);
    if( token.isAuthenticated() )
    {
      page = "redirect:/admin";
    }
    model.addAttribute("token", token);
    model.addAttribute("errormessge", null );
    token.addToHttpServletResponse(response);
    return page;
  }

  @GetMapping("/sys/logout")
  public String httpGetLogoutPage(
    @CookieValue(value = QR_TRAVEL_ADMIN_TOKEN,
                 defaultValue = UNKNOWN_ADMIN_TOKEN) String tokenValue,
    HttpServletResponse response, Model model, LoginForm loginForm)
  {
    LOGGER.debug("Login GET Request");
    QrTravelAdminToken token = QrTravelAdminToken.fromCookieValue(tokenValue);
    token.setAuthenticated(false);
    model.addAttribute("token", token);
    token.addToHttpServletResponse(response);
    return "redirect:/sys/login";
  }

  @PostMapping("/sys/login")
  public String httpPostLoginPage(
    @CookieValue(value = QR_TRAVEL_ADMIN_TOKEN,
                 defaultValue = UNKNOWN_ADMIN_TOKEN) String tokenValue,
    HttpServletResponse response, Model model, @Valid LoginForm loginForm,
    BindingResult bindingResult)
  {
    String page = "login";
    boolean dataCommitted = false;
    ErrorMessage errorMessage = null;
    
    LOGGER.debug("Login POST Request");
    QrTravelAdminToken token = QrTravelAdminToken.fromCookieValue(tokenValue);

    if (bindingResult.hasErrors())
    {
      LOGGER.error("bind result has errors");
      List<FieldError> fel = bindingResult.getFieldErrors();
      for (FieldError fe : fel)
      {
        LOGGER.trace(fe.toString());
      }
    }
    else
    {
      LOGGER.trace("user id = {}", loginForm.getUserId());
      LOGGER.trace("password = {}", loginForm.getPassword());
      
      LoginAttempt loginAttempt = LdapUtil.getLoginAttempt(loginForm.getUserId());
      LOGGER.debug("loginAttempt=" + loginAttempt);
      
      if (loginAttempt != null && loginAttempt.getCounter() < CONFIG.
        getMaxLoginAttempts())
      {
        LdapAccount account = LdapUtil.adminBind(loginForm.getUserId(),
          loginForm.getPassword());

        if (account != null)
        {
          dataCommitted = true;
          token.setAuthenticated(true);
          token.setSn(account.getSn());
          token.setGivenName(account.getGivenName());
          token.setUid(loginForm.getUserId());
          page = "redirect:/admin";
        }
        else
        {
          loginAttempt.incrementCounter();
          errorMessage = new ErrorMessage( "Fehler", "Zugangsdaten falsch!" );
        }
      }
      else
      {
        errorMessage = new ErrorMessage( "Account gesperrt", "Dieser Account ist für " + CONFIG.
          getLoginFailedBlockingDuration() + "s gesperrt!");
      }
    }

    model.addAttribute("dataCommitted", dataCommitted);
    model.addAttribute("errorMessage", errorMessage);
    model.addAttribute("token", token);
    token.addToHttpServletResponse(response);
    return page;
  }
}
