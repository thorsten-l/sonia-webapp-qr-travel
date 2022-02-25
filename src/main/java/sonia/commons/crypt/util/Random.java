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
public class Random extends java.util.Random
{

  private final static Random singleton = new Random();

  //~--- constructors ---------------------------------------------------------
  private Random()
  {
    super(System.currentTimeMillis());
  }

  //~--- get methods ----------------------------------------------------------
  public static String getDigits(int numberOfDigits)
  {
    String str = null;

    if (numberOfDigits > 0)
    {
      str = "";

      for (int i = 0; i < numberOfDigits; i++)
      {
        str += singleton.nextInt(10);
      }
    }

    return str;
  }

  public static Random getRandom()
  {
    return singleton;
  }
}
