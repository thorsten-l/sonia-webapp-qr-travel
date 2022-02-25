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
package sonia.webapp.qrtravel.address;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sonia.webapp.qrtravel.Config;

/**
 *
 * @author Thorsten Ludewig (t.ludewig@gmail.com)
 */
public class OstfaliaAddressClientImpl extends GenericAddressClientImpl
{
  private final static Logger LOGGER = LoggerFactory.getLogger(
    OstfaliaAddressClientImpl.class.getName());

  private final static Config CONFIG = Config.getInstance();

  @Override
  public List<Address> searchByCardNumber(String cardNumber)
  {
    LOGGER.debug("seraching address for cardnumber = {}", cardNumber );
    
    //TODO: implement search
    
    return new ArrayList<>();
  }
}
