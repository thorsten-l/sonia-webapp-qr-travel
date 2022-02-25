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
package sonia.webapp.qrtravel;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

/**
 * @author Thorsten Ludewig (t.ludewig@gmail.com)
 */
@Component
public class WebServerCustomizer
  implements WebServerFactoryCustomizer<TomcatServletWebServerFactory>
{

  private final static Logger LOGGER = LoggerFactory.getLogger(
    WebServerCustomizer.class.getName());

  private final static Config CONFIG = Config.getInstance();

  @Override
  public void customize(TomcatServletWebServerFactory factory)
  {
    LOGGER.info("WebServerCustomizer.customize");
    factory.setPort(CONFIG.getWebServicePort());

    if (!Strings.isNullOrEmpty(CONFIG.getContextPath()))
    {
      LOGGER.info("set context path to: " + CONFIG.getContextPath());
      factory.setContextPath(CONFIG.getContextPath());
    }
    else
    {
      LOGGER.info("set context path to root");
      factory.setContextPath("");
    }
  }
}
