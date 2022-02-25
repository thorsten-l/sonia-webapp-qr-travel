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
package sonia.commons.crypt.util;

/**
 *
 * @author Thorsten Ludewig (t.ludewig@gmail.com)
 */
public class HEX
{

  /** Field description */
  public final static char HEXCHARS[] =
  {
    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E',
    'F'
  };

  //~--- methods --------------------------------------------------------------

  public static String convert(byte b)
  {
    return "" + HEXCHARS[(b & 0xff) >> 4] + HEXCHARS[b & 0x0f];
  }

  public static String convert(byte array[])
  {
    String returnValue = "";

    for (int i = 0; i < array.length; i++)
    {
      returnValue += (convert(array[i]));
    }

    return returnValue;
  }
}
