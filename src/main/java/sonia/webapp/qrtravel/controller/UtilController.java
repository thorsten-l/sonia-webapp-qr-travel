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

import java.io.IOException;
import java.util.HashMap;
import javax.servlet.http.HttpServletResponse;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sonia.webapp.qrtravel.BuildProperties;
import sonia.webapp.qrtravel.Config;
import sonia.webapp.qrtravel.cronjob.CheckExpiredJob;
import sonia.webapp.qrtravel.QrTravelToken;
import static sonia.webapp.qrtravel.QrTravelToken.QR_TRAVEL_TOKEN;
import static sonia.webapp.qrtravel.QrTravelToken.UNKNOWN_TOKEN;

/**
 * 
 * @author Thorsten Ludewig (t.ludewig@gmail.com)
 */
@RestController
public class UtilController
{
  private final static Logger LOGGER = LoggerFactory.getLogger(
    UtilController.class.getName());
  
  @GetMapping(path = "/api/build", produces = MediaType.APPLICATION_JSON_VALUE)
  public BuildProperties buildProperties()
  {
    LOGGER.debug("get build request");
    return BuildProperties.getInstance();
  }
  
  @GetMapping(path = "/acceptCookie",
              produces = MediaType.APPLICATION_JSON_VALUE)
  public HashMap<String, Boolean> acceptCookie(@CookieValue(
    value = QR_TRAVEL_TOKEN, defaultValue = UNKNOWN_TOKEN) String tokenValue,
    HttpServletResponse response)
  {
    LOGGER.debug("accept cookie request");
    QrTravelToken token = QrTravelToken.fromCookieValue(tokenValue);
    token.setCookieAccepted(true);
    HashMap<String, Boolean> map = new HashMap<>();
    map.put("cookieAccepted", Boolean.TRUE);
    LOGGER.debug(token.toString());
    token.addToHttpServletResponse(response);
    return map;
  }
  
  @GetMapping(path = "/api/check", produces = MediaType.APPLICATION_JSON_VALUE)
  public HashMap<String, Boolean> checkExpired()
  {
    LOGGER.debug("checkExpired request");
    
    try
    {
      CheckExpiredJob job = new CheckExpiredJob();
      job.execute(null);
    }
    catch (JobExecutionException ex)
    {
      LOGGER.error("checkExpired job failed ", ex);
    }
    
    HashMap<String, Boolean> map = new HashMap<>();
    map.put("check", Boolean.TRUE);
    return map;
  }
  
  @GetMapping(path = "/admin/reloadconfig",
              produces = MediaType.APPLICATION_JSON_VALUE)
  public HashMap<String, Boolean> adminReloadConfig()
  {
    LOGGER.debug("Reload config request");
    
    try
    {
      Config.readConfig();
    }
    catch (IOException ex)
    {
      LOGGER.error("Error reading config. ", ex);
    }
    
    HashMap<String, Boolean> map = new HashMap<>();
    map.put("reload", Boolean.TRUE);
    return map;
  }
}
