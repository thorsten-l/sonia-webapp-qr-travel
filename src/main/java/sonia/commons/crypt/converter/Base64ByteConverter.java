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
package sonia.commons.crypt.converter;

//~--- non-JDK imports --------------------------------------------------------

import sonia.commons.crypt.util.Convert;

/**
 *
 * @author Thorsten Ludewig (t.ludewig@gmail.com)
 * @since 1.1.0
 */
public class Base64ByteConverter implements ByteConverter
{

  @Override
  public byte[] decode(String data)
  {
    return Convert.fromBase64(data);
  }

  @Override
  public String encode(byte[] data)
  {
    return Convert.toBase64(data);
  }
}
