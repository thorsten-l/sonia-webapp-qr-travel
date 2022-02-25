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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import sonia.webapp.qrtravel.Config;
import sonia.webapp.qrtravel.QrTravelAdminToken;
import static sonia.webapp.qrtravel.QrTravelAdminToken.QR_TRAVEL_ADMIN_TOKEN;
import static sonia.webapp.qrtravel.QrTravelAdminToken.UNKNOWN_ADMIN_TOKEN;
import sonia.webapp.qrtravel.util.ErrorMessage;

/**
 *
 * @author Thorsten Ludewig (t.ludewig@gmail.com)
 */
@Controller
@Scope("session")
public class AdminTraceContactsController
{
  private final static String QR_TRAVEL_ERROR_MESSAGE = "QR_TRAVEL_ERROR_MESSAGE";

  private final static Logger LOGGER = LoggerFactory.getLogger(
    AdminTraceContactsController.class.getName());

  private final static Config CONFIG = Config.getInstance();

  @GetMapping("/admin/trace-contacts")
  public String httpGetAdminTracePage(
    @CookieValue(value = QR_TRAVEL_ADMIN_TOKEN,
                 defaultValue = UNKNOWN_ADMIN_TOKEN) String tokenValue,
    HttpServletResponse response, HttpServletRequest request, Model model)
  {
    LOGGER.debug("Admin trace contacts GET request");
    QrTravelAdminToken token = QrTravelAdminToken.fromCookieValue(tokenValue);

    ErrorMessage errorMessage = (ErrorMessage) request.getSession(true).
      getAttribute(QR_TRAVEL_ERROR_MESSAGE);
    LOGGER.debug("Error message ({})", errorMessage);

    model.addAttribute("token", token);
    model.addAttribute("errorMessage", errorMessage);
    token.addToHttpServletResponse(response);
    return "adminTrace";
  }
}
