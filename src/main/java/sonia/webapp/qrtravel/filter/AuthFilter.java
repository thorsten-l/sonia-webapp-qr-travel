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
package sonia.webapp.qrtravel.filter;

import com.google.common.base.Strings;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import sonia.webapp.qrtravel.Config;
import sonia.webapp.qrtravel.QrTravelAdminToken;

/**
 *
 * @author Thorsten Ludewig (t.ludewig@gmail.com)
 */
@Component
@Order(1)
public class AuthFilter implements Filter
{
  private final static org.slf4j.Logger LOGGER = LoggerFactory.getLogger(
    AuthFilter.class.getName());
  
  private final static Config CONFIG = Config.getInstance();
  
  @Override
  public void doFilter(ServletRequest servletRequest,
    ServletResponse servletResponse,
    FilterChain chain) throws IOException, ServletException
  {
    HttpServletRequest request = (HttpServletRequest) servletRequest;
    HttpServletResponse response = (HttpServletResponse) servletResponse;

    boolean followChain = true;

    String uri = request.getRequestURI();
    
    String contextPath = "";
    
    if ( ! Strings.isNullOrEmpty(CONFIG.getContextPath()))
    {
      contextPath = CONFIG.getContextPath();
      uri = uri.substring(contextPath.length());
      if ( ! contextPath.endsWith("/"))
      {
        contextPath += "/";
      }
    }
    
    if (uri.startsWith("/sys") || uri.startsWith("/admin"))
    {
      LOGGER.debug("Logging Request {} : {}", request.getMethod(), uri);
      QrTravelAdminToken token = QrTravelAdminToken.fromHttpRequest(request);
      LOGGER.trace(token.toString());

      if (uri.startsWith("/admin") && !token.isAuthenticated())
      {
        followChain = false;
      }
      else
      {
        long lastAccessDiff = (System.currentTimeMillis()
          - token.getLastAccess()) / 1000;        
        if ( lastAccessDiff > (long)CONFIG.getAdminTokenTimeout() )
        {
          token.setAuthenticated(false);
          token.addToHttpServletResponse(response);
          followChain = false;
        }
      }
    }

    if (followChain)
    {
      chain.doFilter(request, response);
    }
    else
    {
      response.sendRedirect( contextPath + "/sys/login");
    }
  }
}
