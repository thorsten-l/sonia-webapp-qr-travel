/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sonia.webapp.qrtravel;

import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import static sonia.webapp.qrtravel.QrTravelToken.QR_TRAVEL_TOKEN;
import static sonia.webapp.qrtravel.QrTravelToken.UNKNOWN_TOKEN;
import sonia.webapp.qrtravel.db.Database;

@Controller
public class SampleController
{
  private final static Config CONFIG = Config.getInstance();
  private final static Logger LOGGER = LoggerFactory.getLogger(SampleController.class.getName());
  
  @GetMapping("/sample")
  public String home(@RequestHeader(name = "x-original-uri", required = false,
                                      defaultValue = "") String originalUri,
    @RequestParam( name="p", required = false ) String pin,
    @CookieValue(value = QR_TRAVEL_TOKEN, defaultValue = UNKNOWN_TOKEN) String tokenValue,
    HttpServletResponse response, Model model)
  {
    QrTravelToken token = QrTravelToken.fromCookieValue(tokenValue);
    
    LOGGER.info( "pin = " + pin );
    
    LOGGER.info( "token mail = " + token.getMail() );
    LOGGER.info( "token phone = " + token.getPhone() );
    LOGGER.info( "token surname = " + token.getSurname() );
    LOGGER.info( "token given name = " + token.getGivenName() );
    
    /*
    token.setMail("t.ludewig@ostfalia.de");
    token.setPhone("+49 5331 939 19000");
    token.setGivenName("Thorsten");
    token.setSurname("Ludewig");
    */
    
    model.addAttribute("room", Database.findRoom(pin));
    model.addAttribute("pin", pin );
    model.addAttribute("mail", token.getMail());
    model.addAttribute("phone", token.getPhone());
    model.addAttribute("surname", token.getSurname());
    model.addAttribute("givenname", token.getGivenName());
   
   //  model.addAttribute("description", description );
   //  model.addAttribute("studentNumber", studentNumber );
 
    token.setLastPin(pin);
    token.addToHttpServletResponse(response);
    return "sample";
  }

}