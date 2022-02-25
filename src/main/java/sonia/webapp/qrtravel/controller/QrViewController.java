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
package sonia.webapp.qrtravel.controller;

import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sonia.webapp.qrtravel.Config;
import sonia.webapp.qrtravel.db.Database;

/**
 * 
 * @author Thorsten Ludewig (t.ludewig@gmail.com)
 */
@Controller
public class QrViewController
{
  private final static Config CONFIG = Config.getInstance();

  private final static Logger LOGGER = LoggerFactory.getLogger(
    QrViewController.class.getName());

  @GetMapping("/view")
  public String qrPage(
    @RequestParam(name = "p", required = true) String pin,
    @RequestParam(name = "l", required = false) String location,
    HttpServletResponse response, Model model)
  {
    LOGGER.info("pin = " + pin + ", location=" + location );
    
    String qrUrl = CONFIG.getWebServiceUrl() + "/qrcode?p=" + pin;
    String apiUrl = CONFIG.getWebServiceUrl() + "/room?p=" + pin;
    
    if ( location != null )
    {
      qrUrl += "&l=" + location;
    }
    
    model.addAttribute("webServiceUrl", CONFIG.getWebServiceUrl());
    model.addAttribute("room", Database.findRoom(pin));
    model.addAttribute("location", location );
    model.addAttribute("qrUrl", qrUrl );
    model.addAttribute("apiUrl", apiUrl );
    return "view";
  }
}
